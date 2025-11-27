package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Payment;
import org.kursach.kursach.model.Servise;
import org.kursach.kursach.repository.PaymentRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class ChairService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private PaymentRepository paymentRepository;
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public List<Payment> searchPaymentsByName(String name) {
        return paymentRepository.findByName(name);
    }
    
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
    
    public List<Payment> getPaymentsByFaculty(Servise servise) {
        return paymentRepository.findByServise(servise);
    }
    
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
    
    public void deletePayment(Long id) {
        paymentRepository.delete(id);
    }
} 
