package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;

import java.util.*;

public class EmprestimoRepositorioMemoria implements PortaEmprestimoRepositorio {

    private final Map<Long, Emprestimo> db = new LinkedHashMap<>();

    @Override
    public void salvar(Emprestimo emprestimo) {
        db.put(emprestimo.getId(), emprestimo);
    }

    @Override
    public Optional<Emprestimo> buscarPorId(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void remover(Long id) {
        db.remove(id);
    }
}
