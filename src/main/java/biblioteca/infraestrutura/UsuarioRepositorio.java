package biblioteca.infraestrutura;

import biblioteca.dominio.Usuario;
import java.util.*;

public class UsuarioRepositorio {

    private final Map<Long, Usuario> db = new LinkedHashMap<>();

    public void salvar(Usuario usuario) {
        db.put(usuario.getId(), usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(db.values());
    }

    public void remover(Long id) {
        db.remove(id);
    }
}
