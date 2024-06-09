package cat.uvic.teknos.coursemanagement.domain.jpa.models;

import cat.uvic.teknos.coursemanagement.models.Address;
import cat.uvic.teknos.coursemanagement.models.Course;
import cat.uvic.teknos.coursemanagement.models.Genre;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STUDENT")
public class JpaStudent implements cat.uvic.teknos.coursemanagement.models.Student, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @ManyToOne(targetEntity = cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaAddress.class)
    @JoinColumn(name = "ADDRESS")
    private Address address;

    @Column(name = "BORN_ON")
    private LocalDate bornOn;

    @ManyToOne(targetEntity = cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaGenre.class)
    @JoinColumn(name = "GENRE")
    private Genre genre;

    @ManyToMany(targetEntity = cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaCourse.class)
    @JoinTable(
            name = "STUDENT_COURSE",
            joinColumns = @JoinColumn(name = "STUDENT"),
            inverseJoinColumns = @JoinColumn(name = "COURSE")
    )
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
    public void setAddress(Address address) {this.address = address;

    }
}
