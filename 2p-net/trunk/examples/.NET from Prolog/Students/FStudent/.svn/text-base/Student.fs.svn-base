namespace FStudent

type Student(id : int, name : string, surname : string) = 
    member val Id = id with get, set
    member val Exams = 10 with get, set 
    member val Name = name with get, set
    member val Surname = surname with get, set

    member s.PrintInfoUni() =
        "Alma Mater Studiorum - Bologna"

    member s.PrintInfoStudent() =
        s.Id.ToString() + s.Name + s.Surname

    member s.SignExam(grade) =
        if grade >= 30 then
            "YHEA!"
        else
            "Can do better"
