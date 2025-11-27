package org.kursach.kursach.repository;

import org.kursach.kursach.model.Client;
import java.util.List;

/**
 * Интерфейс репозитория 
 */
public interface ClientRepository extends Repository<Client, Long> {
    
    /**
     */
    List<Curriculum> findByName(String name);
    
    /**
     */
    List<Curriculum> findByCourse(Integer course);
    
    /**
     */
    List<Curriculum> findBySpeciality(String speciality);
} 
