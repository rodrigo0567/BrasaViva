package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.Driver;

public class BrasaVivaCRUD {
	private static final String LOCALHOST = "jdbc:mysql://localhost:3306/churrascaria";
	private static final String USER = "root";
    private static final String PASSWORD = "";

	Driver driver;
    private Connection connection;

    public BrasaVivaCRUD() {
        try {
        	this.driver = new Driver();
			DriverManager.registerDriver(driver);
            this.connection = DriverManager.getConnection(LOCALHOST, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inserirCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, cpf, email, telefone) VALUES (?, ?, ?, ?)";
        try{
        	PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserirEstoque(Estoque estoque) {
        String sql = "INSERT INTO estoque (id_produto, quantidade_disponivel) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, estoque.getIdProduto());
            stmt.setInt(2, estoque.getQuantidadeDisponivel());
            stmt.executeUpdate();
            System.out.println("Estoque inserido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarEstoque(Estoque estoque) {
        String sql = "UPDATE estoque SET quantidade_disponivel = quantidade_disponivel - ? WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, estoque.getQuantidadeDisponivel());
            stmt.setLong(2, estoque.getIdProduto());
            stmt.executeUpdate();
            System.out.println("Estoque atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerEstoque(Long idProduto) {
        String sql = "DELETE FROM estoque WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idProduto);
            stmt.executeUpdate();
            System.out.println("Estoque removido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserirPagamento(Pagamento pagamento) {
        String sql = "INSERT INTO pagamento (id_venda, valor_total, metodo_pagamento, data_pagamento) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, pagamento.getVenda().getId());
            stmt.setDouble(2, pagamento.getValorPago());
            stmt.setString(3, pagamento.getMetodoPagamento());
            stmt.setDate(4, new java.sql.Date(pagamento.getDataPagamento().getTime()));
            stmt.executeUpdate();
            System.out.println("Pagamento inserido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarPagamento(Pagamento pagamento) {
        String sql = "UPDATE pagamentos SET valor = ?, metodo = ?, data_pagamento = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, pagamento.getValorPago());
            stmt.setString(2, pagamento.getMetodoPagamento());
            stmt.setDate(3, new java.sql.Date(pagamento.getDataPagamento().getTime()));
            stmt.setLong(4, pagamento.getId());
            stmt.executeUpdate();
            System.out.println("Pagamento atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerPagamento(Long id) {
        String sql = "DELETE FROM pagamentos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            System.out.println("Pagamento removido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public Pagamento exibirUmPagamento(Long id) {
//        Pagamento pagamento = null;
//        String sql = "SELECT p.id AS pagamento_id, p.id_venda, p.valor_pago, p.metodo_pagamento, p.data_pagamento, " +
//                "v.id_cliente " +
//                "FROM pagamento p " +
//                "JOIN venda v ON p.id_venda = v.id " +
//                "WHERE p.id = ?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setLong(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                Long idVenda = rs.getLong("id_venda");
//                double valorPago = rs.getDouble("valor_pago");
//                String metodoPagamento = rs.getString("metodo_pagamento");
//                Date dataPagamento = rs.getDate("data_pagamento");
//
//                // Criar a instância de Venda correspondente ao pagamento
//                Venda venda = new Venda();
//                venda.setId(idVenda);
//                venda.setIdCliente(rs.getLong("id_cliente"));
//
//                pagamento = new Pagamento(
//                        rs.getLong("pagamento_id"),
//                        venda,
//                        valorPago,
//                        metodoPagamento,
//                        dataPagamento
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return pagamento;
//    }


//    public void inserirProduto(Produto produto) {
//        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, produto.getNome());
//            stmt.setDouble(2, produto.getPreco());
//            stmt.setInt(3, produto.getQuantidade());
//            stmt.executeUpdate();
//            System.out.println("Produto inserido com sucesso!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void inserirVenda(Venda venda) {
        String sql = "INSERT INTO venda (id_cliente, data_venda, valor_total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, venda.getIdCliente());
            stmt.setDate(2, new java.sql.Date(venda.getDataVenda().getTime()));
            stmt.setDouble(3, venda.calcularValorTotal());
            stmt.executeUpdate();
            System.out.println("Venda inserida com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void inserirProdutoVenda(ProdutoVenda pv, long vendaId) {
        String sql = "INSERT INTO venda_produto (id_venda, id_produto, quantidade) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, vendaId);
            stmt.setLong(2, pv.getProduto().getId());
            stmt.setInt(3, pv.getQuantidade());
            stmt.executeUpdate();
            System.out.println("Produto da venda inserido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, cpf = ?, email = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setLong(5, cliente.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cliente atualizado com sucesso!");
            } else {
                System.out.println("Cliente não encontrado para atualização.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarProduto(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, preco = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setLong(3, produto.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Produto atualizado com sucesso!");
            } else {
                System.out.println("Produto não encontrado para atualização.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Cliente> pesquisarClienteNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

    public List<Cliente> pesquisarClienteCPF(String cpf) {
        // Remover a formatação do CPF para garantir que apenas números sejam comparados
        String cpfNumerico = cpf.replaceAll("[^\\d]", "");

        List<Cliente> clientesEncontrados = new ArrayList<>();

        try {
            String sql = "SELECT * FROM cliente WHERE REPLACE(REPLACE(REPLACE(cpf, '.', ''), '-', ''), ' ', '') = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, cpfNumerico);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //Cria objetos Cliente com os dados retornados
                Cliente cliente = new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
                clientesEncontrados.add(cliente);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientesEncontrados;
    }

//    public List<Estoque> pesquisarProdutoNome(String nome) {
//        List<Estoque> estoques = new ArrayList<>();
//        String sql = "SELECT p.id AS produto_id, p.nome, p.preco, e.id AS estoque_id, e.quantidade_disponivel " +
//                "FROM produto p " +
//                "JOIN estoque e ON p.id = e.id_produto " +
//                "WHERE p.nome LIKE ?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, "%" + nome + "%");
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                Produto produto = new Produto(
//                        rs.getLong("produto_id"),
//                        rs.getString("nome"),
//                        rs.getDouble("preco")
//                );
//
//                Estoque estoque = new Estoque(
//                        rs.getLong("estoque_id"),
//                        produto,
//                        rs.getInt("quantidade_disponivel")
//                );
//
//                estoques.add(estoque);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return estoques;
//    }

    public void removerCliente(Long id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("model.Cliente removido com sucesso!");
            } else {
                System.out.println("Nenhum cliente encontrado com o ID especificado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerProduto(Long id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("model.Produto removido com sucesso!");
            } else {
                System.out.println("Nenhum produto encontrado com o ID especificado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listarTodosClientes() {
    	String sql = "SELECT * FROM clientes";
    	try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
            	System.out.println("\n");
            	System.out.println("ID: " + rs.getLong("id"));
            	System.out.println("Nome: " + rs.getString("nome"));
            	System.out.println("CPF: " + rs.getString("cpf"));
            	System.out.println("Email: " + rs.getString("email"));
            	System.out.println("Telefone: " + rs.getString("telefone"));    
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//    public List<Cliente> listarTodosClientes() {
//        List<Cliente> clientes = new ArrayList<>();
//        String sql = "SELECT * FROM clientes";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                Cliente cliente = new Cliente(
//                        rs.getLong("id"),
//                        rs.getString("nome"),
//                        rs.getString("cpf"),
//                        rs.getString("email"),
//                        rs.getString("telefone")
//                );
//                clientes.add(cliente);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return clientes;
//    }

    public List<Produto> listarTodosProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco")
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public Cliente exibirUmCliente(Long id) {
        Cliente cliente = null;
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente = new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public Produto exibirUmProduto(Long id) {
        Produto produto = null;
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                produto = new Produto(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produto;
    }

    public Estoque buscarEstoquePorProduto(Long idProduto) {
        Estoque estoque = null;
        String sql = "SELECT e.id AS estoque_id, e.quantidade_disponivel " +
                "FROM estoque e " +
                "WHERE e.id_produto = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idProduto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Crie um objeto Estoque com os dados obtidos
                estoque = new Estoque(
                        rs.getLong("estoque_id"), // ID do estoque
                        rs.getInt("quantidade_disponivel") // Quantidade disponível
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estoque;
    }



    public Estoque obterEstoquePorProduto(long idProduto) {
        Estoque estoque = null;
        String sql = "SELECT * FROM estoque WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idProduto);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                estoque = new Estoque(
                        rs.getLong("id_produto"),
                        rs.getInt("quantidade_disponivel")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estoque;
    }

}
