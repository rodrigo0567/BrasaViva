package model;

public class Estoque {
    private Long id;
    private Produto produto;
    private int quantidadeDisponivel;

    public Estoque(Long idProduto, Produto produto, int quantidadeDisponivel) {
        this.id = idProduto;
        this.produto = produto;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public Estoque(Produto produto, int quantidadeDisponivel) {
        this.produto = produto;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public void incrementarQuantidade(int quantidade) {
        this.quantidadeDisponivel += quantidade;
    }

    public boolean decrementarQuantidade(int quantidade) {
        if (this.quantidadeDisponivel >= quantidade) {
            this.quantidadeDisponivel -= quantidade;
            return true;
        } else {
            return false; // Quantidade insuficiente
        }
    }

}
