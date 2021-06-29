package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.proc.data.etc.Nothing;
import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.util.GameAlert;
import org.bukkit.ChatColor;

import java.io.IOException;
import java.util.*;

public class FileParser {

    public static Map<String, ProcBundle> parseBundles(String name) {
        try {
            FileReader reader = new FileReader(name);
            Map<String, ProcBundle> bundles = new HashMap<>();
            String firstLine = reader.readLine();
            if (firstLine == null) return bundles;
            String line = cutFrontSpace(cutBackSpace(firstLine.replace("\t", "")));
            while (line != null) {
                if (isStartWith(line, Settings.getBundlePrefix())) {
                    String bundleName = cutFrontSpace(line.substring(line.indexOf(Settings.getBundlePrefix()) + Settings.getBundlePrefix().length()));
                    String lambda = "->";
                    int lambdaIndex = bundleName.indexOf(lambda);
                    if (lambdaIndex != -1) {
                        String lambdaBundleName = cutBackSpace(bundleName.substring(0, lambdaIndex)).toLowerCase();
                        String lambdaArgs = cutFrontSpace(bundleName.substring(lambdaIndex + lambda.length()));
                        if (lambdaArgs.length() != 0) {
                            List<Process> lambdaProcList = new ArrayList<>();
                            ParseUnit parseUnit = new ParseUnit();
                            Process process = parseProcess(parseUnit, lambdaArgs);
                            while  (parseUnit.hasBrace()) {
                                parseUnit.getFrontBrace().addProc(parseProcess(parseUnit, reader.readLine()));
                            }
                            lambdaProcList.add(process);
                            ProcBundle lambdaProcBundle = new ProcBundle(lambdaProcList);
                            bundles.put(lambdaBundleName, lambdaProcBundle);
                        }
                    }
                    bundleName = bundleName.toLowerCase();
                    if (bundleName.equals("")) continue;
                    List<Process> processList = new ArrayList<>();
                    ParseUnit parseUnit = new ParseUnit();
                    while ((line = reader.readLine()) != null) {
                        line = cutBackSpace(cutFrontSpace(line).replace("\t", ""));
                        if (isStartWith(line, Settings.getBundlePrefix())) break;
                        Process process = parseProcess(parseUnit, line);
                        while  (parseUnit.hasBrace()) {
                            parseUnit.getFrontBrace().addProc(parseProcess(parseUnit, reader.readLine()));
                        }
                        processList.add(process);
                    }
                    if (bundles.containsKey(bundleName)) {
                        GameAlert.DUPLICATED_BUNDLE.print(new String[]{bundleName});
                    } else bundles.put(bundleName, new ProcBundle(processList));
                } else line = reader.readLine();
            }
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
        string = FileParser.cutFrontSpace(origin);
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

    public static Nothing getNothing(ParseUnit parseUnit, String string) {
        Nothing nothing = new Nothing();
        nothing.parse(parseUnit, string);
        return nothing;
    }

    public static boolean isStartWith(String string, String regex) {
        if (string == null) return false;
        int index = string.indexOf(regex);
        return index == 0;
    }

    public static int getStartIndex(String string) {
        if (string == null) return -1;
        for (int i = string.length() - 1; i >= 0; i--) {
            if (string.charAt(i) != ' ' && string.charAt(i) != '\t') {
                return i;
            }
        }
        return -1;
    }

    public static String cutBackSpace(String string) {
        if (string == null) return null;
        for (int i = string.length() - 1; i >= 0; i--) {
            if (string.charAt(i) != ' ' && string.charAt(i) != '\t') {
                return string.substring(0, i + 1);
            }
        }
        return string;
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
