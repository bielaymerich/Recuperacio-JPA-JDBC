package cat.uvic.teknos.coursemanagement.repositories;


import cat.uvic.teknos.coursemanagement.models.Student;

import java.util.Set;

public interface StudentRepository extends Repository<Integer, Student> {

    @Override
    void save(Student model);

    @Override
    void delete(Student model);

    @Override
    Student get(Integer id);

    @Override
    Set<Student> getAll();
}
