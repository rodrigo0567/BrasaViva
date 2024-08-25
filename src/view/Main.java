package view;

import dao.BrasaVivaCRUD;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BrasaVivaCRUD crud = new BrasaVivaCRUD();

        AreaAtendente areaAtendente = new AreaAtendente(crud);
        AreaGerente areaGerente = new AreaGerente();

        while (true) {
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
                    areaAtendente.areaDoAtendente();
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

