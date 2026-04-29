package biblioteca.dominio;

public class Livro {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeDisponivel;

    public Livro(Long id, String titulo, String autor, String isbn, int quantidadeDisponivel) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void realizarEmprestimo() {
        if (quantidadeDisponivel == 0) {
            throw new IllegalStateException("Livro indisponível para empréstimo: " + titulo);
        }
        quantidadeDisponivel--;
    }

    public void registrarDevolucao() {
        quantidadeDisponivel++;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public int getQuantidadeDisponivel() { return quantidadeDisponivel; }
    public void setQuantidadeDisponivel(int v) { this.quantidadeDisponivel = v; }

    @Override
    public String toString() {
        return titulo + " (" + autor + ") — ISBN: " + isbn + " | Disponíveis: " + quantidadeDisponivel;
    }
}
