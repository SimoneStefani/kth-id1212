package client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtility {
    private static final String root = "./localFiles";
    private static Path workingDir = Paths.get(root);

    public static void writeFile(String filename, byte[] bytes) throws IOException {
        Path file = workingDir.resolve(Paths.get(filename));
        Files.write(file,bytes);
    }

    public static boolean fileExists(String filename) {
        Path file = workingDir.resolve(Paths.get(filename));
        return Files.exists(file);
    }

    public static byte[] readFile(String filename) throws IOException {
        Path file = workingDir.resolve(Paths.get(filename));
        if (!Files.exists(file)) {
            throw new FileNotFoundException("Could not find " + file);
        }
        return Files.readAllBytes(file);
    }
}
