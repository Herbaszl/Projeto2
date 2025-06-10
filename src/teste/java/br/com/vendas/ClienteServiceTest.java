package br.com.vendas;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.vendas.dao.ClienteDaoMock;
import br.com.vendas.domain.Cliente;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;
import br.com.vendas.services.ClienteService;
import br.com.vendas.services.IClienteService;
import br.com.vendas.dao.IClienteDAO;

public class ClienteServiceTest {
	
	private IClienteService clienteService;
	private Cliente cliente;
	
	public ClienteServiceTest() {
		IClienteDAO dao = new ClienteDaoMock();
		clienteService = new ClienteService(dao);
	}
	
	@Before
	public void init() {
		cliente = new Cliente();
		cliente.setCpf(1231231231L);
		cliente.setNome("Roberto");
		cliente.setCidade("Rio de Janeiro");
		cliente.setEnd("Rua Amazonia");
		cliente.setNumero(10);
		cliente.setTel(21999999999L);

		
		
	}
	
	@Test
	public void pesquisarCliente() {
		
		Cliente clienteConsultado = clienteService.buscarPorCpf(cliente.getCpf());
    
		Assert.assertNotNull(clienteConsultado);
	}
	
	@Test
	public void salvarCliente() throws TipoChaveNaoEncontradaException {
		Boolean retorno = clienteService.salvar(cliente);
		Assert.assertTrue(retorno);
	}
	
	@Test
	public void excluirCliente() {
		
		clienteService.excluir(cliente.getCpf());
		
	}
	
	@Test
	public void alterarCliente() throws TipoChaveNaoEncontradaException {
		cliente.setNome("Rodrigo Faro");
		clienteService.alterar(cliente);
		
		Assert.assertEquals("Rodrigo Faro", cliente.getNome());
	}
	
}

 