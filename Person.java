package PersonGui;

import java.io.Serializable;

public class Person implements Serializable {
    private String firstName;
    private String lastName;
    private OCCCDate birthDate;

    public Person() {
        this.firstName = "";
        this.lastName = "";
        this.birthDate = new OCCCDate(1, 1, 2000);
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = new OCCCDate();
    }

    public Person(String firstName, String lastName, OCCCDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = new OCCCDate(birthDate);
    }

    public Person(Person p) {
        this.firstName = p.firstName;
        this.lastName = p.lastName;
        this.birthDate = new OCCCDate(p.birthDate);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public OCCCDate getBirthDate() {
        return new OCCCDate(birthDate);
    }

    public void setBirthDate(OCCCDate birthDate) {
        this.birthDate = new OCCCDate(birthDate);
    }

    public int getAge() {
        return new OCCCDate().getDifferenceInYears(birthDate);
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName + " (" + birthDate.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Person)) return false;
        Person e = (Person) obj;
        return firstName.equalsIgnoreCase(e.firstName)
            && lastName.equalsIgnoreCase(e.lastName)
            && birthDate.equals(e.birthDate);
    }

    public void eat() {
        System.out.println("Person is eating.....");
    }

    public void sleep() {
        System.out.println("Person is sleeping.....");
    }

    public void play() {
        System.out.println("Person is playing.....");
    }

    public void run() {
        System.out.println("Person is running.....");
    }
}
