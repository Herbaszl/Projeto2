package br.com.vendas;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import br.com.vendas.dao.ClienteDAO;
import br.com.vendas.dao.IClienteDAO;
import br.com.vendas.domain.Cliente;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.MaisDeUmRegistroException;
import br.com.vendas.exceptions.TableException;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;

public class ClienteDaoTest {
	private IClienteDAO clienteDAO;
	
	public ClienteDaoTest() {
		clienteDAO = new ClienteDAO();
		
	}

	@After
	public void end() throws DAOException {
		Collection<Cliente>  list = clienteDAO.buscarTodos();
		list.forEach(cli ->  {
			try {
				clienteDAO.excluir(cli.getCpf());
			}catch (DAOException e) {
				e.printStackTrace();
			}
		});	
	}
	
	
	@Test
	public void pesquisarCliente() throws MaisDeUmRegistroException, TipoChaveNaoEncontradaException, DAOException, TableException{
		Cliente cliente = new Cliente();
		cliente.setCpf(123398412398L);
		cliente.setNome("João");
		cliente.setCidade("Belo Horizonte");
		cliente.setEnd("Rua Gonçalves");
		cliente.setEstado("BH");
		cliente.setNumero(230);
		cliente.setTel(17999999999L);
		cliente.setFormaPagamento("Pix");
		clienteDAO.cadastrar(cliente);
		
		Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
		Assert.assertNotNull(clienteConsultado);
		
		clienteDAO.excluir(cliente.getCpf());
	}
	
	
	
	@Test
	public void salvarCliente() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
		Cliente cliente = new Cliente();
		cliente.setCpf(12312312612L);
		cliente.setNome("João");
		cliente.setCidade("Belo Horizonte");
		cliente.setEnd("Rua Gonçalves");
		cliente.setEstado("BH");
		cliente.setNumero(230);
		cliente.setTel(17999999999L);
		cliente.setFormaPagamento("Cartão Débito");
		Boolean retorno = clienteDAO.cadastrar(cliente);
		Assert.assertTrue(retorno);
		
		Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
		Assert.assertNotNull(clienteConsultado);
		
		clienteDAO.excluir(cliente.getCpf());
	}
	
	
	@Test
	
	public void excluirCliente() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
			Cliente cliente = new Cliente();
			cliente.setCpf(41254325678L);
			cliente.setNome("João");
			cliente.setCidade("Belo Horizonte");
			cliente.setEnd("Rua Gonçalves");
			cliente.setEstado("BH");
			cliente.setNumero(230);
			cliente.setTel(17999999999L);
			cliente.setFormaPagamento("Cartão de Débito");
			Boolean retorno = clienteDAO.cadastrar(cliente);
			Assert.assertTrue(retorno);
		
			Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
			Assert.assertNotNull(clienteConsultado);
			
			clienteDAO.excluir(cliente.getCpf());
			clienteConsultado = clienteDAO.consultar(cliente.getCpf());
			Assert.assertNull(clienteConsultado);
		}
	
	
@Test
public void alterarCliente() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
		Cliente cliente = new Cliente();
		cliente.setCpf(12389204321L);
		cliente.setNome("João");
		cliente.setCidade("Belo Horizonte");
		cliente.setEnd("Rua Gonçalves");
		cliente.setEstado("BH");
		cliente.setNumero(230);
		cliente.setTel(17999999999L);
		cliente.setFormaPagamento("Dinheiro");
		Boolean retorno = clienteDAO.cadastrar(cliente);
		Assert.assertTrue(retorno);
		
		Cliente clienteConsultado = clienteDAO.consultar(cliente.getCpf());
		Assert.assertNotNull(clienteConsultado);
		
		 clienteConsultado.setNome("Rafael Dantas");
		 clienteDAO.alterar(clienteConsultado);
		 
		 Cliente clienteAlterado = clienteDAO.consultar(clienteConsultado.getCpf());
		 Assert.assertNotNull(clienteAlterado);
		 Assert.assertEquals("Rafael Dantas", clienteAlterado.getNome());
		 
		 
		 clienteDAO.excluir(cliente.getCpf());
		 clienteConsultado = clienteDAO.consultar(cliente.getCpf());
		 Assert.assertNull(clienteConsultado);
}


@Test
public void buscarTodos () throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException{	
	Cliente cliente = new Cliente();
	cliente.setCpf(56565656565L);
	cliente.setNome("João Victor");
	cliente.setCidade("Belo Horizonte");
	cliente.setEnd("Rua Maria Chacrinha");
	cliente.setEstado("BH");
	cliente.setNumero(123);
	cliente.setTel(1199999999L);
	cliente.setFormaPagamento("Bitcoins");
	Boolean retorno = clienteDAO.cadastrar(cliente);
	Assert.assertTrue(retorno);
	
	Cliente cliente1 = new Cliente();
	cliente1.setCpf(56565656569L);
	cliente1.setNome("Izabella");
	cliente1.setCidade("Rio de Janeiro");
	cliente1.setEnd("Rua Lorinha");
	cliente1.setEstado("RJ");
	cliente1.setNumero(100);
	cliente1.setTel(2199999999L);
	cliente.setFormaPagamento("Pix");
	Boolean retorno1 = clienteDAO.cadastrar(cliente1);
	Assert.assertTrue(retorno1);
	
	Collection<Cliente> list = clienteDAO.buscarTodos();
	assertTrue(list != null);
	assertTrue(list.size() == 2);
	
	list.forEach(cli -> {
		try {
			clienteDAO.excluir(cli.getCpf());
		} catch (DAOException e) {
			e.printStackTrace();
		}
	});
	
	Collection<Cliente> list1 = clienteDAO.buscarTodos();
	assertTrue(list1 != null);
	assertTrue(list1.size() == 0);
}
}








	
