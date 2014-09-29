Namespace VBStudent
    Public Class Student
        Private _id As Integer

        Private _name As String

        Private _surname As String

        Private _exams As Integer

        Private Shared _staticProp As String = "Static Prop"

        Public publicField As String

        Public Shared publicStaticField As String = "Static Field"

        Public Shared Property StaticProperty() As String
            Get
                Return Student._staticProp
            End Get
            Set(value As String)
                Student._staticProp = value
            End Set
        End Property

        Public Property Id() As Integer
            Get
                Return Me._id
            End Get
            Set(value As Integer)
                Me._id = value
            End Set
        End Property

        Public Property Name() As String
            Get
                Return Me._name
            End Get
            Set(value As String)
                Me._name = value
            End Set
        End Property

        Public Property Surname() As String
            Get
                Return Me._surname
            End Get
            Set(value As String)
                Me._surname = value
            End Set
        End Property

        Public Property Exams() As Integer
            Get
                Return Me._exams
            End Get
            Set(value As Integer)
                Me._exams = value
            End Set
        End Property

        Public Sub New(id As Integer, name As String, surname As String)
            Me._exams = 10
            Me.publicField = "Public Field"
            Me.Id = id
            Me.Name = name
            Me.Surname = surname
        End Sub

        Public Function PrintStudent() As String
            Return Me.Id.ToString() + Me.Name + Me.Surname
        End Function

        Public Shared Function PrintInfoUni() As String
            Return "Alma Mater Studiorum - Bologna"
        End Function
    End Class
End Namespace