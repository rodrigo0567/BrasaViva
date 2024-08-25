package model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Venda {
    private Long id;
    private Long idCliente;
    private Long idAtendente;
    private final List<VendaProduto> produtos;
    private double valorTotal;
    private List<Pagamento> pagamentos;
    private final Date dataVenda;

    public Venda() {
        this.produtos = new ArrayList<>();
        this.pagamentos = new ArrayList<>();
        this.dataVenda = new Date();
    }

    public Venda(Long id, Long idCliente, Date dataVenda, double valorTotal) {
        this.id = id;
        this.idCliente = idCliente;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.produtos = new ArrayList<>();
        this.pagamentos = new ArrayList<>();
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

    public long getIdAtendente() {
        return idAtendente;
    }

    public void setIdAtendente(Long idAtendente) {
        this.idAtendente = idAtendente;
    }

    public void setProdutos(List<VendaProduto> produtos) {
        if (produtos == null) {
            throw new IllegalArgumentException("Lista de produtos não pode ser nula.");
        }
        this.produtos.clear();
        this.produtos.addAll(produtos);
    }

    public Date getDataVenda() {
        return new Date(dataVenda.getTime());
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public List<VendaProduto> getProdutos() {
        return produtos;
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        VendaProduto produtoVenda = encontrarProdutoNaVenda(produto.getId());
        if (produtoVenda != null) {
            produtoVenda.setQuantidade(produtoVenda.getQuantidade() + quantidade);
        } else {
            produtos.add(new VendaProduto(produto, quantidade));
        }
    }

    public boolean alterarQuantidadeProduto(long idProduto, int quantidadeAlterar) {
        for (VendaProduto pv : produtos) {
            if (pv.getProduto().getId() == idProduto) {
                int novaQuantidade = quantidadeAlterar + pv.getQuantidade();
                if (novaQuantidade < 0) {
                    System.out.println("A quantidade não pode ser negativa!");
                    return false;
                }
                if (novaQuantidade == 0) {
                    produtos.remove(pv);
                } else {
                    pv.setQuantidade(novaQuantidade);
                }
                return true;
            }

        }
        return false;
    }

    public double valorTotal() {
        if (valorTotal == 0.0 && !produtos.isEmpty()) {
            for (VendaProduto pv : produtos) {
                valorTotal += pv.getProduto().getPreco() * pv.getQuantidade();
            }
        }
        return valorTotal;
    }

    private VendaProduto encontrarProdutoNaVenda(Long idProduto) {
        for (VendaProduto pv : produtos) {
            if (pv.getProduto().getId().equals(idProduto)) {
                return pv;
            }
        }
        return null;
    }
}


