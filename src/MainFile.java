import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        new MainFile().logDirWithFiles("./src", "");
    }

    private void logDirWithFiles(String filePath, String logPrefix) {
        File f = new File(filePath);

        if (f.isDirectory()) {
            String[] list = f.list();
            System.out.println(logPrefix + f.getName());
            if (list != null) {
                for (String name : list) {
                    logDirWithFiles(filePath + "/" + name, "  " + logPrefix);
                }
            }
            return;
        }
        System.out.println(logPrefix + f.getName());
    }
}
