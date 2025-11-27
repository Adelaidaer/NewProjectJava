package org.kursach.kursach.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.kursach.kursach.model.Payment;
import org.kursach.kursach.model.Servise;

import java.util.List;

@ApplicationScoped
public class PaymentRepositoryImpl implements PaymentRepository {
    public PaymentRepositoryImpl() {}
    
    @Inject
    private EntityManager em;
    
    @Override
    public Payment findById(Long id) {
        return em.find(Payment.class, id);
    }
    
    @Override
    public List<Payment> findAll() {
        TypedQuery<Payment> query = em.createQuery("SELECT c FROM Payment c", Payment.class);
        return query.getResultList();
    }
    
    @Override
    public List<Payment> findByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return findAll();
        }
        
        String searchPattern = "%" + name.toLowerCase() + "%";
        TypedQuery<Payment> query = em.createQuery(
            "SELECT c FROM Payment c WHERE LOWER(c.namePayment) LIKE :name OR LOWER(c.shortNamePayment) LIKE :name", 
            Chair.class);
        query.setParameter("name", searchPattern);
        return query.getResultList();
    }
    
    @Override
    public List<Payment> findByServise(Servise servise) {
        return em.createQuery("SELECT c FROM Payment c WHERE c.faculty = :faculty", Payment.class)
                 .setParameter("servise", servise)
                 .getResultList();
    }
    
    @Override
    public void save(Payment payment) {
        boolean transactionActive = false;
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
                transactionActive = true;
            }
            
            if (payment.getId() == null) {
                em.persist(payment);
            } else {
                payment = em.merge(payment);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при сохранении способа оплаты", e);
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
            
            Payment payment = findById(id);
            if (payment != null) {
                em.remove(payment);
            }
            
            if (transactionActive) {
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (transactionActive && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Ошибка при удалении способа оплаты", e);
        }
    }
} 
