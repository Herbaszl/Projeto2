package br.com.vendas.dao.generics;

import java.io.Serializable;
import java.util.Collection;

import br.com.vendas.domain.Persistente;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;


public interface IGenericService <T extends Persistente, E extends Serializable> {
	

    public Boolean cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

   
    public void excluir(E valor) throws DAOException;

    
    public void alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

  
    public T consultar(E valor) throws DAOException;

    
    public Collection<T> buscarTodos() throws DAOException;

}
