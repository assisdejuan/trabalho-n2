package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Livro;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;

import java.util.*;

public class LivroRepositorioMemoria implements PortaLivroRepositorio {

    private final Map<Long, Livro> db = new LinkedHashMap<>();

    @Override
    public void salvar(Livro livro) {
        db.put(livro.getId(), livro);
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Livro> listarTodos() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void remover(Long id) {
        db.remove(id);
    }
}
