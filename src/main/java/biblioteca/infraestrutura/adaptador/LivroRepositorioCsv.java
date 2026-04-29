package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class LivroRepositorioCsv implements PortaLivroRepositorio {

    private static final String CABECALHO = "id;titulo;autor;isbn;quantidadeDisponivel";
    private final Path arquivo;

    public LivroRepositorioCsv(String caminhoArquivo) {
        this.arquivo = Path.of(caminhoArquivo);
        if (!Files.exists(arquivo)) {
            try {
                Files.writeString(arquivo, CABECALHO + System.lineSeparator());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao criar arquivo CSV: " + arquivo, e);
            }
        }
    }

    @Override
    public void salvar(Livro livro) {
        Map<Long, Livro> todos = carregarTodos();
        todos.put(livro.getId(), livro);
        persistir(todos.values());
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(carregarTodos().get(id));
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(carregarTodos().values());
    }

    @Override
    public void remover(Long id) {
        Map<Long, Livro> todos = carregarTodos();
        todos.remove(id);
        persistir(todos.values());
    }

    private Map<Long, Livro> carregarTodos() {
        Map<Long, Livro> mapa = new LinkedHashMap<>();
        try {
            List<String> linhas = Files.readAllLines(arquivo);
            for (int i = 1; i < linhas.size(); i++) {
                String linha = linhas.get(i).trim();
                if (linha.isEmpty()) continue;
                String[] c = linha.split(";", -1);
                mapa.put(Long.parseLong(c[0]),
                        new Livro(Long.parseLong(c[0]), c[1], c[2], c[3], Integer.parseInt(c[4])));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler arquivo CSV: " + arquivo, e);
        }
        return mapa;
    }

    private void persistir(Collection<Livro> livros) {
        StringBuilder sb = new StringBuilder(CABECALHO).append(System.lineSeparator());
        for (Livro l : livros) {
            sb.append(l.getId()).append(';')
              .append(l.getTitulo()).append(';')
              .append(l.getAutor()).append(';')
              .append(l.getIsbn()).append(';')
              .append(l.getQuantidadeDisponivel())
              .append(System.lineSeparator());
        }
        try {
            Files.writeString(arquivo, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever arquivo CSV: " + arquivo, e);
        }
    }
}
