package view;

import java.sql.SQLException;
import java.util.List;
import dao.BrasaVivaCRUD;
import model.*;
import utils.Utilitarios;

import java.util.Scanner;

public class AreaGerente {

    private Scanner sc = new Scanner(System.in);
    private BrasaVivaCRUD crud = new BrasaVivaCRUD();
    private Utilitarios cardapioUtil = new Utilitarios(crud);

    public String areaDoGerente() {
        while(true) {
            System.out.println("\n=*=*=* Área do Gerente =*=*=*\n");
            System.out.println("1. Adicionar Produto");
            System.out.println("2. Gerar Relatório");
            System.out.println("3. Alterar Informações de Produto");
            System.out.println("4. Alterar Quantidade em Estoque");
            System.out.println("5. Alterar Dados de Cliente");
            System.out.println("6. Visualizar Cardápio");
            System.out.println("7. Sair da Área do Gerente");
            System.out.print("\nSelecione uma opção: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    adicionarProduto();
                    break;
                case 2:
                    gerarRelatorio();
                    break;
                case 3:
                    alterarProduto();
                    break;
                case 4:
                    alterarEstoque();
                    break;
                case 5:
                    alterarDadosCliente();
                    break;
                case 6:
                    cardapioUtil.visualizarCardapio();
                    break;
                case 7:
                    return "Saindo...";
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void adicionarProduto() {
        try {
            System.out.println("\n--- Adicionar Novo Produto ---\n");

            System.out.print("Digite o Nome do Produto: ");
            String nomeProduto = sc.nextLine();

            System.out.print("Digite o Preço do Produto (R$): ");
            double precoProduto = sc.nextDouble();
            sc.nextLine();

            if (nomeProduto.isEmpty() || precoProduto <= 0) {
                System.out.println("Erro: Nome do produto não pode ser vazio e o preço deve ser maior que zero.");
                return;
            }

            Produto novoProduto = new Produto(nomeProduto, precoProduto);

            Long idProduto = crud.inserirProduto(novoProduto);
            System.out.println("Produto adicionado com sucesso! ID do Produto: " + idProduto);

            if (idProduto == null) {
                System.out.println("Erro ao inserir o produto");
                return;
            }

            Produto produtoComId = new Produto(idProduto, nomeProduto, precoProduto);

            System.out.print("Digite a Quantidade Inicial em Estoque: ");
            int quantidadeEstoque = sc.nextInt();
            sc.nextLine();

            if (quantidadeEstoque >= 0) {
                Estoque novoEstoque = new Estoque(produtoComId, quantidadeEstoque);
                crud.inserirEstoque(novoEstoque);
                System.out.println("Estoque adicionado com sucesso!");
            } else {
                System.out.println("Erro: A quantidade disponível deve ser maior ou igual a zero.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar o produto: " + e.getMessage());
        }
    }

    private void alterarProduto() {
        cardapioUtil.visualizarCardapio();
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
        cardapioUtil.visualizarCardapio();
        System.out.print("\nDigite o ID do Produto a Ser Alterarado no Estoque: ");
        Long idProduto = sc.nextLong();
        sc.nextLine();

        Estoque estoque = crud.buscarEstoquePorProduto(idProduto);
        if (estoque != null) {
            Produto produto = estoque.getProduto();
            if(produto != null) {
                System.out.println("Produto encontrado: " + produto.getNome());
                System.out.println("Quantidade Atual: " + estoque.getQuantidadeDisponivel());
            } else {
                System.out.println("Produto associado ao estoque não encontrado.");
            }

            System.out.print("Nova Quantidade: ");
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

    private void alterarDadosCliente() {
        System.out.println("\n--- Alterar Dados do Cliente ---\n");
        System.out.println("Buscar Cliente por:");
        System.out.println("1. CPF");
        System.out.println("2. Nome");
        System.out.print("Selecione uma opção (1 ou 2): ");
        int opcao = sc.nextInt();
        sc.nextLine();
        List<Cliente> clientesEncontrados = null;

        switch (opcao) {
            case 1:
                System.out.print("\nDigite o CPF do Cliente: ");
                String cpf = sc.nextLine().replaceAll("[^\\d]", "");

                if (cpf.length() != 11) {
                    System.out.println("CPF inválido. Certifique-se de digitar 11 dígitos.");
                    return;
                }

                cpf = Utilitarios.formatarCPF(cpf);
                clientesEncontrados = crud.pesquisarClienteCPF(cpf);
                break;

            case 2:
                System.out.print("\nDigite o Nome do Cliente: ");
                String nome = sc.nextLine();
                clientesEncontrados = crud.pesquisarClienteNome(nome);
                break;

            default:
                System.out.println("Opção inválida. Tente novamente.");
                return;
        }

        if (clientesEncontrados != null && clientesEncontrados.size() == 1) {
            Cliente cliente = clientesEncontrados.get(0);
            System.out.println("Cliente encontrado: " + cliente.getNome());

            System.out.print("Novo nome do cliente: ");
            String novoNome = sc.nextLine();

            System.out.print("Novo CPF do cliente (apenas números): ");
            String novoCpf = sc.nextLine().replaceAll("[^\\d]", "");
            if (novoCpf.length() != 11) {
                System.out.println("CPF inválido. Deve conter 11 dígitos.");
                return;
            }
            novoCpf = Utilitarios.formatarCPF(novoCpf);

            System.out.print("Novo email do cliente: ");
            String novoEmail = sc.nextLine();

            System.out.print("Novo telefone do cliente: ");
            String novoTelefone = sc.nextLine();

            novoTelefone = Utilitarios.formatarTelefone(novoTelefone);

            cliente.setNome(novoNome);
            cliente.setCpf(novoCpf);
            cliente.setEmail(novoEmail);
            cliente.setTelefone(novoTelefone);

            try {
                crud.atualizarCliente(cliente);
                System.out.println("Dados do cliente atualizados com sucesso!");
            } catch (SQLException e) {
                System.out.println("Erro ao atualizar dados do cliente: " + e.getMessage());
            }

        } else if (clientesEncontrados == null || clientesEncontrados.isEmpty()) {
            System.out.println("Cliente não encontrado com as informações fornecidas.");
        } else {
            System.out.println("Mais de um cliente encontrado com essas informações. Por favor, contate o suporte.");
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
            double valorTotalGeral = 0;
            System.out.println("\n--- Relatório de Vendas ---\n");

            for (Venda venda : vendas) {
                List<VendaProduto> produtosVenda = venda.getProdutos();
                System.out.println("Produtos Associados a Venda: " + (produtosVenda != null ? produtosVenda.size() : "Lista Nula"));

                double valorTotalVenda = venda.valorTotal();
                System.out.println("Venda ID: " + venda.getId() +
                        "\nData: " + venda.getDataVenda() + "\n"
                );

                for (VendaProduto vendaProduto : produtosVenda) {
                    Produto produto = vendaProduto.getProduto();
                    System.out.println("Nome do Produto: " + produto.getNome() +
                            "\nPreço do Produto: R$ " + String.format("%.2f", produto.getPreco()) +
                            "\nQuantidade do Produto: " + vendaProduto.getQuantidade() + "\n"
                    );
                }

                List<Pagamento> pagamentos = venda.getPagamentos();
                System.out.print("\nMétodo de Pagamento: ");
                for (Pagamento pagamento : pagamentos) {
                    System.out.println("- " + pagamento.getMetodoPagamento());
                }

                System.out.println("Valor Total da Venda: R$ " + String.format("%.2f", valorTotalVenda));
                valorTotalGeral += valorTotalVenda;
                System.out.println("----------------------------------");
            }

            System.out.println("\nQuantidade Total de Vendas: " + vendas.size());
            System.out.println("Valor Total das vendas: R$ " + String.format("%.2f", valorTotalGeral));
            System.out.println("----------------------------------");
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
                    "\nValor Unitário: " + String.format("%.2f", estoque.getProduto().getPreco()) +
                    "\nValor Total em Estoque do Produto: R$ " + String.format("%.2f", estoque.getProduto().getPreco()
                    * estoque.getQuantidadeDisponivel())
            );
            System.out.print("--------------------------------\n");
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
