package cat.uvic.teknos.coursemanagement.domain.jpa.repositories;

import cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaAddress;
import cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaStudent;
import cat.uvic.teknos.coursemanagement.models.Address;
import cat.uvic.teknos.coursemanagement.models.Student;
import cat.uvic.teknos.coursemanagement.repositories.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.Set;

public class JpaStudentRepository implements StudentRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaStudentRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    @Override
    public void save(Student model) {
        if (model.getId() == 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void update(Student model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(model);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    private void insert(Student model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(model);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(Student model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            // Find all students that reference this genre and set their genre to null
            Query query = entityManager.createNativeQuery("DELETE FROM STUDENT_COURSE WHERE STUDENT = ?");
            query.setParameter(1, model.getId());
            query.executeUpdate();
            if(model.getAddress() != null) {
                query = entityManager.createNativeQuery("DELETE FROM ADDRESS WHERE ID = ?");
                query.setParameter(1, model.getAddress().getId());
                query.executeUpdate();
            }
            // Now find and delete the genre
            JpaStudent managedStudent = entityManager.find(JpaStudent.class, model.getId());
            if (managedStudent != null) {
                entityManager.remove(managedStudent);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Student get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(JpaStudent.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Set<Student> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT g FROM JpaAddress g", JpaAddress.class);
            return Set.copyOf(query.getResultList());
        } finally {
            entityManager.close();
        }
    }
}
