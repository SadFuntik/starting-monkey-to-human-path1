package RPIS71.Funtikov.wdad.learn.xml;

import java.io.Serializable;
import java.util.Objects;

public class Officiant implements Serializable {

    private static final String DEFAULT_NAME = "self catering";

    private String firstName;
    private String secondName;

    public Officiant() {
    }

    public Officiant(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Officiant: ");
        if (firstName == null || firstName.isEmpty()) {
            sb.append(Objects.requireNonNullElse(secondName, DEFAULT_NAME));
        } else sb.append(firstName).append(" ").append(secondName);
        return sb.toString();
    }
}