import br.com.magalu.movies.Main;
import br.com.magalu.movies.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProcessarArquivoTest {

    private Main main;

    @BeforeEach
    void setUp() {
        Service service = Mockito.mock(Service.class);
        main = Mockito.spy(new Main(service));
    }
    @Test
    void testProcessarArquivoVazio() throws IOException {
        Path arquivoVazio = null;
        try {
            Field field = Main.class.getDeclaredField("indiceInvertido");
            field.setAccessible(true);
            Map<String, Set<Path>> indiceInvertido = (Map<String, Set<Path>>) field.get(main);
            indiceInvertido.clear();

            arquivoVazio = TestsHelper.createAndWriteContent("fileVazio", "");
            main.processarArquivo(arquivoVazio);

            assertTrue(indiceInvertido.isEmpty());
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            Assertions.fail();
        } finally {
            Files.deleteIfExists(Objects.requireNonNull(arquivoVazio));
        }
    }

    @Test
    void testProcessarArquivoComPalavrasDuplicadas() throws IOException {
        Path arquivo = null;
        try {
            Field field = Main.class.getDeclaredField("indiceInvertido");
            field.setAccessible(true);

            Map<String, Set<Path>> indiceInvertido = (Map<String, Set<Path>>) field.get(main);
            indiceInvertido.clear();

            arquivo = TestsHelper.createAndWriteContent("fileDuplicado", "java java java");
            main.processarArquivo(arquivo);

            assertTrue(indiceInvertido.containsKey("java"));
            assertEquals(1, indiceInvertido.get("java").size());
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            Assertions.fail();
        } finally {
            Files.deleteIfExists(Objects.requireNonNull(arquivo));
        }
    }

    @Test
    void testProcessarArquivoAdicionaArquivosDiferentesParaMesmaPalavra() throws IOException {
        Path file1 = null;
        Path file2 = null;
        try {
            Field field = Main.class.getDeclaredField("indiceInvertido");
            field.setAccessible(true);

            file1 = TestsHelper.createAndWriteContent("file1", "java forever");
            file2 = TestsHelper.createAndWriteContent("file1", "java");

            main.processarArquivo(file1);
            main.processarArquivo(file2);

            Map<String, Set<Path>> indiceInvertido = (Map<String, Set<Path>>) field.get(main);
            assertTrue(indiceInvertido.containsKey("java"));
            assertEquals(2, indiceInvertido.get("java").size());

        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            Assertions.fail();
        } finally {
            Files.deleteIfExists(Objects.requireNonNull(file1));
            Files.deleteIfExists(Objects.requireNonNull(file2));
        }
    }
}
