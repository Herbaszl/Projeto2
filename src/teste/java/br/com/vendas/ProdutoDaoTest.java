package br.com.vendas;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import br.com.vendas.dao.IProdutoDAO;
import br.com.vendas.dao.ProdutoDAO;
import br.com.vendas.domain.Produto;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.MaisDeUmRegistroException;
import br.com.vendas.exceptions.TableException;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;

public class ProdutoDaoTest {

	private IProdutoDAO produtoDao;

	public ProdutoDaoTest() {
		produtoDao = new ProdutoDAO();
	}
	
	@After
	public void end() throws DAOException {
		Collection<Produto> list = produtoDao.buscarTodos();
		list.forEach(prod -> {
			try {
				produtoDao.excluir(prod.getCodigo());
			} catch (DAOException e) {
				e.printStackTrace();
			}
		});
	}

	private Produto criarProduto(String codigo) throws TipoChaveNaoEncontradaException, DAOException {
		Produto produto = new Produto();
		produto.setCodigo(codigo);
		produto.setDescricao("Produto 1");
		produto.setNome("Produto 1");
		produto.setValor(BigDecimal.TEN);
		produto.setIsEmPromocao(false);
		produtoDao.cadastrar(produto);
		return produto;
	}
	
	private void excluir(String valor) throws DAOException {
		this.produtoDao.excluir(valor);
	}
	
	@Test
	public void pesquisar() throws MaisDeUmRegistroException, TableException, DAOException, TipoChaveNaoEncontradaException {
		Produto produto = criarProduto("A1");
		Assert.assertNotNull(produto);
		Produto produtoDB = this.produtoDao.consultar(produto.getCodigo());
		Assert.assertNotNull(produtoDB);
		excluir(produtoDB.getCodigo());
	}
	
	@Test
	public void salvar() throws TipoChaveNaoEncontradaException, DAOException {
		Produto produto = criarProduto("A2");
		Assert.assertNotNull(produto);
		excluir(produto.getCodigo());
	}
	
	@Test
	public void excluir() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
		Produto produto = criarProduto("A3");
		Assert.assertNotNull(produto);
		excluir(produto.getCodigo());
		Produto produtoBD = this.produtoDao.consultar(produto.getCodigo());
		assertNull(produtoBD);
	}
	
	@Test
	public void alterarCliente() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
		Produto produto = criarProduto("A4");
		produto.setNome("Guaravita de Limão");
		produto.setIsEmPromocao(false);
		produtoDao.alterar(produto);
		
		Produto produtoBD = this.produtoDao.consultar(produto.getCodigo());
		assertNotNull(produtoBD);
	 	Assert.assertEquals("Guaravita de Limão", produtoBD.getNome());
		
		excluir(produto.getCodigo());
		Produto produtoBD1 = this.produtoDao.consultar(produto.getCodigo());
		assertNull(produtoBD1);
	}
	
	@Test
	public void buscarTodos() throws DAOException, TipoChaveNaoEncontradaException {
		criarProduto("A5");
		criarProduto("A6");
		Collection<Produto> list = produtoDao.buscarTodos();
		assertTrue(list != null);
		assertTrue(list.size() == 2);
		
		for (Produto prod : list) {
			excluir(prod.getCodigo());
		}
		
		list = produtoDao.buscarTodos();
		assertTrue(list != null);
		assertTrue(list.size() == 0);
		
	}
	
	
}
