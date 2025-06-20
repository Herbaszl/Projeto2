package br.com.vendas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.vendas.dao.ClienteDAO;
import br.com.vendas.dao.EstoqueDAO;
import br.com.vendas.dao.IClienteDAO;
import br.com.vendas.dao.IEstoqueDAO;
import br.com.vendas.dao.IProdutoDAO;
import br.com.vendas.dao.IVendaDAO;
import br.com.vendas.dao.ProdutoDAO;
import br.com.vendas.dao.VendaDAO;
import br.com.vendas.dao.generics.jdbc.ConnectionFactory;
import br.com.vendas.domain.Cliente;
import br.com.vendas.domain.Estoque;
import br.com.vendas.domain.Produto;
import br.com.vendas.domain.Venda;
import br.com.vendas.domain.Venda.Status;
import br.com.vendas.exceptions.DAOException;
import br.com.vendas.exceptions.MaisDeUmRegistroException;
import br.com.vendas.exceptions.TableException;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;

public class VendaDaoTest {
    private IVendaDAO vendaDao;
    private IClienteDAO clienteDao;
    private IProdutoDAO produtoDao;
    private IEstoqueDAO estoqueDao;

    private Cliente cliente;
    private Produto produto;

    public VendaDaoTest() {
        this.vendaDao = new VendaDAO();
        this.clienteDao = new ClienteDAO();
        this.produtoDao = new ProdutoDAO();
        this.estoqueDao = new EstoqueDAO();
    }

    @Before
    public void init() throws Exception, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        this.cliente = cadastrarCliente();
        this.produto = cadastrarProdutoComEstoque("A1", BigDecimal.TEN, 10);
    }

    @After
    public void end() throws Exception, DAOException {
        excluirVendas();
        excluirProdutos();
        excluirEstoques();
        clienteDao.excluir(this.cliente.getCpf());
    }

    private void excluirProdutos() throws Exception {
        Collection<Produto> produtos = produtoDao.buscarTodos();
        for (Produto produto : produtos) {
            Estoque estoque = estoqueDao.buscarPorProduto(produto.getId());
            if (estoque != null) {
                estoqueDao.removerEstoque(estoque);
            }
            produtoDao.excluir(produto.getCodigo());
        }
    }
    private void excluirEstoques() throws Exception, DAOException {
        Collection<Estoque> estoques = estoqueDao.buscarTodos();
        for (Estoque estoque : estoques) {
            estoqueDao.removerEstoque(estoque);
        }
    }

    @Test
    public void pesquisar() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Venda venda = criarVenda("A1");
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        Venda vendaConsultada = vendaDao.consultar(venda.getCodigo());
        assertNotNull(vendaConsultada);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
        Venda venda = criarVenda("A2");
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        
        assertEquals(BigDecimal.valueOf(20), venda.getValorTotal());
        assertEquals(Status.INICIADA, venda.getStatus());
        
        Venda vendaConsultada = vendaDao.consultar(venda.getCodigo());
        assertNotNull(vendaConsultada.getId());
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void cancelarVenda() throws Exception, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A3";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());
        
        vendaDao.cancelarVenda(venda);
        
        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        assertEquals(codigoVenda, vendaConsultada.getCodigo());
        assertEquals(Status.CANCELADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws Exception, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A4";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());
        
        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(produto, 1);
        
        BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(valorTotal, vendaConsultada.getValorTotal());
        assertEquals(Status.INICIADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws Exception, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A5";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());
        
        Produto prod = cadastrarProdutoComEstoque(codigoVenda, BigDecimal.valueOf(50), 10);
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());
        
        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(valorTotal, vendaConsultada.getValorTotal());
        assertEquals(Status.INICIADA, vendaConsultada.getStatus());
    }

    @Test(expected = DAOException.class)
    public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradaException, DAOException {
        Venda venda = criarVenda("A6");
        assertTrue(vendaDao.cadastrar(venda));
        assertFalse(vendaDao.cadastrar(venda));
        assertEquals(Status.INICIADA, venda.getStatus());
    }

    @Test
    public void removerProduto() throws Exception {
        String codigoVenda = "A7";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());
        
        Produto prod = cadastrarProdutoComEstoque(codigoVenda, BigDecimal.valueOf(50), 10);
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());
        
        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(valorTotal, vendaConsultada.getValorTotal());
        
        vendaConsultada.removerProduto(prod, 1);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(valorTotal, vendaConsultada.getValorTotal());
        assertEquals(Status.INICIADA, vendaConsultada.getStatus());
    }

    @Test
    public void removerTodosProdutos() throws Exception, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A9";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());
        
        Produto prod = cadastrarProdutoComEstoque(codigoVenda, BigDecimal.valueOf(50), 10);
        assertNotNull(prod);
        assertEquals(codigoVenda, prod.getCodigo());
        
        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        
        assertEquals(0, new BigDecimal("70.00").compareTo(vendaConsultada.getValorTotal()));
        
        vendaConsultada.removerTodosProdutos();
        
        assertEquals(0, BigDecimal.ZERO.compareTo(vendaConsultada.getValorTotal()));
        assertEquals(Status.INICIADA, vendaConsultada.getStatus());
    }
    @Test
    public void finalizarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A10";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());
        
        vendaDao.finalizarVenda(venda);
        
        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Status.CONCLUIDA, vendaConsultada.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A11";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        assertTrue(retorno);
        assertNotNull(venda);
        assertEquals(codigoVenda, venda.getCodigo());
        
        vendaDao.finalizarVenda(venda);
        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        assertEquals(Status.CONCLUIDA, vendaConsultada.getStatus());
        
        vendaConsultada.adicionarProduto(this.produto, 1);
    }

    private Produto cadastrarProdutoComEstoque(String codigo, BigDecimal valor, int quantidadeEstoque) 
            throws Exception {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto " + codigo);
        produto.setNome("Produto " + codigo);
        produto.setValor(valor);
        produtoDao.cadastrar(produto);
        
        Estoque estoque = new Estoque();
        estoque.setIdProduto(produto.getId());
        estoque.setQuantidade(quantidadeEstoque);
        estoqueDao.adicionarEstoque(estoque);
        
        return produto;
    }

    private Cliente cadastrarCliente() throws TipoChaveNaoEncontradaException, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(55155155155L);
        cliente.setNome("Rodrigo");
        cliente.setCidade("São Paulo");
        cliente.setEnd("End");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTel(1199999999L); // Telephone now properly set
        clienteDao.cadastrar(cliente);
        return cliente;
    }

    private Venda criarVenda(String codigo) {
        Venda venda = new Venda();
        venda.setCodigo(codigo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(this.cliente);
        venda.setStatus(Status.INICIADA);
        venda.adicionarProduto(this.produto, 2);
        return venda;
    }

    private void excluirVendas() throws DAOException {
        String sqlProd = "DELETE FROM TB_PRODUTO_QUANTIDADE";
        executeDelete(sqlProd);
        
        String sqlV = "DELETE FROM TB_VENDA";
        executeDelete(sqlV);
    }

    private void executeDelete(String sql) throws DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("ERRO EXCLUINDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
    }

    protected void closeConnection(Connection connection, PreparedStatement stm, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    protected Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new DAOException("ERRO ABRINDO CONEXAO COM BANCO DE DADOS ", e);
        }
    }
}