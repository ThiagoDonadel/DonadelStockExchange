package br.com.donadel.service.transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.donadel.def.TransactionType;
import br.com.donadel.def.entity.Account;
import br.com.donadel.def.entity.Transaction;
import br.com.donadel.exception.transaction.TransactionDataNotFound;
import br.com.donadel.repository.TransactionRepository;

@Service
public class TransactionService implements ITransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public void registerTransaction(Account account, String stockName, Double price, Integer ammount, Double balance, TransactionType type) {
		Transaction transaction = new Transaction(stockName, type, price, ammount, balance, account);
		transactionRepository.save(transaction);
		System.out.println(transaction.toString());
	}

	@Override
	public String generateReport(String accountName) {
		List<Transaction> data = transactionRepository.findAllByAccount(accountName);
		return createReport(accountName, data);

	}

	@Override
	public String generateReportByStock(String accountName, String stockName) {
		List<Transaction> data = transactionRepository.findAllByName(accountName, stockName);
		return createReport(accountName + "." + stockName, data);
	}

	/**
	 * Cria o arquivo do relatorio baseado nos dados recebidos
	 * 
	 * @param baseFileName
	 *            nome base do arquivo
	 * @param data
	 * @return
	 */
	private String createReport(String baseFileName, List<Transaction> data) {
		String dirPath = System.getProperty("user.dir") + "\\reports";
		String path = dirPath + "\\" + baseFileName + "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm.ss")) + ".txt";

		if (data.size() > 0) {
			try {
				if (!Files.exists(Paths.get(dirPath))) {
					Files.createDirectory(Paths.get(dirPath));
				}
				Path report = Files.createFile(Paths.get(path));
				for (Transaction reportLine : data) {
					String stringLine = reportLine.toString() + System.lineSeparator();
					Files.write(report, stringLine.getBytes(), StandardOpenOption.APPEND);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new TransactionDataNotFound(baseFileName);
		}

		return path;
	}

}
