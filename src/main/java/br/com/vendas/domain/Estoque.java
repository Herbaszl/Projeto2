package br.com.vendas.domain;

import anotacao.ColunaTabela;
import anotacao.Tabela;
import anotacao.TipoChave;

@Tabela("tb_estoque")
public class Estoque implements Persistente {
    
    @ColunaTabela(dbName = "id", setJavaName = "setId")
    private Long id;
    
    @TipoChave("getId")
    @ColunaTabela(dbName = "id_produto", setJavaName = "setIdProduto")
    private Long idProduto;
    
    @ColunaTabela(dbName = "quantidade", setJavaName = "setQuantidade")
    private Integer quantidade;
    
    @ColunaTabela(dbName = "data_atualizacao", setJavaName = "setDataAtualizacao")
    private java.util.Date dataAtualizacao;

    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public java.util.Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(java.util.Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}