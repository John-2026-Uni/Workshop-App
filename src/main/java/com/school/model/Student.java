package com.school.model;

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String grade;

    public Student(int id, String firstName, String lastName, String email, String grade) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.grade = grade;
    }

    public int getId()            { return id; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getEmail()      { return email; }
    public String getGrade()      { return grade; }
    public String getFullName()   { return firstName + " " + lastName; }
}
