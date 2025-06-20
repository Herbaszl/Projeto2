package br.com.vendas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.vendas.dao.EstoqueDAO;
import br.com.vendas.dao.IEstoqueDAO;
import br.com.vendas.dao.IProdutoDAO;
import br.com.vendas.dao.ProdutoDAO;
import br.com.vendas.domain.Estoque;
import br.com.vendas.domain.Produto;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;

public class EstoqueDaoTest {
    private IEstoqueDAO estoqueDao;
    private IProdutoDAO produtoDao;
    private Produto produto;

    @Before
    public void init() throws TipoChaveNaoEncontradaException, DAOException {
        estoqueDao = new EstoqueDAO();
        produtoDao = new ProdutoDAO();
        produto = new Produto();
        produto.setCodigo("P1");
        produto.setDescricao("Produto Teste");
        produto.setNome("Produto 1");
        produto.setValor(BigDecimal.valueOf(100));
        produtoDao.cadastrar(produto);
    }

    @After
    public void end() throws DAOException {
        estoqueDao.removerTodos();
        produtoDao.excluir(produto.getCodigo());
    }

    @Test
    public void testAdicionarEstoque() throws Exception {
        Estoque estoque = new Estoque();
        estoque.setIdProduto(produto.getId());
        estoque.setQuantidade(10);
        
        estoqueDao.adicionarEstoque(estoque); 
        
        Estoque estoqueConsultado = estoqueDao.buscarPorProduto(produto.getId());
        
        assertNotNull(estoqueConsultado);
        assertEquals(produto.getId(), estoqueConsultado.getIdProduto());
    }
}