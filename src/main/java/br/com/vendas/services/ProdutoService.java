package br.com.vendas.services;

import br.com.vendas.dao.IProdutoDAO;
import br.com.vendas.domain.Produto;
import br.com.vendas.dao.generics.GenericService;

 
public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {

	public ProdutoService(IProdutoDAO dao) {
		super(dao);
	}

}