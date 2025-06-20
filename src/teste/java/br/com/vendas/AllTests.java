package br.com.vendas;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({ ClienteServiceTest.class, ClienteDaoTest.class,
	ProdutoServiceTest.class, ProdutoDaoTest.class,
	VendaDaoTest.class, EstoqueDaoTest.class})

public class AllTests {
	
	

}
