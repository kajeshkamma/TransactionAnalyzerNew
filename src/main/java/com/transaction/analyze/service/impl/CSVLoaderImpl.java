package com.transaction.analyze.service.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.multipart.MultipartFile;
import com.transaction.analyze.model.Transaction;
import com.transaction.analyze.model.TransactionType;
import com.transaction.analyze.service.CSVLoader;
import au.com.bytecode.opencsv.CSVReader;

public class CSVLoaderImpl implements CSVLoader {

	/**
	 * To log error and debug messages.
	 */
	private static final Logger logger = Logger.getLogger("CSVLoaderImpl");
	private CSVReader csvReader;

	@Override
	public List<Transaction> loadTransactions(MultipartFile inputFile) throws IOException, ParseException {
		List<Transaction> transactions = new ArrayList<>();

		Map<String, Integer> transactionMap = new HashMap<>();
		
		Reader reader = new InputStreamReader(inputFile.getInputStream());
		csvReader = new CSVReader(reader);

		try{
			boolean isFirstLine = true;
			String[] eachLine = csvReader.readNext();

			while (eachLine != null) {
				if (isFirstLine) {
					isFirstLine = false;
				} else {

					Transaction transaction = new Transaction();
					transaction.setID(eachLine[0]);
					transaction.setDate(CSVLoader.ddMMyyyyhhmmss.parse(eachLine[1]));
					transaction.setAmount(Double.parseDouble(eachLine[2]));
					transaction.setMerchant(eachLine[3].trim());

					String paymentType = eachLine[4].trim();
					if ("PAYMENT".equalsIgnoreCase(paymentType)) {
						transaction.setTransactionType(TransactionType.PAYMENT);
						transactionMap.put(transaction.getID(), transactions.size());
						transactions.add(transaction);

					} else if ("REVERSAL".equalsIgnoreCase(paymentType)) {
						transaction.setTransactionType(TransactionType.REVERSAL);
						if (eachLine.length > 5) {
							Integer reverseTransaction = transactionMap.get(eachLine[5].trim());
							if (reverseTransaction != null) {
								boolean isRemoved = true;
								if (!isRemoved) {
									logger.log(Level.INFO,
											String.format(
													"Not able to remove original transaction with id '%s' from collection, REVERSAL transaction id is '%s' ",
													reverseTransaction, eachLine[0]));
								}
							} else {
								logger.log(Level.SEVERE,
										String.format(
												"Original transaction not found for the REVERSAL transaction '%s'",
												eachLine[0]));
							}
						} else {
							logger.log(Level.SEVERE,
									String.format("Original transaction id is missing in the REVERSAL transaction '%s'",
											eachLine[0]));
						}
					}

				}
				eachLine = csvReader.readNext();
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		return transactions;
	}

}
