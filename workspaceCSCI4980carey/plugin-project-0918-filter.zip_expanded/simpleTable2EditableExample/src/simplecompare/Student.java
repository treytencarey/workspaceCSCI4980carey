package simplecompare;

public class Student implements Comparable<Student> {
   String name;
   int    age;
   int    grade;

   public Student(String n, int a, int g) {
      this.name = n;
      this.age = a;
      this.grade = g;
   }

   public int getGrade() {
      return this.grade;
   }

   @Override
   public int compareTo(Student o) {
      return this.name.compareTo(o.name);
   }

   public String toString() {
      return name + ", " + age + ", " + grade;
   }
}
