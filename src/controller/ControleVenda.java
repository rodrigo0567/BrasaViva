package controller;

import dao.BrasaVivaCRUD;
import model.*;
import utils.Utilitarios;
import view.AreaAtendente;

import java.sql.SQLException;
import java.util.Scanner;

public class ControleVenda {
    private final Scanner sc;
    private final Venda venda;
    private BrasaVivaCRUD crud = new BrasaVivaCRUD();
    private Utilitarios cardapioUtil = new Utilitarios(crud);
    AreaAtendente areaAtendente = new AreaAtendente(crud);

    public ControleVenda(Scanner sc, BrasaVivaCRUD crud, Cliente cliente) {
        this.sc = sc;
        this.crud = crud;
        this.venda = new Venda();
        this.venda.setIdCliente(cliente.getId()); // Associação do cliente à venda
    }

    public void processarVenda() {
        while (true) {
            exibirMenu();
            int opVenda = lerOpcao();

            switch (opVenda) {
                case 1:
                    cardapioUtil.visualizarCardapio();
                    break;
                case 2:
                    adicionarProdutoVenda();
                    break;
                case 3:
                    alterarProdutoDaVenda();
                    break;
                case 4:
                    visualizarProdutosSelecionados();
                    break;
                case 5:
                    finalizarVenda();
                    break;
                case 6:
                    cancelarVenda();
                    break;
                case 7:
                    areaAtendente.areaDoAtendente();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n--- Menu de Vendas ---\n");
        System.out.println("1. Visualizar Cardápio");
        System.out.println("2. Adicionar Produto à Venda");
        System.out.println("3. Alterar Produto da Venda");
        System.out.println("4. Visualizar Produtos Selecionados");
        System.out.println("5. Finalizar Venda");
        System.out.println("6. Cancelar Venda");
        System.out.println("7. Voltar Para Área Atendente");
        System.out.print("\nEscolha uma Opção: ");
    }

    private int lerOpcao() {
        int opcao = -1;
        try {
            opcao = sc.nextInt();
            sc.nextLine(); // Limpa o buffer
        } catch (Exception e) {
            System.out.println("Entrada inválida. Digite um número.");
            sc.nextLine(); // Limpa o buffer em caso de erro
        }
        return opcao;
    }

    private void adicionarProdutoVenda() {
        boolean continuarComprando = true;

        while (continuarComprando) {
            Produto produtoEscolhido = selecionarProduto();
            if (produtoEscolhido == null) {
                System.out.println("Produto não encontrado ou quantidade insuficiente.");
                return;
            }

            int quantidade = lerQuantidade();
            if (quantidade <= 0) {
                System.out.println("Quantidade inválida.");
                return;
            }

            Estoque estoqueProduto = crud.buscarEstoquePorProduto(produtoEscolhido.getId());
            if (estoqueProduto == null) {
                System.out.println("Estoque não encontrado para o produto");
                return;
            }

            if (estoqueProduto.getQuantidadeDisponivel() >= quantidade) {
                venda.adicionarProduto(produtoEscolhido, quantidade);
                System.out.println("Produto adicionado à compra com sucesso!");
            } else {
                System.out.println("Quantidade insuficiente no estoque.");
            }

            continuarComprando = desejaContinuarComprando();
        }
    }

    private Produto selecionarProduto() {
        System.out.println("\n=*=*=* Seleção de Compra =*=*=*\n");
        System.out.println("Deseja buscar o produto por:");
        System.out.println("1. ID");
        System.out.println("2. Nome");
        System.out.print("Resposta: ");
        int escolha = sc.nextInt();
        sc.nextLine();

        Produto produto = null;

        try {
            switch (escolha) {
                case 1:
                    System.out.print("\nInsira o ID do Produto: ");
                    long idProduto = sc.nextLong();
                    sc.nextLine(); // Limpa o buffer
                    produto = crud.exibirUmProdutoPorId(idProduto);
                    break;
                case 2:
                    System.out.print("\nInsira o Nome do Produto: ");
                    String nomeProduto = sc.nextLine();
                    produto = crud.exibirUmProdutoPorNome(nomeProduto);
                    break;
                default:
                    System.out.println("Opção inválida. Selecione 1. ID ou 2. Nome");
                    break;
            }

            if (produto != null) {
                System.out.println("Produto selecionado: " + produto.getNome());
                return produto;

            } else {
                System.out.println("Produto não encontrado.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("ID inválido.");
            sc.nextLine(); // Limpa o buffer em caso de erro
            return null;
        }
    }

    private int lerQuantidade() {
        System.out.print("Digite a quantidade: ");
        try {
            return sc.nextInt();
        } catch (Exception e) {
            System.out.println("Entrada inválida.");
            sc.nextLine();
            return -1;
        }
    }

    private boolean desejaContinuarComprando() {
        System.out.print("\nDeseja adicionar mais algum item? [s/n]: ");
        String resposta = sc.next().trim().toLowerCase();

        while (!resposta.equals("s") && !resposta.equals("n")) {
            System.out.println("Digite apenas [s/n]");
            resposta = sc.next().trim().toLowerCase();
        }
        return resposta.equals("s");
    }

    private void alterarProdutoDaVenda() {
        visualizarProdutosSelecionados();

        System.out.print("\nInsria o ID do Produto que Deseja Alterar: ");
        long idProduto = sc.nextLong();
        Produto produto = crud.exibirUmProdutoPorId(idProduto);
        Estoque estoque = crud.buscarEstoquePorProduto(idProduto);

        if (produto != null) {
            System.out.println("Produto Selecionado: " + produto.getNome());

        } else {
            System.out.println("Produto Não Encontrado.");
        }

        if (estoque == null) {
            System.out.println("Estoque não encontrado para o produto!");
            return;
        }

        System.out.println("\nSelecione uma opção: ");
        System.out.println("1. Adicionar Quantidade");
        System.out.println("2. Subtrair Quantidade");
        System.out.print("Resposta: ");
        int op = sc.nextInt();
        sc.nextLine();

        if (op != 1 && op != 2) {
            System.out.println("Opção Inválida!");
            return;
        }

        System.out.print("Insira a Quantidade: ");
        int quantidadeAlterar = sc.nextInt();

        if (quantidadeAlterar <= 0) {
            System.out.println("Quantidade deve ser positiva.");
            return;
        }

        if (op == 2) {
            quantidadeAlterar = -quantidadeAlterar;
        }

        int quantidadeAtualEstoque = estoque.getQuantidadeDisponivel();
        int novaQuantidadeEstoque = quantidadeAtualEstoque - quantidadeAlterar;

        if (novaQuantidadeEstoque < 0) {
            System.out.println("Não há estoque suficiente para completar a operação!");
            return;
        }

        boolean produtoAlterado = venda.alterarQuantidadeProduto(idProduto, quantidadeAlterar);
        if (!produtoAlterado) {
            System.out.println("Produto não encontrado na venda ou quantidade inválida!");
            return;
        }

        estoque.setQuantidadeDisponivel(novaQuantidadeEstoque);
        crud.atualizarProduto(produto);

        System.out.println("Quantidade do produto alterada com sucesso!");
    }

    private void visualizarProdutosSelecionados() {
        System.out.println("\n--- Produtos Selecionados ---\n");
        double precoTotalPedido = 0.0;
        for (VendaProduto pv : venda.getProdutos()) {
            double precoTotalProduto = pv.getProduto().getPreco() * pv.getQuantidade();
            precoTotalPedido += precoTotalProduto;
            System.out.println("ID: " + pv.getProduto().getId() +
                    "\nProduto: " + pv.getProduto().getNome() +
                    "\nQuantidade: " + pv.getQuantidade() +
                    "\nPreço Unitário: R$ " + String.format("%.2f", pv.getProduto().getPreco()) +
                    "\nPreço Total: R$ " +  String.format("%.2f", precoTotalProduto)
            );
            System.out.println("------------------------------");
        }
        System.out.println("Preço Final do Pedido: R$ " + String.format("%.2f", precoTotalPedido));
    }

    private void finalizarVenda() {
        try {
            visualizarProdutosSelecionados();

            // Calcula o valor total da venda
            double valorTotal = venda.valorTotal();
            venda.setValorTotal(valorTotal);

            // Insere a venda na tabela 'venda'
            long idVenda = crud.inserirVenda(venda);
            venda.setId(idVenda);

            // Solicita o método de pagamento e insere o pagamento na tabela 'pagamento'
            inserirPagamento(venda);

            // Insere os produtos da venda na tabela 'venda_produto'
            for (VendaProduto vp : venda.getProdutos()) {
                crud.inserirVendaProduto(idVenda, vp);

                Estoque estoqueProduto = crud.buscarEstoquePorProduto(vp.getProduto().getId());
                if (estoqueProduto != null) {
                    estoqueProduto.decrementarQuantidade(vp.getQuantidade());
                    crud.atualizarEstoque(estoqueProduto);
                } else {
                    System.out.println("Estoque não encontrado para o produto: " + vp.getProduto().getNome());
                }
            }

            System.out.println("Venda finalizada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao finalizar venda: " + e.getMessage());
        }
    }

    private void inserirPagamento(Venda venda) throws SQLException {

        System.out.println("\nSelecione a opção de pagamento: ");
        System.out.println("1. Dinheiro");
        System.out.println("2. Cartão");
        System.out.print("Resposta: ");
        int opPagamento = lerOpcao();

        if (opPagamento != 1 && opPagamento != 2) {
            System.out.println("Opção inválida.");
            return;
        }

        String metodoPagamento = (opPagamento == 1) ? "Dinheiro" : "Cartão";
        double valorTotal = venda.valorTotal();

        if (opPagamento == 1) {
            System.out.print("Digite o Valor Pago em Espécie: ");
            double valorRecebido = sc.nextDouble();
            sc.nextLine();

            if (valorRecebido < valorTotal) {
                System.out.println("Erro: O valor recebido é menor que o valor total da venda.");
                return;
            }

            double troco = valorRecebido - valorTotal;
            System.out.println("Troco: R$ " + String.format("%.2f", troco));
        }

        Pagamento pagamento = new Pagamento(venda, valorTotal, metodoPagamento);
        crud.inserirPagamento(pagamento);
    }

    private void cancelarVenda() {
        System.out.println("Venda cancelada com sucesso. Nenhum dado foi salvo.");
        // Limpa os produtos e pagamentos associados à venda
        venda.getProdutos().clear();
        venda.getPagamentos().clear();
    }
}
