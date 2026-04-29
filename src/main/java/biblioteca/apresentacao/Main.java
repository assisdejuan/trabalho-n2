package biblioteca.apresentacao;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.porta.entrada.PortaEmprestimo;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaNotificacao;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.infraestrutura.adaptador.*;
import biblioteca.servico.EmprestimoServico;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Sistema de Gerenciamento de Biblioteca — Etapa 2 ===\n");

        demonstrarComMemoria();
        System.out.println("\n" + "=".repeat(55) + "\n");
        demonstrarComCsv();
    }

    private static void demonstrarComMemoria() {
        System.out.println("--- Adaptador: repositório em memória ---\n");

        PortaLivroRepositorio livroRepo = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepo = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepo = new EmprestimoRepositorioMemoria();
        PortaNotificacao notif = new NotificacaoConsole();

        PortaEmprestimo servico = new EmprestimoServico(livroRepo, usuarioRepo, emprestimoRepo, notif);

        executarFluxo(livroRepo, usuarioRepo, servico);
    }

    private static void demonstrarComCsv() {
        System.out.println("--- Adaptador: repositório CSV (livros.csv) ---\n");

        PortaLivroRepositorio livroRepo = new LivroRepositorioCsv("livros.csv");
        PortaUsuarioRepositorio usuarioRepo = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepo = new EmprestimoRepositorioMemoria();
        PortaNotificacao notif = new NotificacaoConsole();

        // EmprestimoServico não foi alterado — apenas o adaptador de livro mudou
        PortaEmprestimo servico = new EmprestimoServico(livroRepo, usuarioRepo, emprestimoRepo, notif);

        executarFluxo(livroRepo, usuarioRepo, servico);
    }

    private static void executarFluxo(PortaLivroRepositorio livroRepo,
                                       PortaUsuarioRepositorio usuarioRepo,
                                       PortaEmprestimo servico) {
        Livro livro = new biblioteca.dominio.Livro(1L, "Refactoring", "Martin Fowler", "978-0-13-468599-1", 2);
        livroRepo.salvar(livro);

        Usuario usuario = new Usuario(1L, "Carlos Souza", "carlos@email.com");
        usuarioRepo.salvar(usuario);

        System.out.println("Livros: " + livroRepo.listarTodos());

        Emprestimo emp = servico.realizarEmprestimo(1L, 1L);
        System.out.printf("Empréstimo #%d realizado. Devolução prevista: %s%n",
                emp.getId(), emp.getDataPrevistaDevolucao());

        servico.registrarDevolucao(emp.getId());
        System.out.printf("Empréstimo #%d devolvido. Situação: %s%n", emp.getId(), emp.getSituacao());

        List<Emprestimo> ativos = servico.listarEmprestimosAtivos();
        System.out.println("Empréstimos ativos após devolução: " + ativos.size());
    }
}
