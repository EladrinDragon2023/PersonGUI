package PersonGui;

import java.io.Serializable;


public class OCCCPerson extends RegisteredPerson implements Serializable{

    private String studentId;

    public OCCCPerson(RegisteredPerson p, String studentId){
        super(p);
        this.studentId = studentId;
    }

    public OCCCPerson(String firstName, String lastName, String govId, String studentId){
        super(firstName, lastName, govId);
        this.studentId = studentId;
    }

    public OCCCPerson(OCCCPerson p){
        super(p);
        this.studentId = p.getStudentId();
    }

    public OCCCPerson(){
        super();
        this.studentId = "";
    }

    public String getStudentId(){
        return studentId;
    }

    public boolean equals(OCCCPerson e){
        return super.equals(e) && this.studentId.equals(e.getStudentId());
    }

    public boolean equals(RegisteredPerson e){
        return super.equals(e);
    }

    public boolean equals(Person e){
        return super.equals(e);
    }

    public void eat()
    {
        System.out.println("OCCC Person is eating.....");
    }

    public void sleep()
    {
        System.out.println("OCCC Person is sleeping.....");
    }

    public void play()
    {
        System.out.println("OCCC Person is playing.....");
    }

    public void run()
    {
        System.out.println("OCCC Person is running.....");
    }

    public String toString(){
        return super.toString() + " {" + studentId + "}";
    }

    public void setStudentId(String studentId){
        this.studentId = studentId;
    }
    

    

}
