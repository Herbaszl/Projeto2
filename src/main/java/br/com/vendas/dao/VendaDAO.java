package br.com.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.vendas.dao.factory.ProdutoQuantidadeFactory;
import br.com.vendas.dao.factory.VendaFactory;
import br.com.vendas.dao.generics.GenericDAO;
import br.com.vendas.domain.Estoque;
import br.com.vendas.domain.ProdutoQuantidade;
import br.com.vendas.domain.Venda;
import br.com.vendas.domain.Venda.Status;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.MaisDeUmRegistroException;
import br.com.vendas.exceptions.TableException;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;

public class VendaDAO extends GenericDAO<Venda, String> implements IVendaDAO {

    private IEstoqueDAO estoqueDAO;
    
    public VendaDAO() {
        super();
        this.estoqueDAO = new EstoqueDAO(); 
    }

    @Override
    public Class<Venda> getTipoClasse() {
        return Venda.class;
    }

    @Override
    public void atualizarDados(Venda entity, Venda entityCadastrado) {
        entityCadastrado.setCodigo(entity.getCodigo());
        entityCadastrado.setStatus(entity.getStatus());
    }

    @Override
    public void excluir(String valor) {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE TB_VENDA SET STATUS_VENDA = ? WHERE ID = ?";
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.setString(1, Status.CONCLUIDA.name());
            stm.setLong(2, venda.getId());
            stm.executeUpdate();
            
        } catch (SQLException e) {
            throw new DAOException("ERRO ATUALIZANDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, null);
        }
    }
    
    @Override
    public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            
            // 1. Atualizar status da venda
            String sql = "UPDATE TB_VENDA SET STATUS_VENDA = ? WHERE ID = ?";
            stm = connection.prepareStatement(sql);
            stm.setString(1, Status.CANCELADA.name());
            stm.setLong(2, venda.getId());
            stm.executeUpdate();
            
            // 2. Devolver produtos ao estoque
            devolverProdutosAoEstoque(venda, connection);
            
