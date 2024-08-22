package model;

import java.util.Scanner;

import dao.BrasaVivaCRUD;

public class Cliente extends Pessoa{
    private Long id; //Creio que não vai precisar pois já está na tabela como autoincrement
    BrasaVivaCRUD crud = new BrasaVivaCRUD();

    public Cliente(Long id, String nome, String cpf, String email, String telefone) {
        super(nome, cpf, email, telefone);
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
