package biblioteca.dominio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    private static final int PRAZO_DIAS = 14;

    private Long id;
    private Livro livro;
    private Usuario usuario;
    private LocalDate dataRetirada;
    private LocalDate dataPrevistaDevolucao;
    private SituacaoEmprestimo situacao;

    public Emprestimo(Long id, Livro livro, Usuario usuario, LocalDate dataRetirada) {
        this.id = id;
        this.livro = livro;
        this.usuario = usuario;
        this.dataRetirada = dataRetirada;
        this.dataPrevistaDevolucao = dataRetirada.plusDays(PRAZO_DIAS);
        this.situacao = SituacaoEmprestimo.ATIVO;
    }

    public boolean estaAtrasado() {
        return situacao == SituacaoEmprestimo.ATIVO
                && ChronoUnit.DAYS.between(dataPrevistaDevolucao, LocalDate.now()) > 0;
    }

    public void finalizar() {
        situacao = estaAtrasado() ? SituacaoEmprestimo.ATRASADO : SituacaoEmprestimo.DEVOLVIDO;
    }

    public Long getId() { return id; }
    public Livro getLivro() { return livro; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getDataRetirada() { return dataRetirada; }
    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public SituacaoEmprestimo getSituacao() { return situacao; }
    public void setSituacao(SituacaoEmprestimo situacao) { this.situacao = situacao; }
}
