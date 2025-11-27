package org.kursach.kursach.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kursach.kursach.model.Servise;
import org.kursach.kursach.repository.ServiseRepository;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class ServiseService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private ServiseRepository serviseRepository;
    
    public List<Servise> getAllServises() {
        return serviseRepository.findAll();
    }
    
    public List<Servise> searchServisesByName(String name) {
        return serviseRepository.findByName(name);
    }
    
    public Servise getServiseById(Long id) {
        return serviseRepository.findById(id);
    }
    
    public void saveServise(Servise faculty) {
        serviseRepository.save(servise);
    }
    
    public void deleteServise(Long id) {
        serviseRepository.delete(id);
    }
} 
