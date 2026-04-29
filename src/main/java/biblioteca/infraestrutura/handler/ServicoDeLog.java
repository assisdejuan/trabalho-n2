package biblioteca.infraestrutura.handler;

import biblioteca.dominio.evento.DevolucaoRegistradaEvento;
import biblioteca.dominio.evento.EmprestimoRealizadoEvento;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServicoDeLog {

    private static final String ARQUIVO = "biblioteca.log";
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void onEmprestimoRealizado(EmprestimoRealizadoEvento evento) {
        registrar("EMPRESTIMO_REALIZADO | id=" + evento.emprestimoId()
                + " | usuario=" + evento.usuarioId()
                + " | livro=" + evento.livroId()
                + " | retirada=" + evento.dataRetirada());
    }

    public void onDevolucaoRegistrada(DevolucaoRegistradaEvento evento) {
        registrar("DEVOLUCAO_REGISTRADA | id=" + evento.emprestimoId()
                + " | data=" + evento.dataDevolucao()
                + " | atraso=" + evento.comAtraso());
    }

    private void registrar(String mensagem) {
        String linha = "[" + LocalDateTime.now().format(FORMATO) + "] " + mensagem + System.lineSeparator();
        try {
            Files.writeString(Path.of(ARQUIVO), linha, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Erro ao gravar log: " + e.getMessage());
        }
    }
}
