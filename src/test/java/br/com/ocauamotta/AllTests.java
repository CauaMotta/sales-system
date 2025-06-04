package br.com.ocauamotta;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ClientServiceTest.class, ClientDAOTest.class,
        ProductServiceTest.class, ProductDAOTest.class,
        SaleDAOTest.class})
public class AllTests {
}