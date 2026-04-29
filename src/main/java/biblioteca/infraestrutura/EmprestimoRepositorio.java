package biblioteca.infraestrutura;

import biblioteca.dominio.Emprestimo;
import java.util.*;

public class EmprestimoRepositorio {

    private final Map<Long, Emprestimo> db = new LinkedHashMap<>();

    public void salvar(Emprestimo emprestimo) {
        db.put(emprestimo.getId(), emprestimo);
    }

    public Optional<Emprestimo> buscarPorId(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(db.values());
    }

    public void remover(Long id) {
        db.remove(id);
    }
}
