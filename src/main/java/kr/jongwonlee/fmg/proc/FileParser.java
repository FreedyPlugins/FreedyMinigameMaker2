package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.proc.data.control.Nothing;
import kr.jongwonlee.fmg.proc.data.control.Then;
import kr.jongwonlee.fmg.util.GameAlert;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileParser {

    public static Map<String, ProcBundle> parseBundles(String name) {
        try {
            FileReader reader = new FileReader(name);
            Map<String, ProcBundle> bundles = new HashMap<>();
            String firstLine = reader.readLine();
            if (firstLine == null) return bundles;
            String line = firstLine.replace("\t", "").trim();
            while (line != null) {
                line = line.replace("\t", "").trim();
                if (isStartWith(line, Settings.getBundlePrefix())) {
                    String bundleName = line.substring(line.indexOf(Settings.getBundlePrefix()) + Settings.getBundlePrefix().length()).trim();
                    ParseUnit parseUnit = new ParseUnit();
                    List<Process> processList = new ArrayList<>();
                    if (bundleName.equals("")) continue;
                    int braceIndex = bundleName.indexOf("{");
                    if (braceIndex != -1) {
                        Process process = parseProcess(parseUnit, bundleName.substring(braceIndex));
                        bundleName = bundleName.substring(0, braceIndex).toLowerCase().trim();
                        while (parseUnit.hasBrace()) {
                            line = reader.readLine();
                            if (line != null) line = line.replace("\t", "").trim();
                            else break;
                            parseUnit.getFrontBrace().addProc(parseUnit, parseProcess(parseUnit, line));
                        }
                        line = reader.readLine();
                        if (line != null) line = line.replace("\t", "").trim();
                        processList.add(process);
                        if (bundles.containsKey(bundleName)) {
                            GameAlert.DUPLICATED_BUNDLE.print(new String[]{bundleName});
                        } else bundles.put(bundleName, new ProcBundle(processList));
                        continue;
                    }
                    bundleName = bundleName.toLowerCase().trim();
                    while ((line = reader.readLine()) != null) {
                        line = line.replace("\t", "").trim();
                        if (isStartWith(line, Settings.getBundlePrefix())) break;
                        Process process = parseProcess(parseUnit, line);
                        while (parseUnit.hasBrace()) {
                            line = reader.readLine().replace("\t", "").trim();
                            parseUnit.getFrontBrace().addProc(parseUnit, parseProcess(parseUnit, line));
                        }
                        processList.add(process);
                    }
                    if (bundles.containsKey(bundleName)) {
                        GameAlert.DUPLICATED_BUNDLE.print(new String[]{bundleName});
                    } else bundles.put(bundleName, new ProcBundle(processList));
                } else line = reader.readLine();
            }
            reader.closeFile();
            return bundles;
        } catch (IOException e) {
            e.printStackTrace();

            return new HashMap<>();
        }
    }

    public static Process parseProcess(ParseUnit parseUnit, ProcType procType, String string) {
        if (string == null) return getNothing(parseUnit, "");
        String origin = toColor(string);
        if (procType == null) return getNothing(parseUnit, string);
        string = cutFrontSpace(origin);
        int index = string.indexOf(' ');
        String args = cutFrontSpace(index == -1 ? "" : string.substring(index));
        Process process = procType.getNewProcess();
        process.parse(parseUnit, args);
        return process;
    }

    public static Process parseProcess(ParseUnit parseUnit, String string) {
        if (string == null) return getNothing(parseUnit, "");
        String origin = toColor(string);
        string = cutFrontSpace(origin);
        int index = string.indexOf(' ');
        String processName = (index == -1 ? string : string.substring(0, index)).toLowerCase();
        ProcType procType = ProcType.getProcType(processName);
        if (procType == null) return getNothing(parseUnit, origin);
        String args = index == -1 ? "" : cutFrontSpace(string.substring(index));
        Process process = procType.getNewProcess();
        process.parse(parseUnit, args);
        return process;
    }

    public static boolean isEmptyProcess(Process process) {
        if (process instanceof Nothing) {
            String value = ((Nothing) process).getValue();
            return value == null || value.trim().length() == 0;
        }
        return false;
    }

    public static Nothing getNothing(ParseUnit parseUnit, String string) {
        Nothing nothing = new Nothing();
        nothing.parse(parseUnit, string);
        return nothing;
    }

    public static Then getOneMoreLine(ParseUnit parseUnit, String string) {
        Then then = new Then();
        then.parse(parseUnit, string);
        return then;
    }

    public static boolean isStartWith(String string, String regex) {
        if (string == null) return false;
        int index = string.indexOf(regex);
        return index == 0;
    }

    @Deprecated
    public static int getStartIndex(String string) {
        if (string == null) return -1;
        for (int i = string.length() - 1; i >= 0; i--) {
            if (string.charAt(i) != ' ' && string.charAt(i) != '\t') {
                return i;
            }
        }
        return -1;
    }

    public static String cutFrontSpace(String string) {
        if (string == null) return null;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ' && string.charAt(i) != '\t') {
                return string.substring(i);
            }
        }
        return string;
    }

    public static String toColor(String string) {
        if (string == null) return null;
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
