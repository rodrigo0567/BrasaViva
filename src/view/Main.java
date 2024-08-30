package view;

import dao.BrasaVivaCRUD;

import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static BrasaVivaCRUD crud = new BrasaVivaCRUD();
    private static AreaAtendente areaAtendente = new AreaAtendente(crud);
    private static AreaGerente areaGerente = new AreaGerente();

    public static void main(String[] args) {
        try {
            menuInicial();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void menuInicial() {

        while (true) {
            System.out.print("-----------------------");
            System.out.println("\nBRASA VIVA CHURRASCARIA");
            System.out.println("\n--- Menu Inicial ---\n");
            System.out.println("1. Área do Gerente");
            System.out.println("2. Área do Atendente");
            System.out.println("3. Sair do Sistema");
            System.out.print("\nSelecione uma opção: ");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    areaGerente.areaDoGerente();
                    break;
                case 2:
                    if (areaAtendente.areaDoAtendente()) {
                        continue;
                    }
                    break;
                case 3:
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

