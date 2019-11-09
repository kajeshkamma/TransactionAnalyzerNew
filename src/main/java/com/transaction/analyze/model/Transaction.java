package com.transaction.analyze.model;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.ParseException;
import com.transaction.analyze.service.CSVLoader;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 5354260901677531850L;

	private static final Logger logger = Logger.getLogger("Transaction");

	private String ID;
	private Date date;
	private double amount;
	private String merchant;
	private String reverseTranId;
	
	private TransactionType transactionType;

	public String getReverseTranId() {
		return reverseTranId;
	}

	public void setReverseTranId(String reverseTranId) {
		this.reverseTranId = reverseTranId;
	}

	
	public Transaction(String line) throws java.text.ParseException {
		String[] values = line.split(",");
		this.ID = values[0].trim();
		try {
			this.date = CSVLoader.ddMMyyyyhhmmss.parse(values[1]);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, String.format("Not able to parse %s", values[1]), e);
		}
		this.amount = Double.parseDouble(values[2]);
		this.merchant = values[3].trim();
		String type = values[4].trim();
		if ("PAYMENT".equals(type)) {
			this.setTransactionType(TransactionType.PAYMENT);
		} else if ("REVERSAL".equals(type)) {
			this.setTransactionType(TransactionType.REVERSAL);
			this.reverseTranId = values[5];
			this.amount *= -1;
		}

	}
	 

	public Transaction() {

	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

}
