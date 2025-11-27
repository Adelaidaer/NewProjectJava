package org.kursach.kursach.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.kursach.kursach.model.Servise;
import org.kursach.kursach.service.ServiseService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ServiseController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ServiseController.class.getName());
    
    @Inject
    private ServiseService serviseService;
    
    private List<Service> servises;
    private Service servise;
    private Long editId;
    private String searchTerm;
    
    @PostConstruct
    public void init() {
        servises = serviseService.getAllServices();
        
        // Создаем новый Servise только если текущий объект null
        if (servise == null) {
            servise = new Servise();
            logger.info("Создан новый объект Servise");
        } else {
            logger.info("Использован существующий объект Servise с ID: " + servise.getId());
        }
        
        // Пробуем загрузить из параметра
        loadServise();
        
        logger.info("ServiseController инициализирован");
    }
    
    public String save() {
        try {
            // Проверяем, есть ли ID для редактирования
            if (editId != null && servise.getId() == null) {
                servise.setId(editId);
                logger.info("Используется ID из editId: " + editId);
            }
            
            boolean isNew = (servise.getId() == null);
            logger.info((isNew ? "Создание нового" : "Обновление существующего") + " сервиса: " + servise.getNameFaculty());
            
            if (!isNew) {
                logger.info("ID редактируемого сервиса: " + servise.getId());
            }
            
            // Добавляем логирование ID перед сохранением
            logger.info("Перед сохранением сервиса - ID: " + servise.getId() + 
                         ", Название: " + servise.getNameServise() + 
                         ", Короткое название: " + servise.getShortNameServise());
            
            serviseService.saveServise(servise);
            
            // Логируем ID после сохранения
            logger.info("После сохранения - статус операции: успешно");
            
            // Сбрасываем editId
            editId = null;
            
            // Обновляем список 
            servise = facultyService.getAllServises();
            // Создаем новый объект для формы
            servise = new Servise();
            
            logger.info("Сервис " + (isNew ? "создан" : "обновлен") + " успешно");
            return "servise?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при сохранении сервиса: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public String edit(Long id) {
        try {
            logger.info("Редактирование сервиса с ID: " + id);
            servise = serviseService.getServiseById(id);
            
            if (servise == null) {
                logger.warning("Сервис с ID " + id + " не найден");
                return "servise?faces-redirect=true";
            }
            
            // Сохраняем ID для отслеживания
            this.editId = id;
            logger.info("Установлен editId: " + this.editId);
            
            // Передаем ID как параметр в URL
            return "servise-edit?faces-redirect=true&id=" + id;
        } catch (Exception e) {
            logger.severe("Ошибка при загрузке сервиса для редактирования: " + e.getMessage());
            e.printStackTrace();
            return "servise?faces-redirect=true";
        }
    }
    
    // Загрузка из параметра URL
    public void loadFaculty() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String idParam = params.get("id");
        
        if (idParam != null && !idParam.isEmpty()) {
            try {
                Long id = Long.valueOf(idParam);
                this.editId = id;
                logger.info("Загрузка сервиса из параметра URL, ID: " + id);
                
                servise = serviseService.getServiseById(id);
                if (servise == null) {
                    logger.warning("Сервис с ID " + id + " не найден при загрузке из параметра");
                    servise = new Servise();
                } else {
                    logger.info("Сервис загружен из параметра URL: " + servise.getNameServise());
                }
            } catch (NumberFormatException e) {
                logger.warning("Некорректный ID в параметре: " + idParam);
                servise = new Servise();
            }
        }
    }
    
    public String delete(Long id) {
        try {
            logger.info("Удаление ъервиса с ID: " + id);
            serviseService.deleteServise(id);
            servises = serviseService.getAllServises();
            return "servise?faces-redirect=true";
        } catch (Exception e) {
            logger.severe("Ошибка при удалении сервиса: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void search() {
        logger.info("Поиск сервисов по названию: " + searchTerm);
        servises = serviseService.searchServisesByName(searchTerm);
    }
    
    public void resetSearch() {
        logger.info("Сброс поиска сервисов");
        searchTerm = null;
        servises = serviseService.getAllServises();
    }
    
    public String prepareNew() {
        servise = new Servise();
        editId = null; // Сбрасываем ID при создании нового факультета
        logger.info("Подготовка новой формы сервиса");
        // Перенаправляем без параметра id
        return "servise-edit?faces-redirect=true";
    }
    
    // Геттеры и сеттеры
    public List<Servise> getServises() {
        return servises;
    }

    public void setServises(List<Servise> servises) {
        this.servises = servises;
    }

    public Servise getServise() {
        return servise;
    }

    public void setServise(Servise servise) {
        this.servise = servise;
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
