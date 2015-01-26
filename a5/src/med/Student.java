package med;

import java.io.Serializable;

public class Student implements Serializable  {
    private String name;
    private String number;
    private String email;
    private String address;

    public Student(String n, String a, String w, String h ) {
        name = n;
        number = a;
        email = w;
        address = h;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}