            connection.commit();
            
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new DAOException("ERRO AO REALIZAR ROLLBACK", ex);
            }
            throw new DAOException("ERRO CANCELANDO VENDA ", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                // Ignore
            }
            closeConnection(connection, stm, null);
        }
    }

    private void devolverProdutosAoEstoque(Venda venda, Connection connection) throws DAOException {
        PreparedStatement stm = null;
        try {
            for (ProdutoQuantidade prod : venda.getProdutos()) {
                String sql = "UPDATE TB_ESTOQUE SET QUANTIDADE = QUANTIDADE + ? WHERE ID_PRODUTO = ?";
                stm = connection.prepareStatement(sql);
                stm.setInt(1, prod.getQuantidade());
                stm.setLong(2, prod.getProduto().getId());
                stm.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("ERRO AO DEVOLVER PRODUTOS AO ESTOQUE", e);
        } finally {
            closeConnection(null, stm, null);
        }
    }

    @Override
    protected String getQueryInsercao() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO TB_VENDA ");
        sb.append("(ID, CODIGO, ID_CLIENTE_FK, VALOR_TOTAL, DATA_VENDA, STATUS_VENDA)");
        sb.append("VALUES (nextval('sq_venda'),?,?,?,?,?)");
        return sb.toString();
    }

    @Override
    protected void setParametrosQueryInsercao(PreparedStatement stmInsert, Venda entity) throws SQLException {
        stmInsert.setString(1, entity.getCodigo());
        stmInsert.setLong(2, entity.getCliente().getId());
        stmInsert.setBigDecimal(3, entity.getValorTotal());
        stmInsert.setTimestamp(4, Timestamp.from(entity.getDataVenda()));
        stmInsert.setString(5, entity.getStatus().name());
    }

    @Override
    protected String getQueryExclusao() {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    protected void setParametrosQueryExclusao(PreparedStatement stmInsert, String valor) throws SQLException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    protected String getQueryAtualizacao() {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, Venda entity) throws SQLException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    protected void setParametrosQuerySelect(PreparedStatement stm, String valor) throws SQLException {
        stm.setString(1, valor);
    }

    @Override
    public Venda consultar(String valor) throws MaisDeUmRegistroException, TableException, DAOException {
        StringBuilder sb = sqlBaseSelect();
        sb.append("WHERE V.CODIGO = ? ");
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sb.toString());
            setParametrosQuerySelect(stm, valor);
            rs = stm.executeQuery();
            if (rs.next()) {
                Venda venda = VendaFactory.convert(rs);
                buscarProdutosDaVenda(connection, venda);
                return venda;
            }
            
        } catch (SQLException e) {
            throw new DAOException("ERRO CONSULTANDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
        return null;
    }

    private void buscarProdutosDaVenda(Connection connection, Venda venda) throws DAOException {
        PreparedStatement stmProd = null;
        ResultSet rsProd = null;
        try {
            String sql = "SELECT PQ.ID, PQ.QUANTIDADE, PQ.VALOR_TOTAL, " +
                         "P.ID AS ID_PRODUTO, P.CODIGO, P.NOME, P.DESCRICAO, P.VALOR " +
                         "FROM TB_PRODUTO_QUANTIDADE PQ " +
                         "INNER JOIN TB_PRODUTO P ON P.ID = PQ.ID_PRODUTO_FK " +
                         "WHERE PQ.ID_VENDA_FK = ?";
            stmProd = connection.prepareStatement(sql);
            stmProd.setLong(1, venda.getId());
            rsProd = stmProd.executeQuery();
            Set<ProdutoQuantidade> produtos = new HashSet<>();
            while(rsProd.next()) {
                ProdutoQuantidade prodQ = ProdutoQuantidadeFactory.convert(rsProd);
                produtos.add(prodQ);
            }
            venda.setProdutos(produtos);
            venda.recalcularValorTotalVenda();
        } catch (SQLException e) {
            throw new DAOException("ERRO CONSULTANDO PRODUTOS DA VENDA", e);
        } finally {
            closeConnection(connection, stmProd, rsProd);
        }
    }

    @Override
    public Collection<Venda> buscarTodos() throws DAOException {
        List<Venda> lista = new ArrayList<>();
        StringBuilder sb = sqlBaseSelect();
        
        try {
            Connection connection = getConnection();
            PreparedStatement stm = connection.prepareStatement(sb.toString());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Venda venda = VendaFactory.convert(rs);
                buscarProdutosDaVenda(connection, venda);
                lista.add(venda);
            }
            
        } catch (SQLException e) {
            throw new DAOException("ERRO CONSULTANDO OBJETO ", e);
        } 
        return lista;
    }

    private StringBuilder sqlBaseSelect() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT V.ID AS ID_VENDA, V.CODIGO, V.VALOR_TOTAL, V.DATA_VENDA, V.STATUS_VENDA, ");
        sb.append("C.ID AS ID_CLIENTE, C.NOME, C.CPF, C.TEL, C.ENDERECO, C.NUMERO, C.CIDADE, C.ESTADO ");
        sb.append("FROM TB_VENDA V ");
        sb.append("INNER JOIN TB_CLIENTE C ON V.ID_CLIENTE_FK = C.ID ");
        return sb;
    }

    @Override
    public Boolean cadastrar(Venda entity) throws TipoChaveNaoEncontradaException, DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            
            // 1. Verificar estoque antes de iniciar a transação
            verificarEstoque(entity.getProdutos());
            
            // 2. Inserir a venda
            stm = connection.prepareStatement(getQueryInsercao(), Statement.RETURN_GENERATED_KEYS);
            setParametrosQueryInsercao(stm, entity);
            int rowsAffected = stm.executeUpdate();

            if(rowsAffected > 0) {
                try (ResultSet rs = stm.getGeneratedKeys()){
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }
                
                // 3. Atualizar estoque e inserir produtos
                for (ProdutoQuantidade prod : entity.getProdutos()) {
                    atualizarEstoque(prod, connection);
                    inserirProdutoVenda(prod, entity.getId(), connection);
                }
                
                connection.commit();
                return true;
            }
            
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                throw new DAOException("ERRO AO REALIZAR ROLLBACK", ex);
            }
            throw new DAOException("ERRO CADASTRANDO VENDA", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                // Ignore
            }
            closeConnection(connection, stm, null);
        }
        return false;
    }

    private void verificarEstoque(Set<ProdutoQuantidade> produtos) throws DAOException {
        for (ProdutoQuantidade prod : produtos) {
            try {
                Estoque estoque = estoqueDAO.consultarEstoquePorProduto(prod.getProduto().getId());
                if (estoque == null || estoque.getQuantidade() < prod.getQuantidade()) {
                    throw new DAOException("Estoque insuficiente para o produto: " + 
                                         prod.getProduto().getNome() + 
                                         ". Disponível: " + (estoque != null ? estoque.getQuantidade() : 0) + 
                                         ", Solicitado: " + prod.getQuantidade(), null);
                }
            } catch (Exception e) {
                throw new DAOException("ERRO AO VERIFICAR ESTOQUE", e);
            }
        }
    }

    private void atualizarEstoque(ProdutoQuantidade prod, Connection connection) throws DAOException {
        PreparedStatement stm = null;
        try {
            String sql = "UPDATE TB_ESTOQUE SET QUANTIDADE = QUANTIDADE - ? WHERE ID_PRODUTO = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, prod.getQuantidade());
            stm.setLong(2, prod.getProduto().getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ERRO AO ATUALIZAR ESTOQUE", e);
        } finally {
            closeConnection(null, stm, null);
        }
    }

    private void inserirProdutoVenda(ProdutoQuantidade prod, Long idVenda, Connection connection) throws DAOException {
        PreparedStatement stm = null;
        try {
            String sql = "INSERT INTO TB_PRODUTO_QUANTIDADE " +
                         "(ID, ID_PRODUTO_FK, ID_VENDA_FK, QUANTIDADE, VALOR_TOTAL) " +
                         "VALUES (nextval('sq_produto_quantidade'),?,?,?,?)";
            stm = connection.prepareStatement(sql);
            stm.setLong(1, prod.getProduto().getId());
            stm.setLong(2, idVenda);
            stm.setInt(3, prod.getQuantidade());
            stm.setBigDecimal(4, prod.getValorTotal());
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ERRO AO INSERIR PRODUTO DA VENDA", e);
        } finally {
            closeConnection(null, stm, null);
        }
    }
}