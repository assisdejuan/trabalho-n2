package biblioteca.apresentacao;

import biblioteca.aplicacao.EmprestimoServico;
import biblioteca.aplicacao.LivroServico;
import biblioteca.aplicacao.UsuarioServico;
import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Livro;
import biblioteca.dominio.Usuario;
import biblioteca.infraestrutura.EmprestimoRepositorio;
import biblioteca.infraestrutura.LivroRepositorio;
import biblioteca.infraestrutura.UsuarioRepositorio;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        LivroRepositorio livroRepo = new LivroRepositorio();
        UsuarioRepositorio usuarioRepo = new UsuarioRepositorio();
        EmprestimoRepositorio emprestimoRepo = new EmprestimoRepositorio();

        LivroServico livroServico = new LivroServico(livroRepo);
        UsuarioServico usuarioServico = new UsuarioServico(usuarioRepo);
        EmprestimoServico emprestimoServico = new EmprestimoServico(livroRepo, usuarioRepo, emprestimoRepo);

        System.out.println("=== Sistema de Gerenciamento de Biblioteca — Etapa 1 ===\n");

        // Cadastro de livros
        Livro livro1 = livroServico.cadastrar("Clean Code", "Robert C. Martin", "978-0-13-235088-4", 3);
        Livro livro2 = livroServico.cadastrar("Domain-Driven Design", "Eric Evans", "978-0-32-112521-7", 1);

        System.out.println("Livros cadastrados:");
        livroServico.listarTodos().forEach(l -> System.out.println("  " + l));

        // Cadastro de usuários
        Usuario u1 = usuarioServico.cadastrar("Ana Silva", "ana@email.com");
        Usuario u2 = usuarioServico.cadastrar("Bruno Lima", "bruno@email.com");

        System.out.println("\nUsuários cadastrados:");
        usuarioServico.listarTodos().forEach(u -> System.out.println("  " + u));

        // Realização de empréstimo
        System.out.println("\n-- Realizando empréstimo --");
        Emprestimo emp1 = emprestimoServico.realizarEmprestimo(u1.getId(), livro1.getId());
        System.out.printf("Empréstimo #%d: %s → \"%s\"%n", emp1.getId(), u1.getNome(), livro1.getTitulo());
        System.out.println("Devolução prevista: " + emp1.getDataPrevistaDevolucao());

        // Devolução
        System.out.println("\n-- Registrando devolução --");
        emprestimoServico.registrarDevolucao(emp1.getId());
        System.out.printf("Empréstimo #%d devolvido. Situação: %s%n", emp1.getId(), emp1.getSituacao());

        // Segundo empréstimo e listagem de ativos
        Emprestimo emp2 = emprestimoServico.realizarEmprestimo(u2.getId(), livro2.getId());

        List<Emprestimo> ativos = emprestimoServico.listarEmprestimosAtivos();
        System.out.println("\nEmpréstimos ativos: " + ativos.size());
        ativos.forEach(e -> System.out.printf("  #%d — %s → \"%s\"%n",
                e.getId(), e.getUsuario().getNome(), e.getLivro().getTitulo()));

        List<Emprestimo> atrasados = emprestimoServico.verificarAtrasos();
        System.out.println("Empréstimos em atraso: " + atrasados.size());

        System.out.println("\n=== Fim da demonstração ===");
    }
}
