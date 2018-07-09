package br.com.donadel.price;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.donadel.def.entity.StockMonitor;
import br.com.donadel.service.stock.StockKeeper;
import br.com.donadel.service.stock.StockPriceGenerator;

@RunWith(SpringRunner.class)
public class StockPriceGeneratorTest {

	@TestConfiguration
	static class PriceGeneratorBeanLoaderConfig {
		@Bean
		public StockPriceGenerator accountService() {
			return new StockPriceGenerator();
		}
	}

	@MockBean
	private StockKeeper stockKeeper;

	@MockBean
	private JmsTemplate jmsTemplate;

	@Autowired
	private StockPriceGenerator priceGenerator;

	@Before
	public void setup() {

	}

	@Test
	public void priceGenerationTest() {
		Collection<StockMonitor> stockMonitors = new ArrayList<>();
		StockMonitor stock = new StockMonitor();
		stock.setId(1L);
		stock.setBuyValue(7.0);
		stock.setSellValue(9.0);
		stockMonitors.add(stock);
		Mockito.when(stockKeeper.getSystemStocks()).thenReturn(stockMonitors);

		priceGenerator.generateNewPrices();
		Double price = priceGenerator.getCurrentPrice(stock.getId());
		if (price < 6.3 || price > 9.9) {
			fail("Valor gerado: " + price + " esperado entre 6.3 e 9.9");
		}
	}

}
