package br.com.vendas.dao;
import br.com.vendas.dao.generics.*;
import br.com.vendas.domain.Venda;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;

public interface IVendaDAO extends IGenericDAO<Venda, String> {

	
public void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
	
	public void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
}
