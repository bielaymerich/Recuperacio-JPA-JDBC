package cat.uvic.teknos.coursemanagement.domain.jpa.repositories;

import cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaCourse;
import cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaGenre;
import cat.uvic.teknos.coursemanagement.models.Course;
import cat.uvic.teknos.coursemanagement.models.Genre;
import cat.uvic.teknos.coursemanagement.repositories.CourseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.HashSet;
import java.util.Set;

public class JpaCourseRepository implements CourseRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaCourseRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Course model) {
        if (model.getId() == 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void update(Course model) {
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

    private void insert(Course model) {
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
    public void delete(Course model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Eliminar las referencias en la tabla de unión STUDENT_COURSE
            Query deleteReferencesQuery = entityManager.createNativeQuery(
                    "DELETE FROM STUDENT_COURSE WHERE COURSE = ?");
            deleteReferencesQuery.setParameter(1, model.getId());
            deleteReferencesQuery.executeUpdate();

            // Ahora encontrar y eliminar el curso
            JpaCourse managedCourse = entityManager.find(JpaCourse.class, model.getId());
            if (managedCourse != null) {
                entityManager.remove(managedCourse);
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
    public Course get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(JpaCourse.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Set<Course> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT c FROM JpaCourse c", JpaCourse.class);
            return Set.copyOf(query.getResultList());
        } finally {
            entityManager.close();
        }
    }
}
