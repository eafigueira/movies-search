package br.com.magalu.movies;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class Service {

    public ExecutorService criarExecutor() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public List<Path> listarArquivos(String diretorio) throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(diretorio))) {
            return stream.filter(Files::isRegularFile).toList();
        }
    }
}
