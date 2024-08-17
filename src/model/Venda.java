package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Venda {
    private Long id;
    private Long idCliente;
    private List<ProdutoVenda> produtos;
    private Date dataVenda;

    public Venda() {
        this.produtos = new ArrayList<>();
        this.dataVenda = new Date();
    }

    public Venda(Cliente cliente) {
        this();
        this.idCliente = cliente.getId();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public List<ProdutoVenda> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoVenda> produtos) {
        this.produtos = produtos;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        for (ProdutoVenda pv : produtos) {
            if (pv.getProduto().getId().equals(produto.getId())) {
                pv.setQuantidade(pv.getQuantidade() + quantidade);
                return;
            }
        }
        produtos.add(new ProdutoVenda(produto, quantidade));
    }

    public boolean removerProduto(Long idProduto, int quantidade) {
        for (ProdutoVenda pv : produtos) {
            if (pv.getProduto().getId().equals(idProduto)) {
                if (quantidade >= pv.getQuantidade()) {
                    produtos.remove(pv);
                } else {
                    pv.setQuantidade(pv.getQuantidade() - quantidade);
                }
                return true;
            }
        }
        return false;
    }

    public double calcularValorTotal() {
        double total = 0.0;
        for (ProdutoVenda pv : produtos) {
            total += pv.getProduto().getPreco() * pv.getQuantidade();
        }
        return total;
    }

    public void gerarComanda(Cliente cliente) {
        System.out.println("\n--- Comanda ---\n");
        System.out.println("Cliente Nome: " + cliente.getNome());
        System.out.println("Cliente CPF: " + cliente.getCpf());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("Data: " + sdf.format(dataVenda));
        System.out.println("Produtos: ");

        for (ProdutoVenda pv : produtos) {
            Produto produto = pv.getProduto();
            System.out.println(produto.getNome() + " - Quantidade: " + pv.getQuantidade() + " - Preço unitário: R$ " + produto.getPreco());
        }

        double valorTotal = calcularValorTotal();
        System.out.println("\nValor total: R$ " + valorTotal);
        System.out.println("===================");
    }
}
