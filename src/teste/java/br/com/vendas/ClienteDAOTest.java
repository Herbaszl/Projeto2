package br.com.vendas;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import br.com.vendas.dao.*;
import br.com.vendas.domain.*;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;
import br.com.vendas.services.*;

public class ClienteDAOTest {
	
	private IClienteDAO clienteDao;
	private Cliente cliente;
	
	public ClienteDAOTest() {
		clienteDao = new ClienteDaoMock();
	}
	
	@Before
	public void init() throws TipoChaveNaoEncontradaException {
		cliente = new Cliente();
		cliente.setCpf(1231231231L);
		cliente.setNome("Roberto");
		cliente.setCidade("Rio de Janeiro");
		cliente.setEnd("Rua Amazonia");
		cliente.setNumero(10);
		cliente.setTel(21999999999L);
		clienteDao.cadastrar(cliente);
		
	}
	@Test
	public void pesquisarCliente() {
		Cliente clienteConsultado = clienteDao.consultar(cliente.getCpf());
	    
		Assert.assertNotNull(clienteConsultado);
	}
	@Test
	public void salvarCliente() throws TipoChaveNaoEncontradaException {
		clienteDao.cadastrar(cliente);

	}
	
	@Test
	public void excluirCliente() {
		
		clienteDao.excluir(cliente.getCpf());
		
	}
	
	@Test
	public void alterarCliente() throws TipoChaveNaoEncontradaException {
		cliente.setNome("Rodrigo Faro");
		Assert.assertEquals("Rodrigo Faro", cliente.getNome());
		clienteDao.alterar(cliente);
	}
}

