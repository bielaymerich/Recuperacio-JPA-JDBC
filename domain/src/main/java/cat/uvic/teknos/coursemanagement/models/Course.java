package cat.uvic.teknos.coursemanagement.models;

import java.util.Set;

public interface Course {
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);
    int getYear();
    void setYear(int year);
    Set<Student> getStudents();
    void setStudents(Set<Student> students);
}
