package br.com.vendas.services;

import br.com.vendas.domain.Cliente;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;
import br.com.vendas.dao.generics.IGenericService;

public interface IClienteService extends IGenericService<Cliente, Long> {


		Cliente buscarPorCPF(Long cpf) throws DAOException;

	

}
