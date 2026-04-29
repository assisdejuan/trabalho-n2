package biblioteca.servico;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.porta.entrada.PortaEmprestimo;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaNotificacao;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmprestimoServico implements PortaEmprestimo {

    private final PortaLivroRepositorio livroRepo;
    private final PortaUsuarioRepositorio usuarioRepo;
    private final PortaEmprestimoRepositorio emprestimoRepo;
    private final PortaNotificacao notificacao;
    private long proximoId = 1;

    public EmprestimoServico(PortaLivroRepositorio livroRepo,
                              PortaUsuarioRepositorio usuarioRepo,
                              PortaEmprestimoRepositorio emprestimoRepo,
                              PortaNotificacao notificacao) {
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
        this.emprestimoRepo = emprestimoRepo;
        this.notificacao = notificacao;
    }

    @Override
    public Emprestimo realizarEmprestimo(Long usuarioId, Long livroId) {
        Usuario usuario = usuarioRepo.buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        if (!usuario.podeEmprestar()) {
            throw new IllegalStateException("Usuário suspenso: " + usuario.getNome());
        }

        Livro livro = livroRepo.buscarPorId(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado: " + livroId));

        livro.realizarEmprestimo();
        livroRepo.salvar(livro);

        Emprestimo emprestimo = new Emprestimo(proximoId++, livro, usuario, LocalDate.now());
        emprestimoRepo.salvar(emprestimo);
        return emprestimo;
    }

    @Override
    public void registrarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepo.buscarPorId(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado: " + emprestimoId));

        emprestimo.finalizar();
        emprestimo.getLivro().registrarDevolucao();
        livroRepo.salvar(emprestimo.getLivro());
        emprestimoRepo.salvar(emprestimo);
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepo.listarTodos().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.ATIVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Emprestimo> verificarAtrasos() {
        List<Emprestimo> atrasados = emprestimoRepo.listarTodos().stream()
                .filter(Emprestimo::estaAtrasado)
                .collect(Collectors.toList());

        atrasados.forEach(e -> notificacao.notificarAtraso(e.getUsuario(), e));
        return atrasados;
    }
}
