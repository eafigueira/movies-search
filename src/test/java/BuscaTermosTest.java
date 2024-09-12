import br.com.magalu.movies.Main;
import br.com.magalu.movies.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BuscaTermosTest {
    private static final String DIRETORIO_TESTE = "src/test/resources";
    private Main app;
    private Service service;
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        service = new Service();
        app = new Main(service);
        app.construirIndice(DIRETORIO_TESTE);
    }

    @Test
    void testBuscaTermoUnico() {
        Set<Path> resultado = app.buscaTermo("walt");
        assertFalse(resultado.isEmpty(), "A busca por 'walt' não deveria ser vazia.");
        assertEquals(1, resultado.size(), "Deveria retornar um arquivo.");
        assertTrue(resultado.iterator().next().toString().contains("movies.txt"));
    }
    @Test
    void testBuscaTermoComposto() {
        Set<Path> resultado = app.buscaTermo("walt disney");
        assertFalse(resultado.isEmpty(), "A busca por 'walt disney' não deveria ser vazia.");
        assertEquals(1, resultado.size(), "Deveria retornar um arquivo.");
    }
    @Test
    void testBuscaSemResultados() {
        Set<Path> resultado = app.buscaTermo("palavraInexistente");
        assertTrue(resultado.isEmpty(), "A busca por uma palavra inexistente deveria ser vazia.");
    }
    @Test
    void testBuscaPalavraComumEmVariosArquivos() {
        Set<Path> resultado = app.buscaTermo("Disney");
        assertFalse(resultado.isEmpty(), "A busca por 'Disney' deveria retornar resultados.");
        assertTrue(resultado.size() > 1, "Deveria retornar mais de um arquivo.");
    }
}
