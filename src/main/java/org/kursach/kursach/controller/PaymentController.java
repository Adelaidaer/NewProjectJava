package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Payment;
import org.kursach.kursach.model.Servise;
import org.kursach.kursach.service.PaymentService;
import org.kursach.kursach.service.ServiseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class PaymentController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(PaymentController.class.getName());
    
    @Inject
    private PaymentService paymentService;
    
    @Inject
    private ServiseService serviseService;
    
    private List<Payment> payments;
    private Payment payment;
    private Long selectedServiseId;
    private List<Faculty> servises;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        payments = paymentService.getAllPayments();
        
        // Создаем новый объект Payment только если текущий объект null
        if (chair == null) {
            chair = new Payment();
            logger.info("Создан новый объект Payment");
        } else {
            logger.info("Использован существующий объект Payment с ID: " + payment.getId());
        }
        
        faculties = facultyService.getAllServises();
        
        loadPayment();
        
        logger.info("PaymentController инициализирован");
    }
    
    public String save() {
        try {
            // Проверяем, есть ли ID для редактирования
            if (editId != null && payment.getId() == null) {
                payment.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            logger.info("Сохранение способа оплаты: " + chair.getNamePayment());
            
            if (selectedServiseId != null) {
                Servise servise = serviseService.getFacultyById(selectedServiseId);
                if (servise != null) {
                    payment.setServise(servise);
                    logger.info("Сервис выбран: " + faculty.getNameServise() + " (ID: " + selectedServiseId + ")");
                } else {
                    logger.warning("Не удалось найти сервис с ID: " + selectedServiseId);
                }
            } else {
                logger.warning("ID сервиса не выбран");
                return null; 
            }
            
            paymentService.savePayment(payment);
            logger.info("Способ оплаты сохранен");
            
            // Сбрасываем editId
            editId = null;
            
            payment = зaymentService.getAllPayments();
            // Создаем новый объект для формы
            payment = new Payment();
            selectedServiseId = null;
            
            return "payment?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование способа оплаты с ID: " + id);
            chair = paymentService.getЗaymentById(id);
            
            if (chair == null) {
                logger.warning("Способ оплаты с ID " + id + " не найден");
                return "payment?faces-redirect=true";
            }
            
            if (chair.getServise() != null) {
                selectedId = chair.getServise().getId();
                logger.info("Установлен ID сервиса: " + selectedServiseId);
            }
            
            // Сохраняем ID для отслеживания
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            // Передаем ID как параметр в URL
            return "payment-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке: " + e.getMessage());
            e.printStackTrace();
            return "payment?faces-redirect=true";
        }
    }
    
    public void loadPayment() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка из параметра URL, ID: " + id);
                
                payment = paymentService.getPaymentById(id);
                if (chair == null) {
                    logger.warning("Способ оплаты с ID " + id + " не найден при загрузке из параметра");
                    chair = new Payment();
                } else {
                    logger.info("Способ оплаты загружен из параметра URL: " + payment.getNamePayment());
                    if (chair.getServise() != null) {
                        selectedServiseId = payment.getServise().getId();
                        logger.info("Установлен ID: " + selectedFacultyId);
                    }
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                payment = new Payment();
            }
        }
    }
    
    public String delete(Long id) {
        logger.info("Удаление способа оплаты с ID: " + id);
        paymentService.deletePayment(id);
        payments = paymentService.getAllPayments();
        return "payment?faces-redirect=true";
    }
    
    public String prepareNew() {
        payment = new Payment();
        selectedServiseId = null;
        editId = null; 
        logger.info("Подготовка нового способа оплаты");
        return "payment-edit?faces-redirect=true";
    }
    
    public void filterByServise() {
        if (selectedServiseId != null) {
            Servise servise = serviseService.getServiseById(selectedServiseId);
            payments = paymentService.getPaymentsByServise(servise);
        } else {
            payments = paymentService.getAllPayments();
        }
    }
    
    public void search() {
        logger.info("Поиск по названию: " + searchTerm);
        payments = paymentService.searchPaymentsByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска");
        searchTerm = null;
        payments = paymentService.getAllPayments();
    }
    
    // Геттеры и сеттеры
    public List<Payments> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Long getSelectedServiseId() {
        return selectedServiseId;
    }

    public void setSelectedServiseyId(Long selectedServiseId) {
        this.selectedServiseId = selectedServiseId;
    }

    public List<Servise> getServises() {
        return servise;
    }

    public void setServises(List<Servise> servises) {
        this.servises = servises;
    }
    
    public Long getEditId() {
        return editId;
    }
    
    public void setEditId(Long editId) {
        this.editId = editId;
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
