package module4.Practice;

public class Student extends Person {
    private int studentId;

    public Student(String name, int studentId) {
        super(name);
        this.studentId = studentId;
    }

    public Student() {
        this("Student", 0);

    }

    public int getId() {
        return this.studentId;

    }

    public String toStringa() {
        return "Student Name: " + getName() + ", Student ID: " +
                Integer.toString(this.studentId);

    }

    public static void main(String[] args) {
        Person[] persons = new Person[2];
        persons[0] = new Student();
        persons[1] = new Person("aj");
        for (Person person : persons) {
            System.out.println(person);
        }

    }
}
