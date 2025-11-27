package org.kursach.kursach.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Payment;
import org.kursach.kursach.service.PaymentService;

import java.util.logging.Logger;

@Named
@ApplicationScoped
@FacesConverter(value = "chairConverter", managed = true)
public class PaymentConverter implements Converter<Payment> {
    
    private static final Logger logger = Logger.getLogger(PaymentConverter.class.getName());
    
    @Inject
    private PaymentService paymentService;

    @Override
    public Payment getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            Long id = Long.valueOf(value);
            Payment payment = paymentService.getPaymentById(id);
            logger.info("Converted string '" + value + "' to Payment: " + (payment != null ? payment.getNamePayment() : "null"));
            return payment;
        } catch (NumberFormatException e) {
            logger.warning("Failed to parse Payment ID from value: " + value);
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Payment payment) {
        if (payment == null) {
            logger.fine("Получен null объект в getAsString");
            return "";
        }
        
        try {
            // Защита от неправильного типа
            if (!(payment instanceof Payment)) {
                logger.warning("Ожидался объект Payment, но получен: " + chair.getClass().getName());
                return "";
            }
            
            if (payment.getId() == null) {
                logger.warning("Payment с null ID: " + chair.getNamePayment());
                return "";
            }
            
            String result = payment.getId().toString();
            logger.info("Конвертирована оплата '" + chair.getNameChair() + "' в строку: " + result);
            return result;
        } catch (Exception e) {
            logger.severe("Ошибка в getAsString: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
} 
