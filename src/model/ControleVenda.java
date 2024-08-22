package model;

import dao.BrasaVivaCRUD;
import java.util.List;
import java.util.Scanner;

import model.Pagamento;

public class ControleVenda {
    private final Scanner sc;
    private final BrasaVivaCRUD crud;
    private final Cliente clienteLogado;
    private final Venda venda;

    public ControleVenda(Scanner sc, BrasaVivaCRUD crud, Cliente clienteLogado) {
        this.sc = sc;
        this.crud = crud;
        this.clienteLogado = clienteLogado;
        this.venda = new Venda(clienteLogado);
    }

    public void processarVenda() {
        boolean emProgresso = true;

        while (true) {
            exibirMenu();
            int opVenda = lerOpcao();

            switch (opVenda) {
                case 1:
                    visualizarCardapio();
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
                    System.out.println("Venda cancelada");
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n--- Menu de Vendas ---\n");
        System.out.println("1. Visualizar Cardápio");
        System.out.println("2. Adicionar produto à venda");
        System.out.println("3. Alterar produto da venda");
        System.out.println("4. Visualizar os produtos selecionados");
        System.out.println("5. Finalizar venda");
        System.out.println("6. Cancelar venda");
        System.out.print("\nEscolha uma opção: ");
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

    private void visualizarCardapio() {
        List<Produto> produtos = crud.listarTodosProdutos(); // Obtém todos os produtos do banco de dados

        System.out.println("\n--- Cardápio ---");
        for (Produto produto : produtos) {
            System.out.println("ID: " + produto.getId());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: R$ " + String.format("%.2f", produto.getPreco()));
            System.out.println("----------------");
        }
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
                estoqueProduto.decrementarQuantidade(quantidade);
                crud.atualizarEstoque(estoqueProduto);
                System.out.println("Produto adicionado à compra com sucesso!");
            } else {
                System.out.println("Quantidade insuficiente no estoque.");
            }

            continuarComprando = desejaContinuarComprando();
        }
    }

    private Produto selecionarProduto() {
        System.out.print("\nDigite o ID do produto que o cliente deseja comprar: ");
        try {
            long idProduto = sc.nextLong();
            sc.nextLine(); // Limpa o buffer
            return crud.exibirUmProduto(idProduto);
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
            sc.nextLine(); // Limpa o buffer em caso de erro
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
        System.out.println("Digite o ID do produto que deseja alterar na compra: ");
        long idProduto = sc.nextLong();
        System.out.println("Selecione uma opção: ");
        System.out.println("1. Adicionar Quantidade");
        System.out.println("2. Subtrair Quantidade");
        int op = sc.nextInt();
        sc.nextLine();

        if (op != 1 && op != 2) {
            System.out.println("Opção inválida!");
            return;
        }

        System.out.println("Digite a quantidade: ");
        int quantidadeAlterar = sc.nextInt();

        if (quantidadeAlterar <= 0) {
            System.out.println("Quantidade deve ser positiva.");
            return;
        }

        if (op == 2) {
            quantidadeAlterar = -quantidadeAlterar;
        }

        Produto produto = crud.exibirUmProduto(idProduto);
        if (produto == null) {
            System.out.println("Produto não encontrado!");
            return;
        }

        Estoque estoque = crud.buscarEstoquePorProduto(idProduto);
        if (estoque == null) {
            System.out.println("Estoque não encontrado para o produto!");
            return;
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
        System.out.println("\n--- Produtos Selecionados ---");
        double precoTotalPedido = 0.0;
        for (ProdutoVenda pv : venda.getProdutos()) {
            double precoTotalProduto = pv.getProduto().getPreco() * pv.getQuantidade();
            precoTotalPedido += precoTotalProduto;
            System.out.println("Produto: " + pv.getProduto().getNome() +
                    "\nQuantidade: " + pv.getQuantidade() +
                    "\nPreço Unitário: R$ " + String.format("%.2f", pv.getProduto().getPreco()) +
                    "\nPreço total: R$ " +  String.format("%.2f", precoTotalProduto) + "\n"

            );
        }
        System.out.println("Preço Final do Pedido: R$ " + String.format("%.2f", precoTotalPedido));
    }

    private void finalizarVenda() {
        // Exibir os produtos selecionados e o valor total
        visualizarProdutosSelecionados();

        // Calcular o valor total da venda
        double valorTotal = venda.calcularValorTotal();
        System.out.println("Preço Final do Pedido: R$ " + String.format("%.2f", valorTotal));

        // Solicitar forma de pagamento
        System.out.println("Escolha a forma de pagamento:");
        System.out.println("1. Dinheiro");
        System.out.println("2. Cartão");
        int formaPagamento = lerOpcao();

        if (formaPagamento != 1 && formaPagamento != 2) {
            System.out.println("Opção inválida.");
            return;
        }

        Pagamento pagamento;
        if (formaPagamento == 1) {
            pagamento = new Pagamento(valorTotal, "Dinheiro");
        } else {
            pagamento = new Pagamento(valorTotal, "Cartão");
        }

        // Inserir a venda e o pagamento no banco de dados
        crud.inserirVenda(venda);
        crud.inserirPagamento(pagamento);

        // Atualizar o estoque
        for (ProdutoVenda pv : venda.getProdutos()) {
            Estoque estoque = crud.buscarEstoquePorProduto(pv.getProduto().getId());
            if (estoque != null) {
                estoque.decrementarQuantidade(pv.getQuantidade());
                crud.atualizarEstoque(estoque);
            }
        }

        System.out.println("Venda finalizada com sucesso!");
    }
}
