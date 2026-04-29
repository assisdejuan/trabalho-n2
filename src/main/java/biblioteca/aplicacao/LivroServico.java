package biblioteca.aplicacao;

import biblioteca.dominio.Livro;
import biblioteca.infraestrutura.LivroRepositorio;
import java.util.List;

public class LivroServico {

    private final LivroRepositorio repositorio;
    private long proximoId = 1;

    public LivroServico(LivroRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Livro cadastrar(String titulo, String autor, String isbn, int quantidade) {
        Livro livro = new Livro(proximoId++, titulo, autor, isbn, quantidade);
        repositorio.salvar(livro);
        return livro;
    }

    public List<Livro> listarTodos() {
        return repositorio.listarTodos();
    }
}
