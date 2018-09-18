package simplecompare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
   public static void main(String[] args) {
      Student s1 = new Student("NameC", 21, 91);
      Student s2 = new Student("NameB", 23, 93);
      Student s3 = new Student("NameA", 22, 92);
      List<Student> list = new ArrayList<Student>();
      list.add(s1);
      list.add(s2);
      list.add(s3);
      System.out.println("[DBG] before sorting");
      print(list);

      Collections.sort(list);
      System.out.println("[DBG] sorting by name");
      print(list);

      Collections.sort(list, new GradeComparator());
      System.out.println("[DBG] sorting by grade");
      print(list);
   }

   static void print(List<Student> list) {
      for (Student student : list) {
         System.out.println(student);
      }
      System.out.println("------------------------");
   }
}
