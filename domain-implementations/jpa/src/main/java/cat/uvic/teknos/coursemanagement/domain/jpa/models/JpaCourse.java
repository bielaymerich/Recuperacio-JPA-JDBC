package cat.uvic.teknos.coursemanagement.domain.jpa.models;

import cat.uvic.teknos.coursemanagement.models.Course;
import cat.uvic.teknos.coursemanagement.models.Student;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COURSE")
public class JpaCourse implements Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "YEAR")
    private int year;

    @Column(name = "NAME")
    private String name;

    @ManyToMany(targetEntity = JpaStudent.class)
    @JoinTable(
            name = "STUDENT_COURSE",
            joinColumns = @JoinColumn(name = "COURSE"),
            inverseJoinColumns = @JoinColumn(name = "STUDENT")
    )
    private Set<Student> students = new HashSet<>();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public Set<Student> getStudents() {
        return students;
    }

    @Override
    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
