package view;

import java.sql.SQLException;
import java.util.List;
import dao.BrasaVivaCRUD;
import model.Cliente;
import model.Estoque;
import model.Produto;
import model.Venda;
import utils.CardapioUtil;

import java.util.Scanner;

public class AreaGerente {

    private Scanner sc = new Scanner(System.in);
    private BrasaVivaCRUD crud = new BrasaVivaCRUD();
    private CardapioUtil cardapioUtil = new CardapioUtil(crud);

    public String areaDoGerente() {
        while(true) {
            System.out.println("\n=*=*=* Área do Gerente =*=*=*\n");
            System.out.println("1. Alterar Informações do Produto");
            System.out.println("2. Alterar Quantidade em Estoque");
            System.out.println("3. Gerar Relatórios");
            System.out.println("4. Visualizar Cardápio");
            System.out.println("5. Sair da Área do Gerente");
            System.out.print("\nSelecione uma opção: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    alterarProduto();
                    break;
                case 2:
                    alterarEstoque();
                    break;
                case 3:
                    gerarRelatorio();
                    break;
                case 4:
                    cardapioUtil.visualizarCardapio();
                case 5:
                    return "Saindo...";
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void alterarProduto() {
        System.out.print("\nDigite o ID do produto a ser alterado: ");
        Long idProduto = sc.nextLong();
        sc.nextLine();

        try {
            Produto produto = crud.buscarProdutoPorId(idProduto);
            if (produto != null) {
                System.out.println("Produto encontrado: " + produto.getNome());
                System.out.print("Novo nome do produto: ");
                String novoNome = sc.nextLine();
                System.out.print("Novo preço do produto (R$): ");
                double novoPreco = sc.nextDouble();
                sc.nextLine();

                produto.setNome(novoNome);
                produto.setPreco(novoPreco);

                crud.atualizarProduto(produto);
                System.out.println("Produto atualizado com sucesso!");
            }
        } catch (SQLException e) {
                System.out.println("Produto não encontrado.");
        }

    }

    private void alterarEstoque() {
        System.out.print("\nDigite o ID do produto para alterar o estoque: ");
        Long idProduto = sc.nextLong();
        sc.nextLine();

        Estoque estoque = crud.buscarEstoquePorProduto(idProduto);
        if (estoque != null) {
            Produto produto = estoque.getProduto();
            if(produto != null) {
                System.out.println("Produto encontrado: " + produto.getNome());
            } else {
                System.out.println("Produto associado ao estoque não encontrado.");
            }

            System.out.print("Nova quantidade disponível: ");
            int novaQuantidade = sc.nextInt();
            sc.nextLine();

            if (novaQuantidade >= 0) {
                estoque.setQuantidadeDisponivel(novaQuantidade);
                crud.atualizarEstoque(estoque);
                System.out.println("Estoque atualizado com sucesso!");
            } else {
                System.out.println("Erro: A quantidade disponível deve ser maior ou igual a zero.");
            }
        } else {
            System.out.println("Produto não encontrado no estoque.");
        }
    }

    private void gerarRelatorio() {
        System.out.println("\n=*=*=* Relatórios =*=*=*\n");
        System.out.println("1. Gerar Relatório de Vendas");
        System.out.println("2. Gerar Relatório de Estoque");
        System.out.println("3. Gerar Relatório de Clientes");
        System.out.print("\nSelecione uma opção: ");
        int op = sc.nextInt();
        sc.nextLine();

        switch (op) {
            case 1:
                gerarRelatorioVendas();
                break;
            case 2:
                gerarRelatorioEstoque();
                break;
            case 3:
                gerarRelatorioClientes();
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private void gerarRelatorioVendas() {
        try {
            List<Venda> vendas = crud.listarTodasVendas();
            double valorTotal = 0;
            System.out.println("\n--- Relatório de Vendas ---");
            System.out.println("Quantidade de Vendas: " + vendas.size());
            for (Venda venda : vendas) {
                valorTotal += venda.valorTotal();
            }
            System.out.println("Valor total das vendas: R$ " + String.format("%.2f", valorTotal));
        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas: " + e.getMessage());
        }
    }

    private void gerarRelatorioEstoque() {
        List<Estoque> estoques = crud.listarTodosEstoques();
        System.out.println("\n--- Relatório de Estoque ---\n");

        for (Estoque estoque : estoques) {
            System.out.println("ID: " + estoque.getProduto().getId() +
                    "\nProduto: " + estoque.getProduto().getNome() +
                    "\nQuantidade disponível: " + estoque.getQuantidadeDisponivel() +
                    "\nValor Total em Estoque do Produto: R$ " + String.format("%.2f", estoque.getProduto().getPreco() * estoque.getQuantidadeDisponivel())
            );
            System.out.println("--------------------------------");
        }

        System.out.println("\nQuantidade Total de produtos em estoque: " + estoques.size());
    }

    private void gerarRelatorioClientes() {
        List<Cliente> clientes = crud.listarTodosClientes();
        System.out.println("\n--- Relatório de Clientes ---");

        for (Cliente cliente : clientes) {
            System.out.println("ID: " + cliente.getId() +
                    "\nNome: " + cliente.getNome() +
                    "\nCPF: " + cliente.getCpf() +
                    "\nEmail: " + cliente.getEmail() +
                    "\nTelefone: " + cliente.getTelefone()
            );
            System.out.println("--------------------------------");
        }
        System.out.println("\nQuantidade de Clientes Cadastrados: " + clientes.size());
    }
}
