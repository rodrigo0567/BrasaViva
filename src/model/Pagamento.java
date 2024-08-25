package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Pagamento {
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

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
