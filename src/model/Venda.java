package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Venda {
    private Long id;
    private Long idCliente;
    private Long idAtendente;
    private final List<ProdutoVenda> produtos;
    private List<Pagamento> pagamentos;
    private final Date dataVenda;

    public Venda() {
        this.produtos = new ArrayList<>();
        this.dataVenda = new Date();
    }

    public Venda(Long id) {
        this();
        this.id = id;

    }

    public Venda(Cliente cliente) {
        this();
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }
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

    public long getIdAtendente() {
        return idAtendente;
    }

    public void setIdAtendente(Long idAtendente) {
        this.idAtendente = idAtendente;
    }

    public List<ProdutoVenda> getProdutos() {
        return new ArrayList<>(produtos);
    }

    public void setProdutos(List<ProdutoVenda> produtos) {
        if (produtos == null) {
            throw new IllegalArgumentException("Lista de produtos não pode ser nula.");
        }
        this.produtos.clear();
        this.produtos.addAll(produtos);
    }

    public Date getDataVenda() {
        return new Date(dataVenda.getTime());
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        ProdutoVenda produtoVenda = encontrarProdutoNaVenda(produto.getId());
        if (produtoVenda != null) {
            produtoVenda.setQuantidade(produtoVenda.getQuantidade() + quantidade);
        } else {
            produtos.add(new ProdutoVenda(produto, quantidade));
        }
    }

    public boolean alterarQuantidadeProduto(long idProduto, int quantidadeAlterar) {
        for (ProdutoVenda pv : produtos) {
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
        return false; // não foram econtrados produtos;
    }


    public void removerProduto(Long idProduto) {
        produtos.removeIf(pv -> pv.getProduto().getId() == idProduto);
    }

    public double calcularValorTotal() {
        double total = 0.0;
        for (ProdutoVenda pv : produtos) {
            total += pv.getProduto().getPreco() * pv.getQuantidade();
        }
        return total;
    }

    // Ao finalizar compra
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

    private ProdutoVenda encontrarProdutoNaVenda(Long idProduto) {
        for (ProdutoVenda pv : produtos) {
            if (pv.getProduto().getId().equals(idProduto)) {
                return pv;
            }
        }
        return null;
    }


}
