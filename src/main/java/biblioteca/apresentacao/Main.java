package biblioteca.apresentacao;

import biblioteca.dominio.Livro;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;
import biblioteca.dominio.porta.entrada.PortaEmprestimo;
import biblioteca.dominio.porta.saida.PortaEmprestimoRepositorio;
import biblioteca.dominio.porta.saida.PortaLivroRepositorio;
import biblioteca.dominio.porta.saida.PortaNotificacao;
import biblioteca.dominio.porta.saida.PortaUsuarioRepositorio;
import biblioteca.infraestrutura.adaptador.*;
import biblioteca.infraestrutura.evento.EventBus;
import biblioteca.infraestrutura.handler.ServicoDeLog;
import biblioteca.infraestrutura.handler.ServicoDeNotificacao;
import biblioteca.servico.EmprestimoServico;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Sistema de Gerenciamento de Biblioteca — Etapa 3 ===\n");

        // Barramentos de eventos
        EventBus<EmprestimoRealizadoEvento> busEmprestimo = new EventBus<>();
        EventBus<DevolucaoRegistradaEvento> busDevolucao = new EventBus<>();

        // Registro dos consumidores — EmprestimoServico não os conhece diretamente
        ServicoDeNotificacao notificador = new ServicoDeNotificacao();
        ServicoDeLog logger = new ServicoDeLog();

        busEmprestimo.assinar(notificador::onEmprestimoRealizado);
        busEmprestimo.assinar(logger::onEmprestimoRealizado);
        busDevolucao.assinar(logger::onDevolucaoRegistrada);

        // Composição com adaptadores
        PortaLivroRepositorio livroRepo = new LivroRepositorioMemoria();
        PortaUsuarioRepositorio usuarioRepo = new UsuarioRepositorioMemoria();
        PortaEmprestimoRepositorio emprestimoRepo = new EmprestimoRepositorioMemoria();
        PortaNotificacao notif = new NotificacaoConsole();

        PortaEmprestimo servico = new EmprestimoServico(
                livroRepo, usuarioRepo, emprestimoRepo, notif, busEmprestimo, busDevolucao);

        // Dados de demonstração
        Livro livro = new Livro(1L, "Refactoring", "Martin Fowler", "978-0-13-468599-1", 2);
        livroRepo.salvar(livro);

        Usuario usuario = new Usuario(1L, "Carlos Souza", "carlos@email.com");
        usuarioRepo.salvar(usuario);

        System.out.println("-- Realizando empréstimo (deve disparar 2 handlers) --");
        var emp = servico.realizarEmprestimo(1L, 1L);
        System.out.printf("Empréstimo #%d criado.%n%n", emp.getId());

        System.out.println("-- Registrando devolução (deve disparar 1 handler) --");
        servico.registrarDevolucao(emp.getId());
        System.out.printf("Empréstimo #%d finalizado. Situação: %s%n", emp.getId(), emp.getSituacao());

        System.out.println("\nLog gravado em: biblioteca.log");
        System.out.println("\n=== Fim da demonstração ===");
    }
}
