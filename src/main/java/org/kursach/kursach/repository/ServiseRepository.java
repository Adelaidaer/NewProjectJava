package org.kursach.kursach.repository;

import org.kursach.kursach.model.Servise;
import java.util.List;

/**
 * Интерфейс репозитория для работы 
 */
public interface ServiseRepository extends Repository<Servise, Long> {
    
    /**
     */
    List<Servise> findByName(String name);
} 
