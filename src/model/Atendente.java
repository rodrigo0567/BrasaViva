package model;

import dao.BrasaVivaCRUD;
import java.util.Scanner;

public class Atendente extends Pessoa{
	private int id;
	private double salario;

	private final Scanner sc;
	private final BrasaVivaCRUD crud;

	public Atendente(String nome, String cpf, String email, String telefone, int id, double salario, Scanner sc, BrasaVivaCRUD crud) {
		super(nome, cpf, email, telefone);
		this.id = id;
		this.salario = salario;
		this.sc = sc;
		this.crud = crud;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getSalario() {
		return salario;
	}
	public void setSalario(double salario) {
		this.salario = salario;
	}

}
	
	
