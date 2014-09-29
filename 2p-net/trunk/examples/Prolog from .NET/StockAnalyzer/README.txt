This example requires a MySQL server (http://www.mysql.com/) running on the machine and the presence of the following table:

CREATE TABLE IF NOT EXISTS `stockanalysis` (
  `StockName` varchar(40) NOT NULL,
  `Date` varchar(40) NOT NULL,
  `DaysOfAnalysis` int(11) NOT NULL,
  `Return` double NOT NULL,
  `StandardDeviation` double NOT NULL,
  `Average` double NOT NULL,
  PRIMARY KEY (`StockName`,`Date`,`DaysOfAnalysis`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

The parameters to access the DB have to be modified in StockAnalyzer\java\com\JDBCadapter.java

It is necessary also a connection to the Internet to run the example successfully. 