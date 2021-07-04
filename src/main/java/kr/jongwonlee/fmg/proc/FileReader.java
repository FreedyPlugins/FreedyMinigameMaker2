package kr.jongwonlee.fmg.proc;

import kr.jongwonlee.fmg.conf.Settings;
import kr.jongwonlee.fmg.util.FileStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileReader {

    private final BufferedReader reader;
    private String line;

    public FileReader(String name) throws IOException {
        FileStore fileStore = new FileStore(name + Settings.getExtension(), true);
        File file = fileStore.getFile();
        reader = Files.newBufferedReader(file.toPath());
    }

    private FileReader() {
        reader = null;
    }

    public static FileReader getReader(String line) {
        FileReader reader = new FileReader();
        reader.line = line;
        return reader;
    }

    public String readLine() throws IOException {
        if (reader != null) line = reader.readLine();
        return line;
    }

    public void closeFile() throws IOException {
        reader.close();
    }

    public String getLine() {
        return line;
    }

}
