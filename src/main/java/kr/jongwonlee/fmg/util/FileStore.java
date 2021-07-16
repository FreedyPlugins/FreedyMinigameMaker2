package kr.jongwonlee.fmg.util;

import kr.jongwonlee.fmg.FMGPlugin;
import kr.jongwonlee.fmg.conf.Settings;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileStore {

    protected final File file;
    public static final Path DIR = Paths.get(File.separator + "games" + File.separator);
    protected static final String DOT = ".";
    private final JavaPlugin plugin;
    private final boolean inDir;

    public String getRoot() {
        return plugin.getDataFolder().getAbsolutePath();
    }

    public FileStore(String fileName) {
        this(fileName, false, FMGPlugin.getInst(), true);
    }

    public FileStore(String fileName, boolean inDir, boolean doInit) {
        this(fileName, inDir, FMGPlugin.getInst(), doInit);
    }

    public FileStore(JavaPlugin plugin, String fileName) {
        this(fileName, true, FMGPlugin.getInst(), true);
    }

    public FileStore(String fileName, boolean inDir) {
        this(fileName, inDir, FMGPlugin.getInst(), true);
    }

    public FileStore(final String fileName, boolean inDir, JavaPlugin plugin, boolean doInit) {
        this.inDir = inDir;
        this.plugin = plugin;
        this.file = new File(getRoot() + (inDir ? DIR : ""),fileName + "");
        if (!this.file.exists()) {
            try {
                this.file.getParentFile().mkdirs();
                if (!file.exists() && doInit) loadResource();
                this.file.createNewFile();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadResource() throws IOException {
            InputStream inputStream = plugin.getResource(file.getName().equals(Settings.getHubGameName() + Settings.getExtension()) ? "hub" : (inDir ? "game" : this.file.getName()));
            if (inputStream == null) return;
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            OutputStream outStream = new FileOutputStream(this.file);
            outStream.write(buffer);
            inputStream.close();
            outStream.close();
    }

    public File getFile() {
        return file;
    }

    public FileStore getInst() {
        return this;
    }

    public void deleteFile() {
        if (file.delete()) FMGPlugin.getInst().getLogger().info("File " + file.getName() + " Deleted");
    }

    public static List<String> getDirFiles(String otherPath) {
        final File file = new File(FMGPlugin.getInst().getDataFolder().getAbsolutePath(), DIR + otherPath);
        file.mkdirs();
        final Path path = file.toPath();
        try {
            return Files.walk(path)
                    .filter(f -> !Files.isDirectory(f))
                    .map(p -> {
                        final String string = path.relativize(p).toString();
                        return string.substring(0, string.lastIndexOf('.'));
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
