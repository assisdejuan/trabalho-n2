package biblioteca.aplicacao;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.SituacaoEmprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.EmprestimoRepositorio;
import biblioteca.infraestrutura.LivroRepositorio;
import biblioteca.infraestrutura.UsuarioRepositorio;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmprestimoServico {

    private final LivroRepositorio livroRepo;
    private final UsuarioRepositorio usuarioRepo;
    private final EmprestimoRepositorio emprestimoRepo;
    private long proximoId = 1;

    public EmprestimoServico(LivroRepositorio livroRepo,
                              UsuarioRepositorio usuarioRepo,
                              EmprestimoRepositorio emprestimoRepo) {
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
        this.emprestimoRepo = emprestimoRepo;
    }

    public Emprestimo realizarEmprestimo(Long usuarioId, Long livroId) {
        Usuario usuario = usuarioRepo.buscarPorId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        if (!usuario.podeEmprestar()) {
            throw new IllegalStateException("Usuário suspenso não pode realizar empréstimos: " + usuario.getNome());
        }

        Livro livro = livroRepo.buscarPorId(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado: " + livroId));

        livro.realizarEmprestimo();
        livroRepo.salvar(livro);

        Emprestimo emprestimo = new Emprestimo(proximoId++, livro, usuario, LocalDate.now());
        emprestimoRepo.salvar(emprestimo);
        return emprestimo;
    }

    public void registrarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepo.buscarPorId(emprestimoId)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado: " + emprestimoId));

        emprestimo.finalizar();
        emprestimo.getLivro().registrarDevolucao();
        livroRepo.salvar(emprestimo.getLivro());
        emprestimoRepo.salvar(emprestimo);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepo.listarTodos().stream()
                .filter(e -> e.getSituacao() == SituacaoEmprestimo.ATIVO)
                .collect(Collectors.toList());
    }

    public List<Emprestimo> verificarAtrasos() {
        return emprestimoRepo.listarTodos().stream()
                .filter(Emprestimo::estaAtrasado)
                .collect(Collectors.toList());
    }
}
