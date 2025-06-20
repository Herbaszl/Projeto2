package br.com.vendas.dao.factory;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.vendas.domain.Cliente;

public class ClienteFactory {
	
	public static Cliente convert(ResultSet rs) throws SQLException{
		Cliente cliente = new Cliente();
		cliente.setId(rs.getLong("ID_CLIENTE"));
		cliente.setNome(rs.getString(("NOME")));
		cliente.setCpf(rs.getLong(("CPF")));
		cliente.setTel(rs.getLong(("TEL")));
		cliente.setEnd(rs.getString(("ENDERECO")));
		cliente.setCidade(rs.getString(("CIDADE")));
		cliente.setNumero(rs.getInt(("NUMERO")));
		cliente.setEstado(rs.getString(("ESTADO")));
		cliente.setFormaPagamento(rs.getString("FORMA_PAGAMENTO"));
		return cliente;	
	}	
}
