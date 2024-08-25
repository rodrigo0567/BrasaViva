package view;

import java.util.List;
import dao.BrasaVivaCRUD;
import model.Cliente;
import service.ControleVenda;
import utils.CardapioUtil;

import java.util.Scanner;

public class AreaAtendente {

    private Scanner sc = new Scanner(System.in);
    private BrasaVivaCRUD crud = new BrasaVivaCRUD();
    private CardapioUtil cardapioUtil = new CardapioUtil(crud);

    public String areaDoAtendente() {
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

        if (cpfLogin.length() != 11) {
            System.out.println("CPF inválido. Certifique-se de digitar 11 dígitos");
            return;
        }

        cpfLogin = formatarCPF(cpfLogin);

        List<Cliente> clientesEncontrados = crud.pesquisarClienteCPF(cpfLogin);

        if (clientesEncontrados.size() == 1) {
            Cliente clienteLogado = clientesEncontrados.get(0);
            System.out.println("\nCliente encontrado com sucesso!");
            ControleVenda controleVenda = new ControleVenda(sc, crud, clienteLogado);
            //método processarVenda abre outra tela
            controleVenda.processarVenda();
        } else if (clientesEncontrados.isEmpty()) {
            System.out.println("Cliente não encontrado com o CPF fornecido");
        } else {
            System.out.println("Mais de um cliente encontrado com esse CPF. Por favor, contate o suporte");
        }
    }

}
