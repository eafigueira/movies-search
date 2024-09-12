package br.com.magalu.movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class Main {
    private static final Map<String, Set<Path>> indiceInvertido = new ConcurrentHashMap<>();

    private final Service service;

    public Main(Service service) {
        this.service = service;
    }

    public void processarArquivo(Path arquivo) {
        try (BufferedReader reader = Files.newBufferedReader(arquivo)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] palavras = linha.toLowerCase().split("\\W+");
                for (String palavra : palavras) {
                    if (!palavra.isEmpty()) {
                        indiceInvertido
                                .computeIfAbsent(palavra, k -> new HashSet<>())
                                .add(arquivo);
                    }
                }
            }
        } catch (IOException e) {
            System.err.printf("Erro ao processar o arquivo %s: %s%n", arquivo, e.getMessage());
        }
    }

    public void construirIndice(String diretorio) throws IOException, InterruptedException {
        try (ExecutorService executor = service.criarExecutor()) {
            List<Path> arquivos = service.listarArquivos(diretorio);
            executor.invokeAll(arquivos.stream()
                    .map(arquivo -> (Callable<Void>) () -> {
                        processarArquivo(arquivo);
                        return null;
                    })
                    .toList());
        }
    }

    public Set<Path> buscaTermo(String termo) {
        String[] palavrasBusca = termo.toLowerCase().split("\\W+");
        List<Set<Path>> arquivosPorPalavra = Arrays.stream(palavrasBusca)
                .map(indiceInvertido::get)
                .filter(Objects::nonNull)
                .toList();

        if (arquivosPorPalavra.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Path> arquivosComTermo = new HashSet<>(arquivosPorPalavra.getFirst());
        arquivosPorPalavra.forEach(arquivosComTermo::retainAll);

        return arquivosComTermo;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        if (args.length < 2) {
            System.out.println("Uso: java -jar movies-search.jar <diretorio> <palavra>");
            return;
        }

        Service service = new Service();
        Main search = new Main(service);

        String diretorio = args[0];
        String termo = args[1];

        if (!Files.exists(Path.of(diretorio))) {
            System.out.printf("O diretorio [%s] não existe\n", diretorio);
            return;
        }

        long startTime = System.currentTimeMillis();

        search.construirIndice(diretorio);
        Set<Path> arquivosComTermo = search.buscaTermo(termo);

        if (arquivosComTermo.isEmpty()) {
            System.out.printf("Nenhum arquivo contém o termo: %s%n", termo);
        } else {
            System.out.printf("Foram encontradas %d ocorrências para o termo '%s'%n", arquivosComTermo.size(), termo);
            arquivosComTermo.stream()
                    .sorted()
                    .forEach(System.out::println);
        }

        System.out.printf("Tempo total: %d ms%n", System.currentTimeMillis() - startTime);
    }
}
