package br.com.donadel.service.stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.donadel.def.entity.StockMonitor;
import br.com.donadel.exception.stock.StockNotMonitoredException;
import br.com.donadel.util.RoundUtil;

/**
 * @author thiago.donadel
 * 
 *         Serviço responsável por gerar os novos preços das ações monitoradas a
 *         cada 5 segundos
 *
 */
@Service
public class StockPriceGenerator {

	@Autowired
	private StockKeeper stockKeeper;

	@Autowired
	private JmsTemplate jmsTemplate;

	private static final double priceModifier = 0.1;

	private HashMap<Long, Double> currentPriceMap;

	public StockPriceGenerator() {
		currentPriceMap = new HashMap<>();
	}

	/**
	 * Método que gera aleatoriamente um novo valor para
	 */

	/**
	 * Método responsável por gerar as novas taxas a cada 5 segundos.
	 */
	@Scheduled(fixedRate = 5000)
	public void generateNewPrices() {
		Collection<StockMonitor> sysStocks = stockKeeper.getSystemStocks();
		if (sysStocks.size() > 0) {
			sysStocks.iterator().forEachRemaining(stock -> {
				Double min = stock.getBuyValue() * (1 - priceModifier);
				Double max = stock.getSellValue() * (1 + priceModifier);
				Double newPrice = ThreadLocalRandom.current().nextDouble(min, max);
				newPrice = RoundUtil.currencyTruncate(newPrice);
				currentPriceMap.put(stock.getId(), newPrice);
			});
			/* Envia uma mensagem para dizer que a geração dos novos valores acabou */
			jmsTemplate.convertAndSend("reactToMarket", "Atulização de Preços Finalizada");
		}
	}

	/**
	 * Busca o preço atual de uma ação monitorada
	 * 
	 * @param stockId
	 *            id da ação
	 * @return preço atual
	 */
	public Double getCurrentPrice(Long stockId) {
		Double currentPrice = 0.0;

		if (currentPriceMap.containsKey(stockId)) {
			currentPrice = currentPriceMap.get(stockId);
		} else {
			throw new StockNotMonitoredException();
		}

		return currentPrice;
	}

}
