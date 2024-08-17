package main;

import dao.BrasaVivaCRUD;
import model.Cliente;
import model.Produto;
import model.ProdutoVenda;
import model.Venda;

import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BrasaVivaCRUD crud = new BrasaVivaCRUD();
        Cliente clienteLogado = null;

        while (true) {
            System.out.println("\n--- Menu Principal ---\n");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Login de Cliente");
            System.out.println("3. Visualizar Todos os Clientes");
            System.out.println("4. Sair");
            System.out.print("\nEscolha uma opção: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    //Cadastro de Cliente
                    System.out.print("\nNome: ");
                    String nome = sc.nextLine();

                    String cpf;
                    while (true) {
                        System.out.println("CPF (apenas números): ");
                        cpf = sc.nextLine().replaceAll("[^\\d]", "");
                        if (cpf.length() == 11) {
                            break;
                        } else {
                            System.out.println("CPF inválido. Deve conter 11 dígitos.");
                        }
                    }
                    cpf = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");

                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    System.out.print("Telefone: ");
                    String telefone = sc.nextLine();

                    Cliente novoCliente = new Cliente(null, nome, cpf, email, telefone);
                    crud.inserirCliente(novoCliente);
                    System.out.println("Cliente cadastrado com sucesso!");
                    break;

                case 2:
                    // Login do Cliente
                    System.out.print("\nDigite o CPF do cliente (apenas números): ");
                    // Remove todos os caracteres que não são números do CPF digitado
                    String cpfLogin = sc.nextLine().replaceAll("[^\\d]", "");

                    if (cpfLogin.length() == 11) {
                        // Fromata o CPF no padrão xxx.xxx.xxx-xx
                        cpfLogin = cpfLogin.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
                    } else {
                        System.out.println("CPF inválido. Certifique-se de digitar 11 dígitos");
                        break;
                    }

                    List<Cliente> clientesEncontrados = crud.pesquisarClientePorCPF(cpfLogin);

                    if(clientesEncontrados.size() == 1) {
                        clienteLogado = clientesEncontrados.get(0);
                        System.out.println("Login realizado com sucesso!");

                        // Processo de Venda
                        Venda novaVenda = new Venda(clienteLogado);

                        while (true) {
                            System.out.println("\n--- Menu de Vendas ---\n");
                            System.out.println("1. Listar todos os produtos");
                            System.out.println("2. Adicionar produto à compra");
                            System.out.println("3. Remover produto da compra");
                            System.out.println("4. Visualizar os produtos selecionados");
                            System.out.println("5. Finalizar venda");
                            System.out.println("6. Cancelar venda");
                            System.out.print("\nEscolha uma opção: ");
                            int opVenda = sc.nextInt();
                            sc.nextLine();

                            switch (opVenda) {
                                case 1:
                                    // Listar todos os produtos disponíveis
                                    List<Produto> produtos = crud.listarTodosProdutos();
                                    System.out.println("\n--- Produtos Disponíveis ---");
                                    for (Produto produto : produtos) {
                                        System.out.println("ID: " + produto.getId() +
                                                "\nNome: " + produto.getNome() +
                                                "\nPreço: R$ " + produto.getPreco() +
                                                "\nQuantidade disponível: " + produto.getQuantidade() + "\n"
                                        );
                                    }
                                    break;

                                case 2:
                                    // Adicionar produto à compra do cliente
                                    boolean continuarComprando = true;

                                    while(continuarComprando) {
                                        System.out.print("\nDigite o ID do produto que o cliente deseja comprar: ");
                                        long idProduto = sc.nextLong();
                                        System.out.print("Digite a quantidade: ");
                                        int quantidade = sc.nextInt();

                                        Produto produtoEscolhido = crud.exibirUmProduto(idProduto);

                                        if (produtoEscolhido != null && produtoEscolhido.getQuantidade() >= quantidade) {
                                            novaVenda.adicionarProduto(produtoEscolhido, quantidade);
                                            crud.atualizarProduto(produtoEscolhido);
                                            System.out.println("Produto adicionado à compra com sucesso!");
                                        } else {
                                            System.out.println("Quantidade insuficiente ou produto inexistente.");
                                        }

                                        System.out.print("\nDeseja adicionar mais algum item? [s/n]: ");
                                        String resposta = sc.next().trim().toLowerCase();

                                        while (!resposta.equals("s") && !resposta.equals("n")) {
                                            System.out.println("Digite apenas [s/n]");
                                            resposta = sc.next().toLowerCase();
                                        }

                                        if (resposta.equals("n")) {
                                            continuarComprando = false;
                                        }
                                    }
                                    break;

                                case 3:
                                    System.out.print("Digite o ID do produto que deseja remover da compra: ");
                                    long idRemover = sc.nextLong();
                                    System.out.println("Digite a quantidade a ser removida: ");
                                    int quantidadeRemover = sc.nextInt();

                                    boolean produtoRemovido = novaVenda.removerProduto(idRemover, quantidadeRemover);
                                    if (produtoRemovido) {
                                        Produto produtoAtualizar = crud.exibirUmProduto(idRemover);
                                        produtoAtualizar.setQuantidade(produtoAtualizar.getQuantidade() + quantidadeRemover);
                                        System.out.println("Produto removido da venda com sucesso!");
                                    } else {
                                        System.out.println("Produto não encontrado na venda ou quantidade inválida!");
                                    }
                                    break;

                                case 4:
                                    // Mostrar produtos selecionados até agora
                                    System.out.println("\n--- Produtos Selecionados ---\n");
                                    List<ProdutoVenda> produtosSelecionados = novaVenda.getProdutos();
                                    if (produtosSelecionados.isEmpty()) {
                                        System.out.println("Nenhum produto selecionado até agora.");
                                    } else {
                                        double precoTotalPedido = 0.0;

                                        for (ProdutoVenda pv : produtosSelecionados) {
                                            double precoTotalProduto = pv.getProduto().getPreco() * pv.getQuantidade();
                                            precoTotalPedido += precoTotalProduto;

                                            System.out.println("Produto: " + pv.getProduto().getNome() +
                                                    "\nQuantidade: " + pv.getQuantidade() +
                                                    "\nPreço Unitário: R$ " + String.format("%.2f", pv.getProduto().getPreco()) +
                                                    "\nPreço Total: R$ " + String.format("%.2f", precoTotalProduto) + "\n"

                                            );
                                        }
                                        System.out.println("Preço Final do Pedido: R$ " + String.format("%.2f", precoTotalPedido));
                                    }
                                    break;

                                case 5:
                                    // Finalizar venda
                                    double valorTotal = novaVenda.calcularValorTotal();
                                    crud.inserirVenda(novaVenda);
                                    novaVenda.gerarComanda(clienteLogado);
                                    System.out.println("Venda finalizada com sucesso!");
                                    clienteLogado = null;
                                    break;

                                case 6:
                                    // Cancelar venda
                                    System.out.println("Venda cancelada");
                                    clienteLogado = null;
                                    break;

                                default:
                                    System.out.println("Opção inválida. Tente novamente");
                                    break;
                            }

                            if (opVenda == 5 || opVenda == 6) {
                                break;
                            }
                        }
                    } else if (clientesEncontrados.isEmpty()){
                        System.out.println("Cliente não encontrado com o CPF fornecido");
                    } else {
                        System.out.println("Mais de um cliente encontrado com esse CPF. Por favor, contate o suporte");
                    }
                    break;

                case 3:
                    // Visualizar todos os clientes
                    System.out.println("\n--- Lista de Todos os Clientes ---");
                    List<Cliente> todosClientes = crud.listarTodosClientes();
                    for (Cliente cliente : todosClientes) {
                        System.out.println("ID: " + cliente.getId() +
                                "\nNome: " + cliente.getNome() +
                                "\nCPF: " + cliente.getCpf() +
                                "\nEmail: " + cliente.getEmail() +
                                "\nTelefone: " + cliente.getTelefone());
                        System.out.println("----------------------");
                    }
                    break;

                case 4:
                    System.out.println("Saindo do sistema...");
                    sc.close();
                    return;

                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }
}
