package cat.uvic.teknos.coursemanagement.domain.fake.models;

import cat.uvic.teknos.coursemanagement.models.Address;
import cat.uvic.teknos.coursemanagement.models.Course;
import cat.uvic.teknos.coursemanagement.models.Genre;
import cat.uvic.teknos.coursemanagement.models.Student;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FakeStudent implements Student {
    private int id;
    private Genre genre;
    private Address address;
    private String firstName;
    private String lastName;
    private LocalDate bornOn;
    private Set<Course> courses = new HashSet<>();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Genre getGenre() {
        return genre;
    }

    @Override
    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public LocalDate getBornOn() {return bornOn;
    }

    @Override
    public void setBornOn(LocalDate bornOn) {
        this.bornOn = bornOn;
    }

    @Override
    public Set<Course> getCourses() {
        return courses;
    }

    @Override
    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }



    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void setAddress(Address address) {

    }
}
