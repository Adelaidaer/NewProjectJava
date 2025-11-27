package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Servise;

import java.util.List;

@ApplicationScoped
public class ServiseRepositoryImpl implements ServiseRepository {
    public ServiseRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Servise findById(Long id) {
        return em.find(Servise.class, id);
    }
    
    @Override
    public List<Servise> findAll() {
        TypedQuery<Servise> query = em.createQuery("SELECT f FROM Servise f", Servise.class);
        return query.getResultList();
    }
    
    @Override
    public List<Servise> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Faculty> query = em.createQuery(
            "SELECT f FROM Servise f WHERE LOWER(f.nameServise) LIKE :name OR LOWER(f.shortNameServise) LIKE :name", 
            Faculty.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public void save(Servise servise) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (faculty.getId() == null) {
                em.persist(servise);
            } else {
                faculty = em.merge(servise);
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
            
            Servise servise = findById(id);
            if (faculty != null) {
                em.remove(servise);
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
