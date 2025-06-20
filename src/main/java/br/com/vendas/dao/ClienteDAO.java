	package br.com.vendas.dao;
	
	import java.sql.PreparedStatement;
	import java.sql.SQLException;
	
	import br.com.vendas.dao.generics.GenericDAO;
	import br.com.vendas.domain.Cliente;
	
	public class ClienteDAO extends GenericDAO<Cliente, Long> implements IClienteDAO {
	
	
		public ClienteDAO() {
			super();
		}
	
		@Override
		public Class<Cliente> getTipoClasse() {
			return Cliente.class;
		}
	
		@Override
		public void atualizarDados(Cliente entity, Cliente entityCadastrado) {
			entityCadastrado.setCidade(entity.getCidade());
			entityCadastrado.setCpf(entity.getCpf());
			entityCadastrado.setEnd(entity.getEnd());
			entityCadastrado.setEstado(entity.getEstado());
			entityCadastrado.setNome(entity.getNome());
			entityCadastrado.setNumero(entity.getNumero());
			entityCadastrado.setTel(entity.getTel());
			entityCadastrado.setFormaPagamento(entity.getFormaPagamento());
			
		}
	
		@Override
		protected String getQueryInsercao() {
		    StringBuilder sb = new StringBuilder();
		    sb.append("INSERT INTO tb_cliente ");  
		    sb.append("(ID, NOME, CPF, TEL, ENDERECO, NUMERO, CIDADE, ESTADO, FORMA_PAGAMENTO)");  
		    sb.append("VALUES (nextval('sq_cliente'), ?, ?, ?, ?, ?, ?, ?, ?)");
		    return sb.toString();
		}
		@Override
		protected void setParametrosQueryInsercao(PreparedStatement stmInsert, Cliente entity) throws SQLException {
			stmInsert.setString(1,  entity.getNome());
			stmInsert.setLong(2, entity.getCpf());
			stmInsert.setLong(3, entity.getTel());
			stmInsert.setString(4, entity.getEnd());
			stmInsert.setLong(5, entity.getNumero());
			stmInsert.setString(6, entity.getCidade());
			stmInsert.setString(7, entity.getEstado());		
			stmInsert.setString(8, entity.getFormaPagamento());
		}
	
		
		@Override
		protected String getQueryExclusao() {
			return "Delete from tb_cliente where cpf = ?";
		}
		
		
		@Override
		protected void setParametrosQueryExclusao(PreparedStatement stmExclusao, Long valor) throws SQLException {
			stmExclusao.setLong(1, valor);
		}
	
		@Override
		protected void setParametrosQueryAtualizacao(PreparedStatement stmUpdate, Cliente entity) throws SQLException {
			stmUpdate.setString(1, entity.getNome());
			stmUpdate.setLong(2, entity.getTel());
			stmUpdate.setString(3, entity.getEnd());
			stmUpdate.setLong(4, entity.getNumero());
			stmUpdate.setString(5, entity.getCidade());
			stmUpdate.setString(6, entity.getEstado());
			stmUpdate.setString(7, entity.getFormaPagamento());
			stmUpdate.setLong(8, entity.getCpf());
		}
	
	
		@Override
		protected void setParametrosQuerySelect(PreparedStatement stmSelect, Long valor) throws SQLException {
			stmSelect.setLong(1, valor);
		}
	
	
		@Override
		protected String getQueryAtualizacao() {
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE TB_CLIENTE ");
			sb.append("SET NOME = ?,");
			sb.append("TEL = ?,");
			sb.append("ENDERECO = ?,");
			sb.append("NUMERO = ?,");
			sb.append("CIDADE = ?,");
			sb.append("ESTADO = ?,");
			sb.append("FORMA_PAGAMENTO = ?");
			sb.append(" WHERE CPF = ?");
			return sb.toString();
		}
	
		
	
	
	}
