import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestsHelper {
    static Path createAndWriteContent(String prefix, String content) throws IOException {
        var tempFile = Files.createTempFile(prefix, ".txt");
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }
}
