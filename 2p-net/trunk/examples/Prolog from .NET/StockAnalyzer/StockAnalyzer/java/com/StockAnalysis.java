package com;
import java.util.Scanner;

 
public class StockAnalysis {
	private String stock;				//Nome indice azionario
	private String dateAnalysis;		//Giorno in cui viene eseguita l'analisi
	private int daysOfAnalysis;			//Numero di giorni in cui è eseguita l'analisi
	private double Return;				//Valore di ritorno dell'azione
	private double average;				//Media del valori azionari
	private double standardDeviation;	//Deviazione standard dei valori azionari
	//Getter e setter
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getDateAnalysis() {
		return dateAnalysis;
	}
	public void setDateAnalysis(String dateAnalysis) {
		this.dateAnalysis = dateAnalysis;
	}
	public int getDaysOfAnalysis() {
		return daysOfAnalysis;
	}
	public void setDaysOfAnalysis(int daysOfAnalysis) {
		this.daysOfAnalysis = daysOfAnalysis;
	}
	public double getReturn() {
		return Return;
	}
	public void setReturn(float return1) {
		Return = return1;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(float average) {
		this.average = average;
	}
	public double getStandardDeviation() {
		return standardDeviation;
	}
	public void setStandardDeviation(float standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	//Costruttore
	private StockAnalysis(String stock, String dateAnalysis, int daysOfAnalysis,
			double return1, double standardDeviation, double average) {
		super();
		this.stock = stock;
		this.dateAnalysis = dateAnalysis;
		this.daysOfAnalysis = daysOfAnalysis;
		Return = return1;
		this.average = average;
		this.standardDeviation = standardDeviation;
	}	
	//Metodo factory per la costruzione dell'oggetto 
	public static StockAnalysis BuildFromString(String string) throws InstantiationError,NumberFormatException
	{			
		String str=string.replace(',', '.');
		Scanner scanner=new Scanner(str);
		scanner.useDelimiter(";");
		String stock=scanner.next();
		String date=scanner.next();
		String days=scanner.next();
		String returns=scanner.next();
		String stadev=scanner.next();
		String avg=scanner.next();
		scanner.close();
		int daysInt =Integer.parseInt(days);
		Double returnsD =Double.parseDouble(returns);
		Double stadevD =Double.parseDouble(stadev);
		Double avgD =Double.parseDouble(avg);
		return new StockAnalysis(stock,date,daysInt,returnsD,stadevD,avgD);
	}

	@Override
	public String toString() {
		return "StockAnalysis [stock=" + stock + ", dateAnalysis="
				+ dateAnalysis + ", daysOfAnalysis=" + daysOfAnalysis
				+ ", Return=" + Return + ", average=" + average
				+ ", standardDeviation=" + standardDeviation + "]";
	}



}
