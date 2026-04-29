package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Usuario;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;

import java.util.*;

public class UsuarioRepositorioMemoria implements PortaUsuarioRepositorio {

    private final Map<Long, Usuario> db = new LinkedHashMap<>();

    @Override
    public void salvar(Usuario usuario) {
        db.put(usuario.getId(), usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void remover(Long id) {
        db.remove(id);
    }
}
