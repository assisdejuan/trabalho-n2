package biblioteca.aplicacao;

import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.UsuarioRepositorio;
import java.util.List;

public class UsuarioServico {

    private final UsuarioRepositorio repositorio;
    private long proximoId = 1;

    public UsuarioServico(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Usuario cadastrar(String nome, String email) {
        Usuario usuario = new Usuario(proximoId++, nome, email);
        repositorio.salvar(usuario);
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return repositorio.listarTodos();
    }
}
