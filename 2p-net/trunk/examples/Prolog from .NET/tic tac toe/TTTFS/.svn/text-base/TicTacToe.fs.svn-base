namespace TTTFS
open System.Text

type TicTacToe =
    val mutable private _caselle : char[]
    val mutable private _Xstatus : int
    val mutable private _Ostatus : int 
    val mutable private _remaining : int 
    val private PowArray : int[]
    val private WinArray : int[] 
    new() = { 
        _Xstatus = 0
        _Ostatus = 0
        _remaining = 9
        _caselle = [|'1'; '2'; '3'; '4'; '5'; '6'; '7'; '8'; '9'|]
        PowArray = [| 1; 2; 4; 8; 16; 32; 64; 128; 256 |]
        WinArray = [| 7; 56; 73; 84; 146; 273; 292; 448 |]
        }

    member this.Caselle 
        with get () = this._caselle

    member this.Remaining
        with get () = this._remaining

    member this.Board
        with get () = 
            let sb = System.Text.StringBuilder()
            sb.Append("board(") |> ignore 
            for i in 0 .. this._caselle.Length - 1 do
                if this._caselle.[i] = 'x' || this._caselle.[i] = 'o' then
                    sb.Append(this._caselle.[i]) |> ignore
                else
                    sb.Append('_') |> ignore
                if not(i = this._caselle.Length - 1) then
                    sb.Append(',') |> ignore

            sb.Append(')') |> ignore
            sb.ToString()

    member this.Gioca(casella : int, player: string) =
        let mutable cas = casella
        cas <- cas - 1
        if this._remaining > 0 then
            if not(this._caselle.[cas].Equals('x')) || not(this._caselle.[cas].Equals('o')) then
                if player.Equals("x") then 
                    this._caselle.[cas] <- 'x'
                    this._Xstatus <- this._Xstatus + this.PowArray.[cas]
                else
                    this._caselle.[cas] <- 'o'
                    this._Ostatus <- this._Ostatus + this.PowArray.[cas]
        this._remaining <- this._remaining - 1
        
    member this.CheckWin()  =
        let mutable result = 0
        for i in 0 .. this.WinArray.Length - 1 do
            if (this._Xstatus &&& this.WinArray.[i]) = this.WinArray.[i] then
                result <- 1
            if (this._Ostatus &&& this.WinArray.[i]) = this.WinArray.[i] then
                result <- 2
        result

    member this.PrintBoard() =
        let format = "\n {0} | {1} | {2} \n"
        let sb = StringBuilder()
        for i in 0 .. 3 .. this._caselle.Length - 1 do
                sb.AppendFormat(format, this._caselle.[i], this._caselle.[i+1], this._caselle.[i+2]) |> ignore
        System.Console.WriteLine(sb) |> ignore

    member this.InputMove() =
        let mutable casella : int = 0
        let mutable read : string = null
        let mutable isNotFinished: bool = true
        while isNotFinished do
            try
                try
                    System.Console.Write("Your move: ");
                    read <- System.Console.ReadLine();
                    casella <- System.Int32.Parse(read);
                with 
                    | :? System.FormatException as fex -> ()
                    | ex -> reraise()
            finally
                isNotFinished <- casella < 0 || casella > 10 || this._caselle.[casella-1] <> read.Chars(0)
        casella            

    static member InputResponse() =
        let mutable response : string = null
        while response = null do
            response <- System.Console.ReadLine()
            if response.StartsWith("y") then
                response <- "yes"
            else if response.StartsWith("n") then
                response <- "no"
            else 
                System.Console.Write("Not a valid response! (y/n): ")
                response <- null
        response

    static member InputPlayer() =
        let mutable player : string = null
        while player = null do
            player <- System.Console.ReadLine()
            if player.StartsWith("o") then
                player <- "o"
            else if player.StartsWith("x") then
                player <- "x"
            else 
                System.Console.Write("Not a valid response! (o/x): ")
                player <- null
        player
                

