package com.transaction.analyze.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.transaction.analyze.model.Transaction;
import com.transaction.analyze.service.CSVLoader;
import com.transaction.analyze.service.impl.CSVLoaderImpl;
import com.transaction.analyze.service.impl.TransactionAnalysisImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/upload")
@Api(value = "transaction", description = "loading the csv file for analyzing transactions")
public class TransactionAnalyzeController {
	@Autowired
	private TransactionAnalysisImpl transactionAnalysis;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadCSV(
    @ApiParam(name = "file", value = "Select the file to Upload", required = true)
    @RequestPart("file") MultipartFile inputFile,
    @RequestParam("merchant") String merchant,
    @ApiParam(name = "startDateTime", value = "startDateTime format \"DD/MM/YYYY hh:mm:ss\"", required = true)
    @RequestParam("startDateTime") String startDateTime,
    @ApiParam(name = "endDateTime", value = "endDateTime format \"DD/MM/YYYY hh:mm:ss\"", required = true)
    @RequestParam("endDateTime") String endDateTime
    ) throws ParseException {
		DoubleSummaryStatistics dss;
		  try {
		  CSVLoader loader = new CSVLoaderImpl();
		  Collection<Transaction> transactions = loader.loadTransactions(inputFile);
		  dss = transactionAnalysis.analyzeTransactions(transactions, merchant, startDateTime, endDateTime);

		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		 
		  return ResponseEntity.status(HttpStatus.OK)
			        .body("1)Number of transactions        " + dss.getCount()+"\n2)Average Transaction Value     "+dss.getAverage());
	}

}
