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
        System.out.println("\n--- Produtos Disponíveis ---\n");

        for (Produto produto : produtos) {
            Estoque estoque = crud.buscarEstoquePorProduto(produto.getId());

            if (estoque != null) {
                System.out.println("ID: " + produto.getId() +
                        "\nNome: " + produto.getNome() +
                        "\nPreço: R$ " + String.format("%.2f", produto.getPreco()) +
                        "\nQuantidade disponível: " + estoque.getQuantidadeDisponivel() + "\n"
                );
                System.out.print("--------------------------\n");
            }
        }
    }

    public static String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatarTelefone(String telefone) {
        return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }

//    public void gerarComanda(Cliente cliente) {
//        System.out.println("\n--- Comanda ---\n");
//        System.out.println("Cliente Nome: " + cliente.getNome());
//        System.out.println("Cliente CPF: " + cliente.getCpf());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date dataVenda;
//        System.out.println("Data: " + sdf.format(dataVenda));
//        System.out.println("Produtos: ");
//
//        for (ProdutoVenda pv : produtos) {
//            Produto produto = pv.getProduto();
//            System.out.println(produto.getNome() + " - Quantidade: " + pv.getQuantidade() + " - Preço unitário: R$ " + produto.getPreco());
//        }
//
//        double valorTotal = calcularValorTotal();
//        System.out.println("\nValor total: R$ " + valorTotal);
//        System.out.println("===================");
//    }
}
