# TransactionAnalyser
To display statistic information about processed transactions

## Requirement
The goal of the system is to display statistic information about processed transactions.
A transaction record will contain the following fields:
ID - A String representing the transaction id
Date - The date and time when the transaction took place (format "DD/MM/YYYY hh:mm:ss")
Amount - The value of the transaction (dollars and cents)
Merchant - The name of the merchant this transaction belongs to
Type - The type of the transaction, which could be either PAYMENT or REVERSAL
Related Transaction - (Optional) - In the case a REVERSAL transaction, this field will contain the id of the transaction it is reversing.

The system will be initialized with an input file in CSV format containing a list of transaction records. ( org.deb.loader.CSVLoaderImpl )

Once initialized, the system should report the total number of transactions and the average transaction value for a specific merchant in a specific date range.
Another requirement is that, if a transaction has a reversing transaction, then it should not be included in the computed statistics, even if the reversing transaction is outside of the requested date range. ( org.deb.analysis.Analysis )

### Assumptions
For the sake of simplicity, you can assume that Transaction records are listed in correct time order.
The input file is well formed and is not missing data.


## How to build

mvn clean install 


## How to run
## Application done by using SpringBoot.

Right click on TransactionAnalyzeApplication.java file to run as java application.
Once started the application, Have to enter the below url to access the swagger-ui
URL :- http://localhost:8080/swagger-ui.html

Then click on transaction-analyze-controller link, Will display POST uri,
After then click on Try it now will display the input parameters and click on execute button,

Response will display below details, 

Number of transactions and
Average Transaction Value


### 3rd party libraries
* opencsv - to read CSV files.

# TransactionAnalyzerNew
TransactionAnalyzer for load transaction from csv file and displaying the statistics
