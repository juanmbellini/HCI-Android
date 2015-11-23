package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

/**
 * Created by JuanMarcos on 23/11/15.
 */
public class Account {

    @Expose
    int id;
    @Expose
    String userName;
    @Expose
    String firstName;
    @Expose
    String lastName;
    @Expose
    String gender;
    @Expose
    String identityCard;
    @Expose
    String email;
    @Expose
    String birthDate;


    public Account(int id, String userName, String firstName, String lastName, String gender, String identityCard, String email, String birthDate) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.identityCard = identityCard;
        this.email = email;
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        return birthDate;
    }
}
