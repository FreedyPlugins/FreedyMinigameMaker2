package kr.jongwonlee.fmg.setting;

import kr.jongwonlee.fmg.util.YamlStore;

public class Settings {

    private static final String hubGameName = "hub";
    private static String extension;
    private static String bundlePrefix;
    private static boolean hideJoinLeaveMessage;
    private static boolean cancelBlockExplode;

    public static void init() {
        YamlStore yamlStore = new YamlStore("settings.yml");
        hideJoinLeaveMessage = yamlStore.getBoolean("hideJoinLeaveMessage");
        cancelBlockExplode = yamlStore.getBoolean("cancelBlockExplode");
        extension = yamlStore.getString("fileExtension").toLowerCase();
        bundlePrefix = yamlStore.getString("bundlePrefix").toLowerCase();
    }

    public static boolean isHideJoinLeaveMessage() {
        return hideJoinLeaveMessage;
    }

    public static boolean isCancelBlockExplode() {
        return cancelBlockExplode;
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
