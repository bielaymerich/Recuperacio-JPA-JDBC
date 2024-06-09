package cat.uvic.teknos.coursemanagement.domain.jpa.repositories;

import cat.uvic.teknos.coursemanagement.models.Student;
import cat.uvic.teknos.coursemanagement.repositories.StudentRepository;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;

public class JpaStudentRepository implements StudentRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaStudentRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    @Override
    public void save(Student model) {
        
    }

    @Override
    public void delete(Student model) {

    }

    @Override
    public Student get(Integer id) {
        return null;
    }

    @Override
    public Set<Student> getAll() {
        return Set.of();
    }
}
