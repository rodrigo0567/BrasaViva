package model;

public class Produto {
    private Long id;
    private double preco;
    private String nome;

    public Produto() {}

    public Produto(Long id, String nome, double preco) {
        this.id = id;
        this.preco = preco;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // Método para calcular o valor total de um produto baseado na quantidade
    public double calcularValorTotal(int quantidade) {
        return this.preco * quantidade;
    }

    @Override
    public String toString() {
        return String.format("Produto{id=%d, nome='%s', preco=%.2f}", id, nome, preco);
    }

}
