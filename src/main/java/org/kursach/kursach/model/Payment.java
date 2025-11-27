package org.kursach.kursach.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "id_servise")
    private Servise servise;
    
    @Column(name = "name_payment")
    private String namePayment;
    
    @Column(name = "short_name_payment")
    private String shortNamePayment;
    
    
    // Конструкторы
    public Payment() {
    }
    
    public Payment(Faculty faculty, String namePayment, String shortNamePayment) {
        this.servise = servise;
        this.namePayment = namePayment;
        this.shortNamePayment = shortNamePayment;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Servise getServise() {
        return servise;
    }

    public void setServise(Servise servise) {
        this.servise = servise;
    }

    public String getNameChair() {
        return nameChair;
    }

    public void setNamePayment(String namePayment) {
        this.namePayment = namePayment;
    }

    public String getShortNamePayment() {
        return shortNamePayment;
    }

    public void setShortNamePayment(String shortNamePayment) {
        this.shortNamePayment = shortNamePayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Payment chair = (Payment) o;
        return id != null ? id.equals(chair.id) : chair.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", servise=" + (servise != null ? servise.getShortNameSrvise() : "null") +
                ", namePayment='" + namePayment + '\'' +
                ", shortNamePayment='" + shortNamePayment + '\'' +
                '}';
    }
} 
