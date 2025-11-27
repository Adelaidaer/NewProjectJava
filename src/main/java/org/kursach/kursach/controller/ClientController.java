package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Payment;
import org.kursach.kursach.model.Client;
import org.kursach.kursach.model.Servise;
import org.kursach.kursach.service.PaymentService;
import org.kursach.kursach.service.ClientService;
import org.kursach.kursach.service.ServiseService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ClientController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ClientController.class.getName());
    
    @Inject
    private ClientService clientService;
    
    @Inject
    private ServiseService serviseService;
    
    @Inject
    private PaymentService paymentService;
    
    private List<Client> client;
    private Client client;
    private String searchSpeciality;
    private Integer searchCourse;
    private Long selectedFacultyId;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        clients = clientService.getAllClients();
        
        // Создаем новый объект только если текущий объект null
        if (client == null) {
            client = new Client();
            logger.info("Создан новый объект Client");
        } else {
            logger.info("Использован существующий объект Client с ID: " + client.getId());
        }
        
        // Пробуем загрузить из параметра
        loadClient();
        
        logger.info("ClientController инициализирован");
    }
    
    public String save() {
        try {
            // Проверяем, есть ли ID для редактирования
            if (editId != null && client.getId() == null) {
                client.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            logger.info("Сохранение нового клиента: " + client.getNameClient());
            
            if (selectedServiseId != null) {
                logger.info("Выбран сервис с ID: " + selectedServiseId);
                // Можно дополнительно что-то сделать
            }
            
            clientService.saveClient(client);
            logger.info("Новый клиент сохранен успешно");
            
            // Сбрасываем editId
            editId = null;
            
            // Обновляем список 
            client = clientService.getAllClients();
            
            // Создаем новый объект для формы
            client = new Client();
            selectedServiceId = null;
            
            return "client?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении нового клиента: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование клиента с ID: " + id);
            client = clientService.getClientById(id);
            
            if (client == null) {
                logger.warning("Клиент с ID " + id + " не найден");
                return "client?faces-redirect=true";
            }
            
            
            // Сохраняем ID для отслеживания
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            // Передаем ID как параметр в URL
            return "client-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при информации о клиенте для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "client?faces-redirect=true";
        }
    }
    
    // Загрузка из параметра URL
    public void loadClient() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка информации о клиенте из параметра URL, ID: " + id);
                
                client = clientService.getClientById(id);
                if (client == null) {
                    logger.warning("Учебный план с ID " + id + " не найден при загрузке из параметра");
                    client = new Client();
                } else {
                    logger.info("Учебный план загружен из параметра URL: " + client.getNameClient());
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                client = new Client();
            }
        }
    }
    
    public String delete(Long id) {
        try {
            logger.info("Удаление информации о клиенте с ID: " + id);
            clientService.deleteClient(id);
            clients = curriculumService.getAllClients();
            return "client?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при удалении: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String prepareNew() {
        client = new Client();
        selectedFacultyId = null;
        editId = null; // Сбрасываем ID при создании 
        logger.info("Подготовка новой информации о клиенте");
        return "client-edit?faces-redirect=true";
    }
    
    public void search() {
        logger.info("Поиск клиентов по имени: " + searchTerm);
        clients = clientService.searchClientsByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска учебных планов");
        searchTerm = null;
        clients = clientService.getAllClients();
    }
    
    // Геттеры и сеттеры
    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getSearchSpeciality() {
        return searchSpeciality;
    }

    public void setSearchSpeciality(String searchSpeciality) {
        this.searchSpeciality = searchSpeciality;
    }

    public Integer getSearchCourse() {
        return searchCourse;
    }

    public void setSearchCourse(Integer searchCourse) {
        this.searchCourse = searchCourse;
    }
    
    public Long getSelectedFacultyId() {
        return selectedFacultyId;
    }
    
    public void setSelectedFacultyId(Long selectedFacultyId) {
        this.selectedFacultyId = selectedFacultyId;
    }
    
    public Long getEditId() {
        return editId;
    }
    
    public void setEditId(Long editId) {
        this.editId = editId;
    }
    
    public List<Payment> getPayments() {
        return paymentService.getAllPayments();
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
