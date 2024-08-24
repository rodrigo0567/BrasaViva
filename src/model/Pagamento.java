package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Pagamento {
    private Long id;
    private Venda venda;
    private double valorPago;
    private String metodoPagamento;
    private Date dataPagamento;

    public Pagamento(Venda venda, double valorPago, String metodoPagamento) {
        this.venda = venda;
        this.valorPago = valorPago;
        this.metodoPagamento = metodoPagamento;
        this.dataPagamento = new Date();
    }

    public Long getId() {
        return id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        if (venda == null) {
            throw new IllegalArgumentException("Venda não pode ser nula.");
        }
        this.venda = venda;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return "Pagamento [id=" + id + ", idVenda=" + (venda != null ? venda.getId() : "null") +
                ", valorPago=" + valorPago + ", metodoPagamento=" + metodoPagamento +
                ", dataPagamento=" + sdf.format(dataPagamento) + "]";
    }

}
