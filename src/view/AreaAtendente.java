package view;

import java.util.List;
import dao.BrasaVivaCRUD;
import model.Cliente;
import controller.ControleVenda;
import utils.Utilitarios;

import java.util.Scanner;

public class AreaAtendente {

    private Scanner sc = new Scanner(System.in);
    private BrasaVivaCRUD crud = new BrasaVivaCRUD();
    private Utilitarios cardapioUtil = new Utilitarios(crud);

    public AreaAtendente(BrasaVivaCRUD crud) {
        this.crud = crud;
    }

    public String areaDoAtendente() {
        while (true) {
            System.out.println("\n=*=*=* Área do Atendente =*=*=*\n");
            System.out.println("1. Cadastrar Cliente");
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
                    cardapioUtil.visualizarCardapio();
                    break;
                case 4:
                    return "Saindo...";
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void cadastrarCliente() {
        System.out.println("\n=*=*=* Cadastro do Cliente =*=*=*\n");
        System.out.println("Insira as informações a baixo:");
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
        cpf = Utilitarios.formatarCPF(cpf);

        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        telefone = Utilitarios.formatarTelefone(telefone);

        Cliente novoCliente = new Cliente(null, nome, cpf, email, telefone);
        crud.inserirCliente(novoCliente);
        System.out.println("Cliente cadastrado com sucesso!");
    }

    private void realizarVenda() {
        System.out.println("\n=*=*=* Login Cliente =*=*=*\n");
        System.out.println("Escolha a forma de busca do cliente:");
        System.out.println("1. Buscar por CPF");
        System.out.println("2. Buscar por Nome");
        System.out.print("Selecione uma opção: ");
        int opcao = sc.nextInt();
        sc.nextLine(); // Consumir a nova linha restante

        List<Cliente> clientesEncontrados = null;

        switch (opcao) {
            case 1:
                System.out.print("Insira o CPF do Cliente: ");
                String cpfLogin = sc.nextLine().replaceAll("[^\\d]", "");

                if (cpfLogin.length() != 11) {
                    System.out.println("CPF inválido. Certifique-se de digitar 11 dígitos");
                    return;
                }

                cpfLogin = Utilitarios.formatarCPF(cpfLogin);
                clientesEncontrados = crud.pesquisarClienteCPF(cpfLogin);
                break;

            case 2:
                System.out.print("Insira o Nome do Cliente: ");
                String nomeCliente = sc.nextLine();
                clientesEncontrados = crud.pesquisarClienteNome(nomeCliente);
                break;

            default:
                System.out.println("Opção inválida. Tente novamente.");
                return;
        }

        if (clientesEncontrados != null) {
            if (clientesEncontrados.size() == 1) {
                Cliente clienteLogado = clientesEncontrados.get(0);
                System.out.println("\nCliente encontrado com sucesso!");
                ControleVenda controleVenda = new ControleVenda(sc, crud, clienteLogado);
                controleVenda.processarVenda();
            } else if (clientesEncontrados.isEmpty()) {
                System.out.println("Cliente não encontrado com as informações fornecidas");
            } else {
                System.out.println("Mais de um cliente encontrado com essas informações. Por favor, contate o suporte");
            }
        }
    }
}
