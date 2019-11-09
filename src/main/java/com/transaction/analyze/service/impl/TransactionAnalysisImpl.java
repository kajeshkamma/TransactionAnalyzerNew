package com.transaction.analyze.service.impl;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import org.springframework.stereotype.Service;
import com.transaction.analyze.model.Transaction;
import com.transaction.analyze.service.CSVLoader;

@Service
public class TransactionAnalysisImpl {

	public DoubleSummaryStatistics analyzeTransactions(Collection<Transaction> transactions, String merchant,
			String startDateTime, String endDateTime) throws ParseException {
		DoubleSummaryStatistics dss = new DoubleSummaryStatistics();

		if (transactions != null && merchant != null && startDateTime != null && endDateTime != null) {
			Date start = CSVLoader.ddMMyyyyhhmmss.parse(startDateTime);
			long startTime = start.getTime()-1;
			start.setTime(startTime);
			
			Date end = CSVLoader.ddMMyyyyhhmmss.parse(endDateTime);
			long endTime = end.getTime()+1;
			end.setTime(endTime);
			
			// summaryStatistics is terminal method.
			dss = transactions
					.stream().parallel().filter(p -> p.getMerchant().equalsIgnoreCase(merchant)
							&& p.getDate().after(start) && p.getDate().before(end))
					.mapToDouble(t -> t.getAmount()).summaryStatistics();

		}
		return dss;
	}

}
