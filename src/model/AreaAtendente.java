package model;

import java.util.List;
import dao.BrasaVivaCRUD;
import java.util.Scanner;

public class AreaAtendente {

    private Scanner sc = new Scanner(System.in);
    private BrasaVivaCRUD crud = new BrasaVivaCRUD();

    public void areaDoAtendente() {
        while(true) {
            System.out.println("\n=*=*=* Área do Atendente =*=*=*\n");
            System.out.println("1. Cadastro de Cliente");
            System.out.println("2. Realizar Venda");
            System.out.println("3. Visualizar Cardápio");
            System.out.println("4. Sair da Área do Atendente");
            System.out.print("\nSelecione uma opção: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    cadastrarCliente();
                    break;
                case 2:
                    realizarVenda();
                    break;
                case 3:
                    visualizarCardapio();
                    break;
                case 4:
                    System.out.println("Saindo...");
                    sc.close();
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

    }

    private void cadastrarCliente() {
        System.out.print("\nNome: ");
        String nome = sc.nextLine();

        String cpf;
        while (true) {
            System.out.print("CPF (apenas números): ");
            cpf = sc.nextLine().replaceAll("[^\\d]", "");
            if (cpf.length() == 11) {
                break;
            } else {
                System.out.println("CPF inválido. Deve conter apenas 11 dígitos.");
            }
        }
        cpf = formatarCPF(cpf);

        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();

        Cliente novoCliente = new Cliente(null, nome, cpf, email, telefone);
        crud.inserirCliente(novoCliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private void realizarVenda() {
        System.out.print("Insira o CPF do cliente atendido: ");
        String cpfLogin = sc.nextLine().replaceAll("[^\\d]", "");

        if (cpfLogin.length() == 11) {
            cpfLogin = formatarCPF(cpfLogin);
        } else {
            System.out.println("CPF inválido. Certifique-se de digitar 11 dígitos");
            return;
        }

        List<Cliente> clientesEncontrados = crud.pesquisarClienteCPF(cpfLogin);

        if (clientesEncontrados.size() == 1) {
            Cliente clienteLogado = clientesEncontrados.get(0);
            System.out.println("\nCliente encontrado com sucesso!");
            ControleVenda vendaController = new ControleVenda(sc, crud, clienteLogado);
            vendaController.processarVenda();
        } else if (clientesEncontrados.isEmpty()) {
            System.out.println("Cliente não encontrado com o CPF fornecido");
        } else {
            System.out.println("Mais de um cliente encontrado com esse CPF. Por favor, contate o suporte");
        }
    }

    private void visualizarCardapio() {
        List<Produto> produtos = crud.listarTodosProdutos();
        System.out.println("\n--- Produtos Disponíveis ---");
        for (Produto produto : produtos) {
            System.out.println("ID: " + produto.getId() +
                    "\nNome: " + produto.getNome() +
                    "\nPreço: R$ " + produto.getPreco() +
                    "\nQuantidade disponível: " + produto.getQuantidade() + "\n"
            );
        }
    }
}
