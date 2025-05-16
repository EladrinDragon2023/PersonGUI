package PersonGui;

import java.io.Serializable;

public class RegisteredPerson extends Person implements Serializable{

    private String governmentId;

    public RegisteredPerson(String firstName, String lastName, String govId){
        super(firstName, lastName);
        this.governmentId = govId;
    }

    public RegisteredPerson(Person p, String govId){
        super(p);
        this.governmentId = govId;
    }

    public RegisteredPerson(RegisteredPerson p){
        super(p);
        this.governmentId = p.getGovernmentId();
    }

    public RegisteredPerson(){
        super();
        this.governmentId = "";
    }

    public String getGovernmentId(){
        return governmentId;
    }

    public boolean equals(RegisteredPerson e){
        return super.equals(e) && this.governmentId.equals(e.getGovernmentId());
    }

    public boolean equals(Person e){
        return super.equals(e);
    }

    public String toString(){
        return super.toString() + " [" + governmentId + "]";
    }

    public void eat()
    {
        System.out.println("Registered Person is eating.....");
    }

    public void sleep()
    {
        System.out.println("Registered Person is sleeping.....");
    }

    public void play()
    {
        System.out.println("Registered Person is playing.....");
    }

    public void run()
    {
        System.out.println("Registered Person is running.....");
    }

    public void setGovernmentId(String governmentId) {
        this.governmentId = governmentId;
    }
    
    
}
