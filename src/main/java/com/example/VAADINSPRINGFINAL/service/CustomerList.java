package com.example.VAADINSPRINGFINAL.service;

import com.example.VAADINSPRINGFINAL.Entity.Customer;
import com.example.VAADINSPRINGFINAL.repository.CustomerRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
/**
 * Основная страница приложения с клиентами
 */
@Route("clients")
public class CustomerList extends AppLayout {

    //Вертикальное расположение элементов
    private VerticalLayout layout;
    //Отображение таблицы клиентов
    private Grid<Customer> grid;
    //Переход на страницу создания клиента
    private RouterLink linkCreate;

    //Подключение репозитория
    @Autowired
    CustomerRepository customerRepository;

    public CustomerList() {
        grid = new Grid<>();
        linkCreate = new RouterLink("Создать клиента", ManageCustomer.class, 0);
        addToNavbar(new H3("Список клиентов"));
        layout = new VerticalLayout();
        layout.add(linkCreate);
        layout.add(grid);
        setContent(layout);
    }

    /**
     * Заполнение таблицы
     */
    @PostConstruct
    public void fillGrid() {
        List<Customer> customers = customerRepository.findAll();
        if (!customers.isEmpty()) {
            // Вывод столбцов
            grid.addColumn(Customer::getFirstName).setHeader("Имя");
            grid.addColumn(Customer::getSecondName).setHeader("Фамилия");
            grid.addColumn(Customer::getFatherName).setHeader("Отчество");
            grid.addColumn(Customer::getBirthDate).setHeader("Дата рождения");
            grid.addColumn(Customer::getSex).setHeader("Пол");
            grid.addColumn(Customer::getNumberPhone).setHeader("Номер");
            grid.addColumn(Customer::getEmail).setHeader("E-mail");
            // Добавление кнопки редактирования
            grid.addColumn(new NativeButtonRenderer<Customer>("Редактировать", customer -> {
                UI.getCurrent().navigate(ManageCustomer.class, customer.getId());
            }));
            // Добавление кнопки удаления
            grid.addColumn(new NativeButtonRenderer<Customer>("Удалить", customer -> {
                customerRepository.delete(customer);
                Notification notification = new Notification("Клиент удален", 1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();
                grid.setItems(customerRepository.findAll());
            }));
            grid.setItems(customers);
        }
    }
}
