package ru.shokhinsergey.springproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class UserDtoCreateAndUpdate {


    @Pattern(regexp = "^[A-Z][a-z]{1,24}$",
            message = "First letter of the parameter \"name\" must be uppercase," +
                    "followed - lowercase. Length must be less then 25 letters")
    private String name;


    @Email (message = "Entered data doesn't match the format of parameter \"email\"")
    private String email;


    @Min(value = 1, message = "Entered \"age\" must be more then 1")
    @Max(value = 111, message = "Entered \"age\" must be less then 111")
    private int age;

//    public UserDtoCreateAndUpdate() {
//    }

    public UserDtoCreateAndUpdate(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
