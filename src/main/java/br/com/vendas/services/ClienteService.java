package br.com.vendas.services;

import java.util.Collection;

import br.com.vendas.dao.ClienteDAO;
import br.com.vendas.dao.IClienteDAO;
import br.com.vendas.dao.generics.GenericService;
import br.com.vendas.domain.Cliente;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;

public class ClienteService extends GenericService<Cliente, Long> implements IClienteService {

	private IClienteDAO clienteDAO;
	
	public ClienteService(IClienteDAO clienteDAO) {
		
		this.clienteDAO =  clienteDAO;
	}
	
	
	@Override
	public Boolean salvar(Cliente cliente) throws TipoChaveNaoEncontradaException {
		return clienteDAO.cadastrar(cliente);
	}

	@Override
	public Cliente buscarPorCpf(Long cpf) {
		// TODO Auto-generated method stub
		return clienteDAO.consultar(cpf);
	}


	@Override
	public void excluir(Long cpf) {
		clienteDAO.excluir(cpf);
	}


	@Override
	public void alterar(Cliente cliente)  throws TipoChaveNaoEncontradaException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Boolean cadastrar(Cliente entity) throws TipoChaveNaoEncontradaException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Cliente consultar(Long valor) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Collection<Cliente> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}

}
