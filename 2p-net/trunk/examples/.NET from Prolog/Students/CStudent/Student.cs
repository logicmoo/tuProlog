namespace CStudent
{

    public class Student
    {
        private string _Name;
        private string _SecondName;
        private int _Matricola;
        private int _exams = 10;
        public string publicField = "Public Filed";
        private static string _staticProp = "Static Property";
	    public static string publicStaticField = "Public Static Filed";

        public Student(int matricola, string name, string secondName)
        {
            _Name = name;
            _SecondName = secondName;
            _Matricola = matricola;
        }
        public string Name
        {
            get { return _Name; }
            set { _Name = value; }
        }

        public string Surname
        {
            get { return _SecondName; }
            set { _SecondName = value; }
        }

        public int Id
        {
            get { return _Matricola; }
            set { _Matricola = value; }
        }

        public int Exams
        {
            get
            {
                return this._exams;
            }
            set
            {
                this._exams = value;
            }
        }

        public static string StaticProperty
        {
            get
            {
                return Student._staticProp;
            }
            set
            {
                Student._staticProp = value;
            }
        }

        public override string ToString()
        {
            return Name + " " + Surname + " " + Id.ToString();
        }

        public static string PrintInfoUni()
        {
            return "Alma Mater Studiorum - Bologna";
        }

        public string PrintStudent()
        {
            return this.Id + this.Name + this.Surname;
        }

        public string SignExam(int grade)
        {
            if (grade == 30)
            {
                return "Good job!";
            }
            return "Keep studying!";
        }
    }

}