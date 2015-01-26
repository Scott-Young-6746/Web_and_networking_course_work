package med;

import java.io.Serializable;

public class Course implements Serializable {
	private String name;
	private String number;
	public Course(String name, String number){
		this.name=name;
		this.number=number;
	}
	public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}