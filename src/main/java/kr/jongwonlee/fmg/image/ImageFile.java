package kr.jongwonlee.fmg.image;

import kr.jongwonlee.fmg.FMGPlugin;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageFile {

    private final File file;
    public static final Path root = Paths.get(FMGPlugin.getInst().getDataFolder().getAbsolutePath() + File.separator);

    public ImageFile(final String fileName) {
        this.file = new File(root.toString(), fileName + ".png");
        if (!this.file.exists()) {
            try {
                final boolean mkdirs = this.file.getParentFile().mkdirs();
                if(!mkdirs) FMGPlugin.getInst().getLogger().info("File's folder created");
                final boolean newFile = this.file.createNewFile();
                setDefault();
                if(!newFile) FMGPlugin.getInst().getLogger().warning("Couldn't create the file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void remove() {
        file.delete();
        file.deleteOnExit();
    }

    public File getFile() {
        return file;
    }

    public void setDefault() throws IOException {
        InputStream inputStream = FMGPlugin.getInst().getResource("default.png");
        if (inputStream == null) return;
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        OutputStream outStream = new FileOutputStream(this.file);
        outStream.write(buffer);
        inputStream.close();
        outStream.close();
    }

}
