Imports System.Text

Public Class TicTacToe
    Private _caselle As Char() = {"1"c, "2"c, "3"c, "4"c, "5"c, "6"c, "7"c, "8"c, "9"c}
    Private _Xstatus As Integer, _Ostatus As Integer
    Private _remaining As Integer

    Private Shared ReadOnly PowArray As Integer() = {1, 2, 4, 8, 16, 32, 64, 128, 256}
    Private Shared ReadOnly WinArray As Integer() = {7, 56, 73, 84, 146, 273, 292, 448}

    Public Sub New()
        _Xstatus = 0
        _Ostatus = 0
        _remaining = 9
    End Sub

#Region "properties"

    Public ReadOnly Property Caselle() As Char()
        Get
            Return _caselle
        End Get
    End Property

    Public ReadOnly Property Remaining() As Integer
        Get
            Return _remaining
        End Get
    End Property

    Public ReadOnly Property Board() As String
        Get
            Dim sb As New StringBuilder()
            sb.Append("board(")
            For i As Integer = 0 To _caselle.Length - 1
                If _caselle(i) = "x"c OrElse _caselle(i) = "o"c Then
                    sb.Append(_caselle(i))
                Else
                    sb.Append("_"c)
                End If

                If i <> (_caselle.Length - 1) Then
                    sb.Append(","c)
                End If
            Next
            sb.Append(")"c)
            Return sb.ToString()
        End Get
    End Property

#End Region

    Public Sub Gioca(casella As Integer, player As String)
        casella -= 1
        If _remaining > 0 Then
            If _caselle(casella) <> "x"c OrElse _caselle(casella) <> "o"c Then
                If player.Equals("x") Then
                    _caselle(casella) = "x"c
                    _Xstatus += PowArray(casella)
                ElseIf player.Equals("o") Then
                    _caselle(casella) = "o"c
                    _Ostatus += PowArray(casella)
                End If
            End If
            _remaining -= 1
        End If
    End Sub

    Public Function CheckWin() As Integer
        For i As Integer = 0 To WinArray.Length - 1
            If (_Xstatus And WinArray(i)) = WinArray(i) Then
                Return 1
            End If
        Next
        For i As Integer = 0 To WinArray.Length - 1
            If (_Ostatus And WinArray(i)) = WinArray(i) Then
                Return 2
            End If
        Next
        Return 0
    End Function

    Public Sub PrintBoard()
        Dim format As [String] = vbLf & " {0} | {1} | {2} " & vbLf
        Dim sb As New StringBuilder()
        For i As Integer = 0 To _caselle.Length - 1 Step 3
            sb.AppendFormat(format, _caselle(i), _caselle(i + 1), _caselle(i + 2))
        Next
        Console.WriteLine(sb.ToString())
    End Sub

    Public Function InputMove() As Integer
        Dim casella As Integer = 0
        Dim read As String = Nothing
        Do
            Try
                Console.Write("Your move: ")
                read = Console.ReadLine()
                casella = Int32.Parse(read)
            Catch ex As Exception
                Continue Do
            End Try
        Loop While casella < 0 OrElse casella > 10 OrElse _caselle((casella - 1)) <> read.ElementAt(0)
        Return casella
    End Function

    Public Shared Function InputResponse() As String
        Dim response As String = Nothing
        Do
            response = Console.ReadLine()
            If response.StartsWith("y") Then
                response = "yes"
            ElseIf response.StartsWith("n") Then
                response = "no"
            Else
                Console.Write("Not a valid response! (y/n): ")
                response = Nothing
            End If
        Loop While response Is Nothing

        Return response
    End Function

    Public Shared Function InputPlayer() As String
        Dim player As String = Nothing
        Do
            player = Console.ReadLine()
            If player.StartsWith("x") Then
                player = "x"
            ElseIf player.StartsWith("o") Then
                player = "o"
            Else
                Console.Write("Not a valid response! (o/x): ")
                player = Nothing
            End If
        Loop While player Is Nothing

        Return player
    End Function

End Class

