package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.proc.data.control.MathOperator;
import kr.jongwonlee.fmg.proc.data.control.Nothing;
import kr.jongwonlee.fmg.proc.data.control.Then;
import kr.jongwonlee.fmg.util.GameAlert;
import org.bukkit.Bukkit;
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
                    if (bundleName.equals("")) {
                        line = reader.readLine();
                        parseUnit.clearExecutor();
                        continue;
                    }
                    int braceIndex = bundleName.indexOf("{");
                    if (braceIndex != -1) {
                        Process process = parseProcess(parseUnit, bundleName.substring(braceIndex));
                        bundleName = bundleName.substring(0, braceIndex).toLowerCase().trim();
                        while (parseUnit.hasBrace()) {
                            line = reader.readLine();
                            parseUnit.clearExecutor();
                            if (line != null) line = line.replace("\t", "").trim();
                            else break;
                            if (isStartWith(line, Settings.getBundlePrefix())) break;
                            parseUnit.getFrontBrace().addProc(parseUnit, parseProcess(parseUnit, line));
                        }
                        line = reader.readLine();
                        if (line != null) line = line.replace("\t", "").trim();
                        parseUnit.clearExecutor();
                        processList.add(process);
                        if (bundles.containsKey(bundleName)) {
                            GameAlert.DUPLICATED_BUNDLE.print(new String[]{bundleName});
                        } else bundles.put(bundleName, new ProcBundle(processList));
                        continue;
                    }
                    bundleName = bundleName.toLowerCase().trim();
                    body : while ((line = reader.readLine()) != null) {
                        parseUnit.clearExecutor();
                        line = line.replace("\t", "").trim();
                        if (isStartWith(line, Settings.getBundlePrefix())) break;
                        Process process = parseProcess(parseUnit, line);
                        while (parseUnit.hasBrace()) {
                            line = reader.readLine();
                            parseUnit.clearExecutor();
                            if (line == null) break body;
                            line = line.replace("\t", "").trim();
                            if (isStartWith(line, Settings.getBundlePrefix())) break;
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

    static final Map<Character, ProcType> procBraces = new HashMap<Character, ProcType>(){{
        put(' ', ProcType.NOTHING);
        put('\t', ProcType.NOTHING);
        put('{', ProcType.MID_FRONT_BRACE);
        put('}', ProcType.MID_END_BRACE);
        put('(', ProcType.SMALL_FRONT_BRACE);
        put(')', ProcType.SMALL_END_BRACE);
        put(',', ProcType.OR);
        put('|', ProcType.OR);
        put('&', ProcType.AND);
        put('+', ProcType.ADD);
        put('-', ProcType.SUBTRACT);
        put('/', ProcType.DIVIDE);
        put('%', ProcType.REMAINDER);
        put('*', ProcType.MULTIPLY);
    }};
    public static int getStartIndex(String string) {
        if (string == null) return -1;
        for (int i = 0; i < string.length(); i++) {
            if (procBraces.containsKey(string.charAt(i))) {
                return i == 0 ? -1 : i;
            }
        }
        return -1;
    }


    public static Process parseProcess(ParseUnit parseUnit, String string) {
        if (string == null) return getNothing(parseUnit, "");
        String origin = toColor(string);
        string = cutFrontSpace(origin);
        int index = getStartIndex(string);
        String processName = (index == -1 ? string : string.substring(0, index)).toLowerCase();
        ProcType procType = ProcType.getProcType(processName);
        Process externalProc;
        if (procType == null) {
            externalProc = ProcType.getExternalProc(processName);
            if (externalProc == null) {
                return getNothing(parseUnit, origin);
            }
        } else externalProc = null;
        String args = index == -1 ? "" : cutFrontSpace(string.substring(index));
        Process process = externalProc != null ? externalProc : procType.getNewProcess();
        process.parse(parseUnit, args);
        if (process instanceof MathOperator) return getNothing(parseUnit, "");
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
