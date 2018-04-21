package com.mateuszjanwojtyna.personaldeveloper.DTO;

import com.mateuszjanwojtyna.personaldeveloper.Annotations.UniqueUsername;
import com.mateuszjanwojtyna.personaldeveloper.Entities.User;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserData {

    private int id;

    @UniqueUsername
    @Size(min = 3, max = 255)
    private String username;

    @Size(min = 3, max = 255)//but after encryption it will be probably too much signs || check it always be sixty
    private String password;

    @Size(min = 3, max = 255)
    private String firstName;

    @Size(min = 3, max = 255)
    private String lastName;

    @Size(min = 3, max = 255)
    private String email;

    public UserData() {}

    public UserData(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
