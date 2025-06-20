package br.com.vendas.dao;

import br.com.vendas.dao.generics.IGenericDAO;
import br.com.vendas.domain.Estoque;
import br.com.vendas.exceptions.DAOException;


public interface IEstoqueDAO extends IGenericDAO<Estoque, Long> {
   public void adicionarEstoque(Estoque estoque) throws Exception;
   public void removerEstoque(Estoque estoque) throws Exception;
   public Estoque consultarEstoquePorProduto(Long idProduto) throws Exception;
   public void atualizarEstoque(Estoque estoque) throws Exception;
   public	Estoque buscarPorProduto(Long idProduto)throws DAOException;
   public	void removerTodos() throws DAOException;
}
