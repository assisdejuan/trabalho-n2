package biblioteca.infraestrutura.adaptador;

import biblioteca.dominio.Emprestimo;
import biblioteca.dominio.Usuario;
import biblioteca.dominio.porta.saida.PortaNotificacao;

public class NotificacaoConsole implements PortaNotificacao {

    @Override
    public void notificarAtraso(Usuario usuario, Emprestimo emprestimo) {
        System.out.printf("[NOTIFICAÇÃO] %s, o empréstimo do livro \"%s\" venceu em %s e está em atraso!%n",
                usuario.getNome(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getDataPrevistaDevolucao());
    }
}
