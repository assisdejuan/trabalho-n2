package biblioteca.dominio;

public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private SituacaoUsuario situacao;

    public Usuario(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.situacao = SituacaoUsuario.ATIVO;
    }

    public boolean podeEmprestar() {
        return SituacaoUsuario.ATIVO.equals(situacao);
    }

    public void suspender() {
        situacao = SituacaoUsuario.SUSPENSO;
    }

    public void reativar() {
        situacao = SituacaoUsuario.ATIVO;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public SituacaoUsuario getSituacao() { return situacao; }
    public void setSituacao(SituacaoUsuario situacao) { this.situacao = situacao; }

    @Override
    public String toString() {
        return nome + " <" + email + "> [" + situacao + "]";
    }
}
