package biblioteca.infraestrutura;

import biblioteca.dominio.Livro;
import java.util.*;

public class LivroRepositorio {

    private final Map<Long, Livro> db = new LinkedHashMap<>();

    public void salvar(Livro livro) {
        db.put(livro.getId(), livro);
    }

    public Optional<Livro> buscarPorId(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    public List<Livro> listarTodos() {
        return new ArrayList<>(db.values());
    }

    public void remover(Long id) {
        db.remove(id);
    }
}
