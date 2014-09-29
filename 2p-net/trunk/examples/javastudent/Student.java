package javastudent;

public class Student
{
  private int id;
  private String name;
  private String surname;
  private int exams = 10;
  public String publicField = "Public Filed";
  public static String publicStaticField = "Public Static Filed";

  public Student(int id, String name, String surname)
  {
    this.id = id;
    this.name = name;
    this.surname = surname;
  }

  public String printStudent()
  {
    return this.id + this.name + this.surname;
  }

  public static String printInfoUni()
  {
    return "Alma Mater Studiorum - Bologna";
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return this.surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public void setExams(int exams) {
    this.exams = exams;
  }

  public int getExams() {
    return this.exams;
  }
}