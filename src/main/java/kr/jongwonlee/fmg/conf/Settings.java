package kr.jongwonlee.fmg.conf;

import kr.jongwonlee.fmg.util.YamlStore;

public class Settings {

    private static final String hubGameName = "hub";
    private static String extension;
    private static String bundlePrefix;

    public static void init() {
        YamlStore yamlStore = new YamlStore("settings.yml");
        extension = yamlStore.getString("fileExtension").toLowerCase();
        bundlePrefix = yamlStore.getString("bundlePrefix").toLowerCase();
    }

    public static String getHubGameName() {
        return hubGameName;
    }

    public static String getExtension() {
        return extension;
    }

    public static String getBundlePrefix() {
        return bundlePrefix;
    }

}
