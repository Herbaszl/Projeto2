package br.com.vendas.services;
import br.com.vendas.domain.Cliente;
import br.com.vendas.exceptions.TipoChaveNaoEncontradaException;
public interface IClienteService {

	Boolean salvar(Cliente cliente) throws TipoChaveNaoEncontradaException;

	Cliente buscarPorCpf(Long cpf);

	void excluir(Long cpf);

	void alterar(Cliente cliente) throws TipoChaveNaoEncontradaException;

}
