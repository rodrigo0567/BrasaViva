package utils;

import dao.BrasaVivaCRUD;
import model.Estoque;
import model.Produto;

import java.util.List;

public class Utilitarios {
    private BrasaVivaCRUD crud;

    public Utilitarios(BrasaVivaCRUD crud) {
        this.crud = crud;
    }

    public void visualizarCardapio() {
        List<Produto> produtos = crud.listarTodosProdutos();
        System.out.println("\n--- Produtos Disponíveis ---");

        for (Produto produto : produtos) {
            Estoque estoque = crud.buscarEstoquePorProduto(produto.getId());

            if (estoque != null) {
                System.out.println("\nID: " + produto.getId() +
                        "\nNome: " + produto.getNome() +
                        "\nDescrição: " + produto.getDescricao() +
                        "\nPreço: R$ " + String.format("%.2f", produto.getPreco()) +
                        "\nCategoria: " + produto.getCategoria() +
                        "\nQuantidade disponível: " + estoque.getQuantidadeDisponivel() + "\n"
                );
                System.out.print("----------------------------");
            }
        }
    }

    public static String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatarTelefone(String telefone) {
        return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }
}
