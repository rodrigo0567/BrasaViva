package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.Driver;

public class BrasaVivaCRUD {
    private static final String LOCALHOST = "jdbc:mysql://localhost:3306/churrascaria";
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

    // ------------------------------------CRUD_CLIENTE------------------------------------------

    public void inserirCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, cpf, email, telefone) VALUES (?, ?, ?, ?)";
        try {
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

    public void atualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, cpf = ?, email = ?, telefone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setLong(5, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> pesquisarClienteNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    public void removerCliente(Long id){
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List listarTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getLong("id"));
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("CPF: " + rs.getString("cpf"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Telefone: " + rs.getString("telefone"));
                System.out.println("--------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
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

    // ------------------------------------FIM_CRUD_CLIENTE---------------------------------------

    // ------------------------------------CRUD_ESTOQUE-------------------------------------------

    public void inserirEstoque(Estoque estoque) {
        String sql = "INSERT INTO estoque (id_produto, quantidade_disponivel) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, estoque.getProduto().getId());
            stmt.setInt(2, estoque.getQuantidadeDisponivel());
            stmt.executeUpdate();
            System.out.println("Estoque inserido com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarEstoque(Estoque estoque) {
        String sql = "UPDATE estoque SET quantidade_disponivel = ? WHERE id_produto = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, estoque.getQuantidadeDisponivel());
            stmt.setLong(2, estoque.getProduto().getId());
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

    public void removerEstoquePorProduto(long idProduto) throws SQLException {
        String sql = "DELETE FROM estoque WHERE id_produto = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idProduto);
            stmt.executeUpdate();
        }
    }

    public Estoque buscarEstoquePorProduto(Long idProduto) {
        Estoque estoque = null;
        String sql = "SELECT e.id AS estoque_id, e.quantidade_disponivel, " +
                "p.id AS produto_id, p.nome AS produto_nome, p.descricao AS produto_descricao, " +
                "p.preco AS produto_preco, p.categoria AS produto_categoria " +
                "FROM estoque e " +
                "JOIN produto p ON e.id_produto = p.id " +
                "WHERE p.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idProduto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Cria um objeto Produto com os dados obtidos
                    Produto produto = new Produto(
                            rs.getString("produto_nome"),
                            rs.getString("produto_descricao"),
                            rs.getDouble("produto_preco"),
                            rs.getString("produto_categoria")
                    );
                    produto.setId((rs.getLong("produto_id")));

                    // Crie um objeto Estoque com os dados obtidos
                    estoque = new Estoque(
                            rs.getLong("estoque_id"), // ID do estoque
                            produto, // Objeto Produto
                            rs.getInt("quantidade_disponivel") // Quantidade disponível
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estoque;
    }

    public List<Estoque> listarTodosEstoques() {
        List<Estoque> estoques = new ArrayList<>();
        String sql = "SELECT e.id AS estoque_id, e.quantidade_disponivel, " +
                "p.id AS produto_id, p.nome AS produto_nome, p.descricao AS produto_descricao, " +
                "p.preco AS produto_preco, p.categoria AS produto_categoria " +
                "FROM estoque e " +
                "JOIN produto p ON e.id_produto = p.id";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Cria um objeto Produto com os dados obtidos
                Produto produto = new Produto(
                        rs.getString("produto_nome"), // Nome do produto
                        rs.getString("produto_descricao"), // Descrição do produto
                        rs.getDouble("produto_preco"), // Preço do produto
                        rs.getString("produto_categoria") // Descrição do produto
                );
                produto.setId(rs.getLong("produto_id"));

                // Cria um objeto Estoque com os dados obtidos
                Estoque estoque = new Estoque(
                        rs.getLong("estoque_id"), // ID do estoque
                        produto, // Objeto Produto
                        rs.getInt("quantidade_disponivel") // Quantidade disponível
                );

                estoques.add(estoque);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estoques;
    }

    // ----------------------------------FIM_CRUD_ESTOQUE----------------------------------------

    // ------------------------------------CRUD_PRODUTO------------------------------------------

    public Long inserirProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to insert product, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // Retorna o ID gerado
                } else {
                    throw new SQLException("Failed to retrieve the generated ID.");
                }
            }
        }
    }

    public void atualizarProduto(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, categoria = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getCategoria());
            stmt.setLong(5, produto.getId());
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

    public void removerProduto(Long idProduto) throws SQLException{
        String sql = "DELETE FROM produto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idProduto);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Produto removido com sucesso!");
            } else {
                System.out.println("Nenhum produto encontrado com o ID especificado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> listarTodosProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, preco, categoria FROM produto";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("preco"),
                        rs.getString("categoria")
                );
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public Produto exibirUmProdutoPorId(Long id) {
        Produto produto = null;
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto = new Produto(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getDouble("preco"),
                            rs.getString("categoria")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produto;
    }

    public Produto exibirUmProdutoPorNome(String nomeProduto) {
        Produto produto = null;
        String sql = "SELECT * FROM produto WHERE nome = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeProduto);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
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

    public Produto buscarProdutoPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id = ?";
        Produto produto = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    produto = new Produto(id, nome, preco);
                }
            }
        }
        return produto;
    }

    // ----------------------------------FIM_CRUD_PRODUTO----------------------------------------

    // -------------------------------------CRUD_VENDA-------------------------------------------

    public long inserirVenda(Venda venda) throws SQLException {
        if (venda.getIdCliente() == null) {
            throw new IllegalArgumentException("ID do cliente não pode ser nulo.");
        }

        String sql = "INSERT INTO venda (id_cliente, data_venda, valor_total) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, venda.getIdCliente());
            stmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setDouble(3, venda.getValorTotal());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1); // Return the generated ID
                } else {
                    throw new SQLException("Failed to insert venda, no ID obtained.");
                }
            }
        }
    }

    public void inserirVendaProduto(long idVenda, VendaProduto vendaProduto) throws SQLException {
        String sql = "INSERT INTO venda_produto (id_venda, id_produto, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idVenda);
            stmt.setLong(2, vendaProduto.getProduto().getId());
            stmt.setInt(3, vendaProduto.getQuantidade());
            stmt.setDouble(4, vendaProduto.getPrecoVenda());
            stmt.executeUpdate();
        }
    }

    public List<Venda> listarTodasVendas() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM venda";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                Long idCliente = rs.getLong("id_cliente");
                Timestamp dataVenda = rs.getTimestamp("data_venda");
                double valorTotal = rs.getDouble("valor_total");

                Venda venda = new Venda(id, idCliente, dataVenda, valorTotal);
                buscarProdutosParaVenda(venda);
                vendas.add(venda);
            }
        }
        return vendas;
    }

    private void buscarProdutosParaVenda(Venda venda) throws SQLException {
        List<VendaProduto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM venda_produto WHERE id_venda = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, venda.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long idProduto = rs.getLong("id_produto");
                    int quantidade = rs.getInt("quantidade");
                    double precoUnitario = rs.getDouble("preco_unitario");

                    Produto produto = carregarProdutoPorId(idProduto);

                    VendaProduto vendaProduto = new VendaProduto();
                    vendaProduto.setProduto(produto);
                    vendaProduto.setQuantidade(quantidade);
                    vendaProduto.setPrecoVenda(precoUnitario);

                    produtos.add(vendaProduto);
                }
            }
        }
        venda.setProdutos(produtos);
    }

    private Produto carregarProdutoPorId(Long id) throws SQLException {
        Produto produto = new Produto();
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto.setId(rs.getLong("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                }
            }
        }
        return produto;
    }

    public boolean temVendasAssociadas(long clienteId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM venda WHERE id_cliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // -----------------------------------FIM_CRUD_VENDA-----------------------------------------

    // -----------------------------------CRUD_PAGAMENTO-----------------------------------------

    public void inserirPagamento(Pagamento pagamento) throws SQLException {
        String sql = "INSERT INTO pagamento (id_venda, valor_pago, metodo_pagamento) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, pagamento.getVenda().getId());
            stmt.setDouble(2, pagamento.getValorPago());
            stmt.setString(3, pagamento.getMetodoPagamento());
            stmt.executeUpdate();
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
    // -----------------------------------FIM_CRUD_PAGAMENTO-----------------------------------------
}
