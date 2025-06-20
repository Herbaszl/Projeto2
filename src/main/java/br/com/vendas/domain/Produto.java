package br.com.vendas.domain;
import java.math.BigDecimal;

import  anotacao.*;
@Tabela ("TB_PRODUTO")
public class Produto implements Persistente{
	@ColunaTabela (dbName = "id",  setJavaName = "setId")
	private Long id;
	
	@ColunaTabela (dbName = "nome",  setJavaName = "setNome")
	private String nome;
	
	@TipoChave("getCodigo")
	@ColunaTabela (dbName = "codigo",  setJavaName = "setCodigo")
	private String codigo;
	
	@ColunaTabela (dbName =  "descricao", setJavaName = "setDescricao")
	private String descricao;
	
	@ColunaTabela (dbName =  "valor", setJavaName = "setValor")
	private BigDecimal valor;
	
	@ColunaTabela (dbName =  "Promocao", setJavaName = "setIsEmPromocao")
	private Boolean isEmPromocao;
	
	
	

	public Boolean getIsEmPromocao() {
		return isEmPromocao;
	}

	public void setIsEmPromocao(Boolean isEmPromocao) {
		this.isEmPromocao = isEmPromocao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


}
