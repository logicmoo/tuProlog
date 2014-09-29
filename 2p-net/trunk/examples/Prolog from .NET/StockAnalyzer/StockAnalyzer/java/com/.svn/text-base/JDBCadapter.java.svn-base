package com;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.jar.JarFile;

public class JDBCadapter {
	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	//Variabili per la configurazione della connessione al database
	private final String hostname="localhost";
	private final int port=3306;
	private final String db="test";
	private final String user="root";
	private final String password="ciao";


	private String pathToJar="lib/mysql-connector-java-5.1.29-bin.jar";//Posizione del mysql connector

	public JDBCadapter() throws Exception {	
		//Caricamento del driver attraverso reflection
		JarFile jarFile = new JarFile(pathToJar);		
		URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);
		Class c = cl.loadClass("com.mysql.jdbc.Driver");
		Driver driver=(Driver) c.newInstance();		
		//Connessione al databse
		String con="jdbc:mysql://"+hostname+":"+port+"/"+db+"?"+"user="+user+"&password="+password;
		connect=driver.connect(con, null);
		connect.createStatement();		
		jarFile.close();		
	}

	public int StockAnalysisAdder(StockAnalysis sa) throws Exception
	{
		//Controlla la presenza della stessa tupla all'interno del database
		preparedStatement = connect.prepareStatement("select * from "+db+".stockanalysis where stockname=? and date=? and daysofanalysis = ?");
		preparedStatement.setString(1,sa.getStock());
		preparedStatement.setString(2,sa.getDateAnalysis());
		preparedStatement.setInt(3, sa.getDaysOfAnalysis());
		resultSet=preparedStatement.executeQuery();
		if(!resultSet.next()){
			//Inserimento della tupla
			preparedStatement = connect.prepareStatement("insert into "+db+".stockanalysis values (?, ?, ?, ?, ? , ?)");
			preparedStatement.setString(1,sa.getStock());
			preparedStatement.setString(2,sa.getDateAnalysis());
			preparedStatement.setInt(3, sa.getDaysOfAnalysis());
			preparedStatement.setDouble(4, sa.getReturn());
			preparedStatement.setDouble(5, sa.getStandardDeviation());
			preparedStatement.setDouble(6, sa.getAverage());
			preparedStatement.executeUpdate();
			return 0;
		}		
		return 1;
	}
	//Crea un oggetto StockAnalysis a partire da una stringa che passa ala metodo StockAnalysisAdder
	public int stockAnalysisAdderStr(String str) throws InstantiationError, NumberFormatException, Exception
	{		
		return StockAnalysisAdder(StockAnalysis.BuildFromString(str));		
	}
}

