package com.example.VAADINSPRINGFINAL.service;

import com.example.VAADINSPRINGFINAL.Entity.Customer;
import com.example.VAADINSPRINGFINAL.repository.CustomerRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route("manageClient")
public class ManageCustomer extends AppLayout implements HasUrlParameter<Integer> {

    Integer id;
    FormLayout contactForm;
    TextField firstName;
    TextField secondName;
    TextField fatherName;
    TextField numberPhone;
    TextField email;
    DatePicker birthDate;
    List<String> sexes;
    ComboBox<String> select;
    Button saveContact;

    @Autowired
    CustomerRepository customerRepository;

    public ManageCustomer() {
        // Создаем объекты для формы
        contactForm = new FormLayout();
        firstName = new TextField("Имя");
        secondName = new TextField("Фамилия");
        fatherName = new TextField("Отчество");
        birthDate = new DatePicker("Дата рождения");
        numberPhone = new TextField("Номер телефона");
        email = new TextField("Электронная почта");
        saveContact = new Button("Сохранить");
        sexes = new ArrayList<>();
        select = new ComboBox<>("Пол");
        sexes.add("Мужской");
        sexes.add("Женский");
        select.setItems(sexes);
        // Добавим все элементы на форму
        contactForm.add(firstName, secondName, fatherName, birthDate, select, numberPhone, email, saveContact);
        setContent(contactForm);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer contactId) {
        id = contactId;
        if (!id.equals(0)) {
            addToNavbar(new H3("Редактирование клиента"));
        } else {
            addToNavbar(new H3("Создание клиента"));
        }
        fillForm();
    }

    public void fillForm() {
        if (!id.equals(0)) {
            Optional<Customer> contact = customerRepository.findById(id);
            contact.ifPresent(x -> {
                firstName.setValue(x.getFirstName());
                secondName.setValue(x.getSecondName());
                fatherName.setValue(x.getFatherName());
                birthDate.setValue(x.getBirthDate());
                select.setValue(x.getSex());
                numberPhone.setValue(x.getNumberPhone());
                email.setValue(x.getEmail());
            });
        }
        saveContact.addClickListener(clickEvent -> {
            // Создадим объект контакта, получив значения с формы
            Customer customer = new Customer();
            if (!id.equals(0)) {
                customer.setId(id);
            }
            customer.setFirstName(firstName.getValue());
            customer.setSecondName(secondName.getValue());
            customer.setFatherName(fatherName.getValue());
            customer.setBirthDate(birthDate.getValue());
            customer.setSex(select.getValue());
            customer.setEmail(email.getValue());
            customer.setNumberPhone(numberPhone.getValue());
            customerRepository.save(customer);

            Notification notification =
                    new Notification(id.equals(0) ? "Клиент успешно создан" : "Клиент был изменен", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(CustomerList.class);
            });
            contactForm.setEnabled(false);
            notification.open();
        });
    }
}
