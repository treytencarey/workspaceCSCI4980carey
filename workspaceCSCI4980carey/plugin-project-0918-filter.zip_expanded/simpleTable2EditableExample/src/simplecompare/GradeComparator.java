package simplecompare;

import java.util.Comparator;

public class GradeComparator implements Comparator<Student> {
   @Override
   public int compare(Student o1, Student o2) {
      return o1.getGrade() - o2.getGrade();
   }
}
