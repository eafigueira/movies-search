import br.com.magalu.movies.Main;
import br.com.magalu.movies.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ThreadsTest {

    private static final String DIRETORIO_TESTE = "src/test/resources";
    private ExecutorService mockExecutor;
    private Main main;
    private Service service;

    @BeforeEach
    void setUp() {
        mockExecutor = Mockito.mock(ExecutorService.class);
        service = Mockito.mock(Service.class);
        main = Mockito.spy(new Main(service));

    }

    @Test
    void testNumeroDeTarefasCriadas() throws Exception {
        Path file1 = Paths.get(DIRETORIO_TESTE + "/file1.txt");
        Path file2 = Paths.get(DIRETORIO_TESTE + "/file2.txt");
        Path file3 = Paths.get(DIRETORIO_TESTE + "/file3.txt");
        List<Path> arquivos = List.of(file1, file2, file3);

        when(service.criarExecutor()).thenReturn(mockExecutor);
        when(service.listarArquivos(anyString())).thenReturn(arquivos);

        doNothing().when(main).processarArquivo(any(Path.class));

        ArgumentCaptor<List<Callable<Void>>> taskCaptor = ArgumentCaptor.forClass(List.class);
        main.construirIndice(DIRETORIO_TESTE);
        verify(mockExecutor, times(1)).invokeAll(taskCaptor.capture());

        for (Callable<Void> task : taskCaptor.getValue()) {
            task.call();
        }
        assertEquals(3, taskCaptor.getValue().size());
        verify(main, times(3)).processarArquivo(any(Path.class));
    }
}
