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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    } // utilizado para alterar a quantida disponível da compra do cliente

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

    @Override
    public String toString() {
        return String.format("Estoque{idProduto=%d, quantidadeDisponivel=%d}", id, quantidadeDisponivel);
    }

}
