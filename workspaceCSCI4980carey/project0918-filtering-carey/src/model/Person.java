package model;

public class Person {
   private String  firstName;
   private String  lastName;
   private String  address;

   public Person() {
   }

   public Person(String firstName, String lastName, String address) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.address = address;
   }

   public String getFirstName() {
      return firstName;
   }

   public String getAddress() {
      return address;
   }

   public String getLastName() {
      return lastName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   @Override
   public String toString() {
      return firstName + " " + lastName;
   }
}
