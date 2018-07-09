package br.com.donadel.service.stock;

import java.util.List;

import br.com.donadel.def.entity.StockMonitor;

public interface IStockMonitorService {

	/**
	 * Adiciona um novo monitoramento de uma ação no sistema
	 * 
	 * @param accountName
	 *            Nome da conta a qual o monitoramento deve estar ligado
	 * @param newStockMonitor
	 *            dados do novo monitoramento.
	 */
	void createNewStockMonitor(String accountName, StockMonitor newStockMonitor);

	/**
	 * Busca todos os monitoramentos de uma conta
	 * 
	 * @param accountName
	 *            nome da conta
	 * @return Lista com os monitoramentos da conta.
	 */
	List<StockMonitor> getAllStockByAccount(String accountName);

	/**
	 * Remove o monitoramento de uma ação. Sempre que uma ação for removida, caso
	 * exista uma quantidade de ações no monitoramento, essa quantidade será vendida
	 * pelo valor atual da ação, independente do valor de venda parametrizado.
	 * 
	 * @param accountName
	 *            nome da conta do monitoramento
	 * @param stockName
	 *            nome da ação do monitoramento.
	 * @return
	 */
	boolean stopMonitoring(String accountName, String stockName);

	/**
	 * Reage as mudanças de preços. Chamado via JMS
	 */
	public void reactToMarket();
}
