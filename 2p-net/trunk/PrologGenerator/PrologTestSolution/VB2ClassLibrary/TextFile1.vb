Imports System
Imports alice.tuprolog

Namespace VB2ClassLibrary
    Public Class TextFile1
        Inherits Prolog

        Dim th As String = ""

        Public Sub New()
            Me.setTheory(New Theory(th))
        End Sub

    End Class

End Namespace
