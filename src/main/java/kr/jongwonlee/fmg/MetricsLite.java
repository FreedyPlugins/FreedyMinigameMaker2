package kr.jongwonlee.fmg;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

public class MetricsLite
{
    private final ScheduledExecutorService scheduler;
    public static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bStats.org/submitData/bukkit";
    private final boolean enabled;
    private static boolean logFailedRequests;
    private static boolean logSentData;
    private static boolean logResponseStatusText;
    private static String serverUUID;
    private final Plugin plugin;
    private final int pluginId;
    
    public MetricsLite(final Plugin plugin, final int pluginId) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.plugin = plugin;
        this.pluginId = pluginId;
        final File bStatsFolder = new File(plugin.getDataFolder().getParentFile(), "bStats");
        final File configFile = new File(bStatsFolder, "config.yml");
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.isSet("serverUuid")) {
            config.addDefault("enabled", true);
            config.addDefault("serverUuid", UUID.randomUUID().toString());
            config.addDefault("logFailedRequests", false);
            config.addDefault("logSentData", false);
            config.addDefault("logResponseStatusText", false);
            config.options().header("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo honor their work, you should not disable it.\nThis has nearly no effect on the server performance!\nCheck out https://bStats.org/ to learn more :)").copyDefaults(true);
            try {
                config.save(configFile);
            }
            catch (IOException ignored) {}
        }
        MetricsLite.serverUUID = config.getString("serverUuid");
        MetricsLite.logFailedRequests = config.getBoolean("logFailedRequests", false);
        this.enabled = config.getBoolean("enabled", true);
        MetricsLite.logSentData = config.getBoolean("logSentData", false);
        MetricsLite.logResponseStatusText = config.getBoolean("logResponseStatusText", false);
        if (this.enabled) {
            boolean found = false;
            for (final Class<?> service : Bukkit.getServicesManager().getKnownServices()) {
                try {
                    service.getField("B_STATS_VERSION");
                    found = true;
                }
                catch (NoSuchFieldException ex2) {
                    continue;
                }
                break;
            }
            Bukkit.getServicesManager().register((Class)MetricsLite.class, (Object)this, plugin, ServicePriority.Normal);
            if (!found) {
                this.startSubmitting();
            }
        }
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    private void startSubmitting() {
        final Runnable submitTask = () -> {
            if (!this.plugin.isEnabled()) {
                this.scheduler.shutdown();
            }
            else {
                Bukkit.getScheduler().runTask(this.plugin, this::submitData);
            }
        };
        final long initialDelay = (long)(60000.0 * (3.0 + Math.random() * 3.0));
        final long secondDelay = (long)(60000.0 * (Math.random() * 30.0));
        this.scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
        this.scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1800000L, TimeUnit.MILLISECONDS);
    }
    
    public JsonObject getPluginData() {
        final JsonObject data = new JsonObject();
        final String pluginName = this.plugin.getDescription().getName();
        final String pluginVersion = this.plugin.getDescription().getVersion();
        data.addProperty("pluginName", pluginName);
        data.addProperty("id", this.pluginId);
        data.addProperty("pluginVersion", pluginVersion);
        data.add("customCharts", new JsonArray());
        return data;
    }
    
    private JsonObject getServerData() {
        int playerAmount;
        try {
            final Method onlinePlayersMethod = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers");
            playerAmount = (onlinePlayersMethod.getReturnType().equals(Collection.class) ? ((Collection)onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).size() : ((Player[])onlinePlayersMethod.invoke(Bukkit.getServer(), new Object[0])).length);
        }
        catch (Exception e) {
            playerAmount = Bukkit.getOnlinePlayers().size();
        }
        final int onlineMode = Bukkit.getOnlineMode() ? 1 : 0;
        final String bukkitVersion = Bukkit.getVersion();
        final String bukkitName = Bukkit.getName();
        final String javaVersion = System.getProperty("java.version");
        final String osName = System.getProperty("os.name");
        final String osArch = System.getProperty("os.arch");
        final String osVersion = System.getProperty("os.version");
        final int coreCount = Runtime.getRuntime().availableProcessors();
        final JsonObject data = new JsonObject();
        data.addProperty("serverUUID", MetricsLite.serverUUID);
        data.addProperty("playerAmount", playerAmount);
        data.addProperty("onlineMode", onlineMode);
        data.addProperty("bukkitVersion", bukkitVersion);
        data.addProperty("bukkitName", bukkitName);
        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", coreCount);
        return data;
    }
    
    private void submitData() {
        final JsonObject data = this.getServerData();
        final JsonArray pluginData = new JsonArray();
        for (final Class<?> service : Bukkit.getServicesManager().getKnownServices()) {
            try {
                service.getField("B_STATS_VERSION");
                for (RegisteredServiceProvider<?> provider : Bukkit.getServicesManager().getRegistrations(service)) {
                    try {
                        final Object plugin = provider.getService().getMethod("getPluginData").invoke(provider.getProvider());
                        if (plugin instanceof JsonObject) {
                            pluginData.add((JsonElement)plugin);
                        }
                        else {
                            try {
                                final Class<?> jsonObjectJsonSimple = Class.forName("org.json.simple.JSONObject");
                                if (!plugin.getClass().isAssignableFrom(jsonObjectJsonSimple)) {
                                    continue;
                                }
                                final Method jsonStringGetter = jsonObjectJsonSimple.getDeclaredMethod("toJSONString");
                                jsonStringGetter.setAccessible(true);
                                final String jsonString = (String)jsonStringGetter.invoke(plugin, new Object[0]);
                                final JsonObject object = new JsonParser().parse(jsonString).getAsJsonObject();
                                pluginData.add(object);
                            }
                            catch (ClassNotFoundException e) {
                                if (!MetricsLite.logFailedRequests) {
                                    continue;
                                }
                                this.plugin.getLogger().log(Level.SEVERE, "Encountered unexpected exception ", e);
                            }
                        }
                    }
                    catch (NullPointerException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {}
                }
            }
            catch (NoSuchFieldException ignored) {}
        }
        data.add("plugins", pluginData);
        new Thread(() -> {
            try {
                sendData(this.plugin, data);
            }
            catch (Exception e2) {
                if (MetricsLite.logFailedRequests) {
                    this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + this.plugin.getName(), e2);
                }
            }
        }).start();
    }
    
    private static void sendData(final Plugin plugin, final JsonObject data) throws Exception {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
        }
        if (MetricsLite.logSentData) {
            plugin.getLogger().info("Sending data to bStats: " + data);
        }
        final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://bStats.org/submitData/bukkit").openConnection();
        final byte[] compressedData = compress(data.toString());
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip");
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "MC-Server/1");
        connection.setDoOutput(true);
        try (final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
            outputStream.write(compressedData);
        }
        final StringBuilder builder = new StringBuilder();
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
        }
        if (MetricsLite.logResponseStatusText) {
            plugin.getLogger().info("Sent data to bStats and received response: " + builder);
        }
    }
    
    private static byte[] compress(final String str) throws IOException {
        if (str == null) {
            return null;
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final GZIPOutputStream gzip = new GZIPOutputStream(outputStream)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        }
        return outputStream.toByteArray();
    }
    
    static {
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            final String defaultPackage = new String(new byte[] { 111, 114, 103, 46, 98, 115, 116, 97, 116, 115, 46, 98, 117, 107, 107, 105, 116 });
            final String examplePackage = new String(new byte[] { 121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101 });
            if (MetricsLite.class.getPackage().getName().equals(defaultPackage) || MetricsLite.class.getPackage().getName().equals(examplePackage)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
        }
    }
}
