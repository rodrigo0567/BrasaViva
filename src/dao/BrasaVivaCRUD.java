package dao;

import model.Cliente;
import model.Produto;
import model.ProdutoVenda;
import model.Venda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.Driver;

public class BrasaVivaCRUD {
	private static final String LOCALHOST = "jdbc:mysql://localhost:3306/churrascaria_db";
	private static final String USER = "root";
    private static final String PASSWORD = "K22k22k4k2*";

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
        String sql = "INSERT INTO clientes (nome, cpf, email, telefone) VALUES (?, ?, ?, ?)";
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

    public void inserirProduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.executeUpdate();
            System.out.println("Produto inserido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inserirVenda(Venda venda) {
        String sql = "INSERT INTO vendas (id_cliente, data_venda) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, venda.getIdCliente());
            stmt.setDate(2, new java.sql.Date(venda.getDataVenda().getTime()));
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long vendaID = generatedKeys.getLong(1);
                venda.setId(vendaID);

                // Inserir produtos da venda
                for (ProdutoVenda pv : venda.getProdutos()) {
                    inserirProdutoVenda(pv, vendaID);
                }
            }
            System.out.println("Venda registrada com sucesso!");
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
        String sql = "UPDATE clientes SET nome = ?, cpf = ?, email = ?, telefone = ? WHERE id = ?";
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
        String sql = "UPDATE produtos SET nome = ?, quantidade = ?, preco = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setInt(2, produto.getQuantidade());
            stmt.setDouble(3, produto.getPreco());
            stmt.setLong(4, produto.getId());
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
        String sql = "SELECT * FROM clientes WHERE nome LIKE ?";

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
            String sql = "SELECT * FROM clientes WHERE REPLACE(REPLACE(REPLACE(cpf, '.', ''), '-', ''), ' ', '') = ?";
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

    public List<Produto> pesquisarProdutoNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

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
        String sql = "DELETE FROM produtos WHERE id = ?";
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
        String sql = "SELECT * FROM produtos";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getInt("quantidade")
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
        String sql = "SELECT * FROM produtos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                produto = new Produto(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produto;
    }

}
