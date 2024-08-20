package model;

import java.util.Scanner;

import dao.BrasaVivaCRUD;

public class Cliente extends Pessoa{
    private Long id; //Creio que não vai precisar pois já está na tabela como autoincrement
    private String telefone;
    BrasaVivaCRUD crud = new BrasaVivaCRUD();

    public Cliente(String nome, String cpf, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }
    
    public Cliente(Long id, String nome, String cpf, String email, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
    }
    
    public void cadastraCliente(){
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.print("\nNome: ");
        this.nome = sc.nextLine();

        while (true) {
            System.out.println("CPF (apenas números): ");
            this.cpf = sc.nextLine().replaceAll("[^\\d]", "");
            if (cpf.length() == 11) {
                break;
            } else {
                System.out.println("CPF inválido. Deve conter 11 dígitos.");
            }
        }
        this.cpf = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");

        System.out.print("Email: "); this.email = sc.nextLine();
        System.out.print("Telefone: "); this.telefone = sc.nextLine();

        crud.inserirCliente(new Cliente(nome,cpf,email,telefone));
        System.out.println("Cliente cadastrado com sucesso!");
    	
    }

    public Long getId() {
        return id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
