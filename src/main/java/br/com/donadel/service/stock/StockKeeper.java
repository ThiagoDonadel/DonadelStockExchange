package br.com.donadel.service.stock;

import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.donadel.def.entity.StockMonitor;
import br.com.donadel.exception.stock.StockNotMonitoredException;
import br.com.donadel.repository.StockMonitorRepository;

/**
 * @author thiago.donadel
 * 
 *         Classe Responsavél por manter as ações monitoradas na memoria para
 *         que não seja necessário um acesso ao banco a todo momento.
 */
@Service
public class StockKeeper {

	@Autowired
	private StockMonitorRepository stockRepository;

	/* Guarda as ações mapedas pelo ID para um acesso mais rapido */
	private HashMap<Long, StockMonitor> systemStocks;

	public StockKeeper() {
		systemStocks = new HashMap<>();
	}

	@PostConstruct
	private void init() {
		// Na inicialização carrega todas os monitoramentos ja cadastrados
		Iterable<StockMonitor> stocks = stockRepository.findAll();
		stocks.forEach(stock -> systemStocks.put(stock.getId(), stock));
	}

	/**
	 * Adiciona uma ação a memoria
	 * 
	 * @param stock
	 *            ação a ser monitorada
	 */
	public void keep(StockMonitor stock) {
		systemStocks.put(stock.getId(), stock);
	}

	/**
	 * Retira uma ação da memoria
	 * 
	 * @param stockId
	 *            id da ação
	 */
	public void release(Long stockId) {
		systemStocks.remove(stockId);
	}

	/**
	 * Busca todas as ações que estão sendo monitoradas
	 * 
	 * @return
	 */
	public Collection<StockMonitor> getSystemStocks() {
		return systemStocks.values();
	}

	/**
	 * Busca uma ação da memoria
	 * 
	 * @param stockId
	 *            id da ação
	 * @return ação encontrada.
	 */
	public StockMonitor getStock(Long stockId) {
		if (!systemStocks.containsKey(stockId)) {
			throw new StockNotMonitoredException();
		}
		return systemStocks.get(stockId);
	}

}
