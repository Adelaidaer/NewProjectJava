package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Client;

import java.util.List;

@ApplicationScoped
public class ClientRepositoryImpl implements ClientRepository {
    public ClientRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Client findById(Long id) {
        return em.find(Client.class, id);
    }
    
    @Override
    public List<Client> findAll() {
        TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
        return query.getResultList();
    }
    
    @Override
    public List<Client> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Client> query = em.createQuery(
            "SELECT c FROM Client c WHERE LOWER(c.nameClient) LIKE :name OR LOWER(c.speciality) LIKE :name", 
            Client.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public List<Client> findByCourse(Integer course) {
        return em.createQuery("SELECT c FROM Client c WHERE c.course = :course", Client.class)
                 .setParameter("course", course)
                 .getResultList();
    }
    
    @Override
    public List<Client> findBySpeciality(String speciality) {
        return em.createQuery("SELECT c FROM Client c WHERE c.speciality LIKE :speciality", Client.class)
                 .setParameter("speciality", "%" + speciality + "%")
                 .getResultList();
    }
    
    @Override
    public void save(Client client) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (client.getId() == null) {
                em.persist(client);
            } else {
                client = em.merge(client);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении", e);
        }
    }
    
    @Override
    public void delete(Long id) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            Client client = findById(id);
            if (client != null) {
                em.remove(client);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении", e);
        }
    }
} 
