package br.com.ocauamotta.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ClientRepositoryTest.class, ProductRepositoryTest.class, SaleRepositoryTest.class})
public class AllTests {
}
