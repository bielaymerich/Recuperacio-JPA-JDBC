package cat.uvic.teknos.coursemanagement.domain.jpa.repositories;

import cat.uvic.teknos.coursemanagement.domain.jpa.models.JpaAddress;
import cat.uvic.teknos.coursemanagement.models.Address;
import cat.uvic.teknos.coursemanagement.repositories.AddressRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.util.Set;

public class JpaAddressRepository implements AddressRepository {
    private final EntityManagerFactory entityManagerFactory;

    public JpaAddressRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Address model) {
        if (model.getId() == 0) {
            insert(model);
        } else {
            update(model);
        }
    }

    private void update(Address model) {
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

    private void insert(Address model) {
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
    public void delete(Address model) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Find all students that reference this address and set their address to null
            Query query = entityManager.createQuery("UPDATE JpaStudent s SET s.address = null WHERE s.address.id = :addressId");
            query.setParameter("addressId", model.getId());
            query.executeUpdate();

            // Now find and delete the address
            JpaAddress managedAddress = entityManager.find(JpaAddress.class, model.getId());
            if (managedAddress != null) {
                entityManager.remove(managedAddress);
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
    public Address get(Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(JpaAddress.class, id);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Set<Address> getAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT g FROM JpaAddress g", JpaAddress.class);
            return Set.copyOf(query.getResultList());
        } finally {
            entityManager.close();
        }
    }
}