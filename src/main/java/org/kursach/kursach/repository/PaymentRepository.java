package org.kursach.kursach.repository;

import org.kursach.kursach.model.Payment;
import org.kursach.kursach.model.Servise;

import java.util.List;

/**
 * Интерфейс репозитория
 */
public interface PaymentRepository extends Repository<Payment, Long> {
    
    /**
     * Найти способ оплаты по названию (поиск по части названию)
     * @param name часть имени для поиска
     * @return список найденных способов оплаты
     */
    List<Payment> findByName(String name);
    
    List<Payment> findByServise(Servise servise);
} 
