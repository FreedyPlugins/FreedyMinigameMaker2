package kr.jongwonlee.fmg.proc.data.control;

import kr.jongwonlee.fmg.game.MiniGame;
import kr.jongwonlee.fmg.proc.Process;
import kr.jongwonlee.fmg.proc.*;

import java.util.HashMap;
import java.util.Map;

@Processable(alias = "nothing")
public class Nothing implements Process {

    String value;
    Process process;
    Process frontProcess;

    public String getValue() {
        return value;
    }

    @Override
    public ProcType getType() {
        return ProcType.NOTHING;
    }

    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        String args = arguments.trim();
        if (args.length() == 0) {
            return;
        }
        int index = args.indexOf(' ');

        String frontArg = index == -1 ? args : args.substring(0, index + 1);
        int frontQuote = frontArg.indexOf('\"');
        int frontSmallQuote = frontArg.indexOf('\'');
        if (frontQuote != -1 && args.charAt(frontQuote == 0 ? 0 : frontQuote - 1) != '\\') {
            int endQuote = frontQuote;
            while (true) {
                endQuote = args.indexOf('\"', ++endQuote);
                if (endQuote == -1) break;
                else if (args.charAt(endQuote - 1) != '\\') {
                    if (frontQuote > 0) frontProcess = FileParser.parseProcess(parseUnit, frontArg.substring(0, frontQuote));
                    value = repEscapes(args.substring(frontQuote + 1, endQuote));
                    String endArg = args.substring(endQuote + 1);
                    if (endArg.length() > 0) process = FileParser.parseProcess(parseUnit, endArg);
                    if (process instanceof MathOperator) process = null;
                    return;
                }
            }
        } else if (frontSmallQuote != -1 && args.charAt(frontSmallQuote == 0 ? 0 : frontSmallQuote - 1) != '\\') {
            int endQuote = frontSmallQuote;
            while (true) {
                endQuote = args.indexOf('\'', ++endQuote);
                if (endQuote == -1) break;
                else if (args.charAt(endQuote - 1) != '\\') {
                    if (frontSmallQuote > 0) frontProcess = FileParser.parseProcess(parseUnit, frontArg.substring(0, frontSmallQuote));
                    value = repEscapes(args.substring(frontSmallQuote + 1, endQuote));
                    String endArg = args.substring(endQuote + 1);
                    if (endArg.length() > 0) process = FileParser.parseProcess(parseUnit, endArg);
                    if (process instanceof MathOperator) process = null;
                    return;
                }
            }
        }
        int markUp = frontArg.indexOf("//");
        if (markUp != -1) value = repEscapes(frontArg.substring(0, markUp));
        else {
            value = repEscapes(frontArg);
            if (index != -1) {
                process = FileParser.parseProcess(parseUnit, args.substring(index));
                if (process.getType() != ProcType.NOTHING) value = args.substring(0, index);
                if (process instanceof MathOperator) process = null;
            }
        }
    }

/*    @Override
    public void parse(ParseUnit parseUnit, String arguments) {
        String args = FileParser.cutFrontSpace(arguments);
        if (args.length() >= 1) {
            int quoteIndex = args.indexOf('\"');
            if (quoteIndex != -1) {
                quoteIndex++;
                int frontQuoteIndex = quoteIndex;
                if (frontQuoteIndex < args.length()) {
                    while (true) {
                        quoteIndex = args.indexOf("\"", quoteIndex);
                        if (args.charAt(quoteIndex - 1) != '\\') {
                            value = args.substring(frontQuoteIndex, quoteIndex);
                            process = FileParser.parseProcess(parseUnit, args.substring(quoteIndex + 1));
                            if (frontQuoteIndex != 1) {
                                String frontArgs = args.substring(0, frontQuoteIndex - 1);
                                if (frontArgs.charAt(frontArgs.length() - 1) != ' ')
                                    frontProcess = FileParser.parseProcess(parseUnit, frontArgs + ' ');
                                else frontProcess = FileParser.parseProcess(parseUnit, frontArgs);
                            }
                            return;
                        } else quoteIndex++;
                    }
                }
            } else {
                int markUpIndex = args.indexOf("//");
                if (markUpIndex != -1) {
                    process = FileParser.parseProcess(parseUnit, args.substring(0, markUpIndex));
                    return;
                }
            }
        }
        int index = args.indexOf(' ');
        if (index <= 0) {
            ProcType procType = ProcType.getProcType(args);
            if (procType == null) {
                if (parseUnit.getFrontProcType() == ProcType.NOTHING) value = args + ' ';
                else value = args;
            } else {
                process = FileParser.parseProcess(parseUnit, args);
            }
            return;
        }
        String procName = args.substring(0, index);
        ProcType procType = ProcType.getProcType(procName);
        String procArgs = FileParser.cutFrontSpace(args.substring(index));
        if (procType == null) {
            process = FileParser.parseProcess(parseUnit, procArgs);
            if (parseUnit.getFrontProcType() == ProcType.NOTHING) value = procName + ' ';
            else value = procName;
        } else {
            process = FileParser.parseProcess(parseUnit, procType, procArgs);
        }
    }*/

    @Override
    public String run(MiniGame miniGame, ProcUnit procUnit) {
        if (frontProcess != null) {
            if (process == null) return frontProcess.run(miniGame, procUnit) + value;
            else return frontProcess.run(miniGame, procUnit) + value + process.run(miniGame, procUnit);
        } else if (process == null) {
            if (value == null) return "";
            else return value;
        } else if (value == null)
            return process.run(miniGame, procUnit);
        return value + process.run(miniGame, procUnit);
    }

    private static String repEscapes(String string) {
        if (string == null) return null;
        return string
                .replace("\\\\\\\"", "\\\"")
                .replace("\\\\\\'", "\\'")
                .replace("\\\\", "\\")
                .replace("\\'", "'")
                .replace("\\\"", "\"")
                .replace("\\n", "\n");

    }

}
