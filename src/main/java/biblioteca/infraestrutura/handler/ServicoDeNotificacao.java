package biblioteca.infraestrutura.handler;

import biblioteca.dominio.evento.EmprestimoRealizadoEvento;

import java.time.LocalDate;

public class ServicoDeNotificacao {

    public void onEmprestimoRealizado(EmprestimoRealizadoEvento evento) {
        LocalDate prevista = evento.dataRetirada().plusDays(14);
        System.out.printf("[NOTIFICAÇÃO] Empréstimo #%d confirmado. Devolva o livro até %s.%n",
                evento.emprestimoId(), prevista);
    }
}
