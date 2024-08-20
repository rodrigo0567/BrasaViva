package model;

public class Vendedor extends Pessoa{
	private String matricula;
	private double salario;
	
	public Vendedor(String matricula, String nome, String cpf, String email, String telefone, double salario) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.matricula = matricula;
        this.salario = salario;
    }
	
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public double getSalario() {
		return salario;
	}
	public void setSalario(double salario) {
		this.salario = salario;
	}
	
	
}
