package br.com.vendas.dao.generics;
import java.io.Serializable;
import java.util.Collection;

import br.com.vendas.domain.Persistente;
import br.com.vendas.dao.generics.IGenericDAO;
import anotacao.TipoChave;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;


public abstract class GenericService<T extends Persistente, E extends Serializable> implements IGenericService<T, E> {
	
	protected IGenericDAO <T, E> dao;
	

	
	
}
