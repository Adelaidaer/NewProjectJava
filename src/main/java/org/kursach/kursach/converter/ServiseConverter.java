package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Servise;
import org.kursach.kursach.service.ServiseService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "serviseConverter", managed = true)
public class ServiseConverter implements Converter<Servise> {
    
    private static final Logger logger = Logger.getLogger(ServiseConverter.class.getName());
    
    @Inject
    private ServiseService serviseService;

    @Override
    public Servise getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            Long id = Long.valueOf(value);
            Servise servise = serviseService.getServiseById(id);
            logger.info("Converted string '" + value + "' to Servise: " + (servise != null ? servise.getNameServise() : "null"));
            return servise;
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse Servise ID from value: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Servise servise) {
        if (faculty == null) {
            logger.fine("Получен null объект в getAsString");
            return "";
        }
        
        try {
            // Защита от неправильного типа
            if (!(servise instanceof Servise)) {
                logger.warning("Ожидался объект Servise, но получен: " + servise.getClass().getName());
                return "";
            }
            
            if (servise.getId() == null) {
                logger.warning("Servise с null ID: " + servise.getNameServise());
                return "";
            }
            
            String result = servise.getId().toString();
            logger.info("Конвертирован сервис '" + faculty.getNameServise() + "' в строку: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Ошибка в getAsString: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
} 
