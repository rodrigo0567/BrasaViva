package model;

public class Estoque {
    private Long idProduto;
    private int quantidadeDisponivel;

    public Estoque(Long idProduto, int quantidadeDisponivel) {
        this.idProduto = idProduto;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
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

    @Override
    public String toString() {
        return String.format("Estoque{idProduto=%d, quantidadeDisponivel=%d}", idProduto, quantidadeDisponivel);
    }

}
