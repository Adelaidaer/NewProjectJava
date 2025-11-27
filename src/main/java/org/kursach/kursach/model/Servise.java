package org.kursach.kursach.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "servise")
public class Servise implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name_servise")
    private String nameServise;
    
    @Column(name = "short_name_servise")
    private String shortNameServise;
    
    @OneToMany(mappedBy = "servise")
    private List<Client> clients;
    
    // Конструкторы
    public Servise() {
    }
    
    public Servise(String nameFaculty, String shortNameServise) {
        this.nameServise = nameServise;
        this.shortNameServise = shortNameServise;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameServise() {
        return nameServise;
    }

    public void setNameServise(String nameServise) {
        this.nameServise = nameServise;
    }

    public String getShortNameServise() {
        return shortNameServise;
    }

    public void setShortNameServise(String shortNameServise) {
        this.shortNameServise = shortNameServise;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setChairs(List<Client> clients) {
        this.clients = clients;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Servise servise = (Servise) o;
        return id != null ? id.equals(servise.id) : servise.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Servise{" +
                "id=" + id +
                ", nameServise='" + nameServise + '\'' +
                ", shortNameServise='" + shortNameServise + '\'' +
                '}';
    }
} 
