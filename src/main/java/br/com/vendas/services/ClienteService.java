package br.com.vendas.services;


import br.com.vendas.dao.IClienteDAO;
import br.com.vendas.domain.Cliente;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.MaisDeUmRegistroException;
import br.com.vendas.exceptions.TableException;
import br.com.vendas.dao.generics.GenericService;


public class ClienteService extends GenericService<Cliente, Long> implements IClienteService {
	
	
	public ClienteService(IClienteDAO clienteDAO) {
		super(clienteDAO);
	}




	@Override
	public Cliente buscarPorCPF(Long cpf) throws DAOException {
		try {
			return this.dao.consultar(cpf);
		} catch (MaisDeUmRegistroException | TableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}

