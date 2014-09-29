// Basato sul codice di Luca Bolognese https://channel9.msdn.com/Blogs/pdc2008/TL11/

(*
Esempio file csv

        Date,Open,High,Low,Close,Volume,Adj Close
        2014-02-04,505.85,509.46,502.76,508.79,13452900,505.76

la prima riga fa da intestazione, nelle successive i valori indicati dalla stessa.
I valori usati nel nostro caso sono solo la data(il primo) e il valore alla chiusura(l'ultimo)
*)
namespace Stock
open System
open System.Net
open System.IO

module internal Prices =
    //Metodo per il recupero dei dati 
    let loadPrices stock = async { // async indica che il emtodo è asincorno
        let url="http://ichart.finance.yahoo.com/table.csv?s="+stock+"&d=1&e=4&f=2014&g=d&a=7&b=19&c=2004&ignore=.csv"

        let req = System.Net.WebRequest.Create(url)
        let! resp = req.AsyncGetResponse()//Viene effettuata una richiesta asincorna
        let stream = resp.GetResponseStream()
        let reader = new System.IO.StreamReader(stream)
        let csv = reader.ReadToEnd()

        let prices=
            csv.Split([|'\n'|])
            |> Seq.skip 1 //Salta la prima riga
            |> Seq.map (fun line -> line.Split([|','|]))//Separa la riga in stringhe separate da ,
            |> Seq.filter(fun values -> values |> Seq.length =7 )//Selezione le righe che hanno effettivamente 7 stringhe
            |> Seq.map (fun values ->                
                System.DateTime.Parse(values.[0]),//Selezione la data
                float values.[6])//Seleziona il valore alal chiusura
        return  prices }


type StockAnalyzer(lprices,daysIn)=      
    let days=daysIn
    let date=DateTime.Now.Date.ToString("d")//Assegna la data attuale alla data
    let prices=
        lprices
        |> Seq.map snd
        |> Seq.take days
    static member GetAnalyzers (stocks, days)= //Metodo factory
        stocks               
        |>Seq.map Prices.loadPrices 
        |>Async.Parallel
        |>Async.RunSynchronously
        |>Seq.map(fun prices -> new StockAnalyzer(prices,days))
    member s.Days=
        days
    member s.Return= //Calcola il valore di ritorno
        let lastPrice = prices |> Seq.nth 0
        let startPrice =prices |> Seq.nth  (days - 1 )
        lastPrice / startPrice - 1.
    member s.StdDev = //Calcola la deviazione standard 
        let logRets =
            prices
            |> Seq.pairwise
            |> Seq.map(fun (x,y)-> log(x/y))
        let mean = logRets |> Seq.average
        let sqr x = x * x
        let var = logRets |> Seq.averageBy(fun r -> sqr(r-mean))
        sqrt var
    member s.Average = //Calcola la media 
        prices |> Seq.average
    member s.ToString = 
        let time = DateTime.Now
        ""+date+";"+s.Days.ToString()+";"+s.Return.ToString()+";"+s.StdDev.ToString()+";"+s.Average.ToString()
    
        






