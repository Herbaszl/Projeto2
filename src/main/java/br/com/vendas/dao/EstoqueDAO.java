package br.com.vendas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import br.com.vendas.dao.generics.GenericDAO;
import br.com.vendas.domain.Estoque;
import br.com.vendas.exceptions.DAOException;

public class EstoqueDAO extends GenericDAO<Estoque, Long> implements IEstoqueDAO {

    public EstoqueDAO() {
        super();
    }

    @Override
    public Class<Estoque> getTipoClasse() {
        return Estoque.class;
    }

    @Override
    public void atualizarDados(Estoque entity, Estoque entityCadastrado) {
        entityCadastrado.setQuantidade(entity.getQuantidade());
        entityCadastrado.setDataAtualizacao(new Date());
    }

    @Override
    protected String getQueryInsercao() {
        return "INSERT INTO tb_estoque (ID, ID_PRODUTO, QUANTIDADE, DATA_ATUALIZACAO) VALUES (nextval('sq_estoque'), ?, ?, ?)";
    }

    @Override
    protected void setParametrosQueryInsercao(PreparedStatement stmInsert, Estoque entity) throws SQLException {
        stmInsert.setLong(1, entity.getIdProduto());
        stmInsert.setInt(2, entity.getQuantidade());
        stmInsert.setTimestamp(3, new java.sql.Timestamp(entity.getDataAtualizacao().getTime()));
    }

    @Override
    protected String getQueryExclusao() {
        return "DELETE FROM tb_estoque WHERE id = ?";
    }

    @Override
    protected void setParametrosQueryExclusao(PreparedStatement stmExclusao, Long valor) throws SQLException {
        stmExclusao.setLong(1, valor);
    }

    @Override
    protected String getQueryAtualizacao() {
        return "UPDATE tb_estoque SET QUANTIDADE = ?, DATA_ATUALIZACAO = ? WHERE id = ?";
    }

    @Override
    protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, Estoque entity) throws SQLException {
        stmUpdate.setInt(1, entity.getQuantidade());
        stmUpdate.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
        stmUpdate.setLong(3, entity.getId());
    }

    @Override
    protected void setParametrosQuerySelect(PreparedStatement stmSelect, Long valor) throws SQLException {
        stmSelect.setLong(1, valor);
    }

    @Override
    public Estoque consultarEstoquePorProduto(Long idProduto) throws Exception {
        String sql = "SELECT * FROM tb_estoque WHERE id_produto = ?";
        try (PreparedStatement stm = getConnection().prepareStatement(sql)) {
            stm.setLong(1, idProduto);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return convert(rs);
                }
            }
        }
        return null;
    }

    private Estoque convert(ResultSet rs) throws SQLException {
        Estoque estoque = new Estoque();
        estoque.setId(rs.getLong("id"));
        estoque.setIdProduto(rs.getLong("id_produto"));
        estoque.setQuantidade(rs.getInt("quantidade"));
        estoque.setDataAtualizacao(new Date(rs.getTimestamp("data_atualizacao").getTime()));
        return estoque;
    }

    @Override
    public void adicionarEstoque(Estoque estoque) throws Exception {
        estoque.setDataAtualizacao(new Date());
        super.cadastrar(estoque);
    }

    @Override
    public void removerEstoque(Estoque estoque) throws Exception {
        super.excluir(estoque.getId());
    }

    @Override
    public void atualizarEstoque(Estoque estoque) throws Exception {
        estoque.setDataAtualizacao(new Date());
        super.alterar(estoque);
    }
    
    @Override
    public Estoque buscarPorProduto(Long idProduto) throws DAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Estoque estoque = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement("SELECT * FROM tb_estoque WHERE id_produto = ?");
            stmt.setLong(1, idProduto);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                estoque = new Estoque();
                estoque.setId(rs.getLong("id"));
                estoque.setIdProduto(rs.getLong("id_produto"));
                estoque.setQuantidade(rs.getInt("quantidade"));
            }
            
            return estoque;
        } catch (SQLException e) {
            throw new DAOException("Erro buscando estoque por produto", e);
        } finally {
            closeConnection(conn, stmt, rs);
        }
    }

	@Override
	public void removerTodos() throws DAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement("DELETE FROM tb_estoque");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Erro removendo todos os estoques", e);
        } finally {
            closeConnection(conn, stmt, null);
        }
    }
}

