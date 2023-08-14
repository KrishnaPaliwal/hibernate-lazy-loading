package com.lazy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateExample {

    public static void main(String[] args) {
        // Create session factory
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        // Create a session
        Session session = sessionFactory.openSession();
        try {
            // Begin a transaction
            session.beginTransaction();

            // Create a department
            Department department = new Department();
            department.setName("Sales");

            // Create employees
            Employee employee1 = new Employee();
            employee1.setName("John Doe");

            Employee employee2 = new Employee();
            employee2.setName("Jane Smith");

            // Associate employees with the department
            department.getEmployees().add(employee1);
            department.getEmployees().add(employee2);

            // Set department for employees
            employee1.setDepartment(department);
            employee2.setDepartment(department);

            // Persist the department (and its associated employees)
            session.persist(department);

            // Commit the transaction
            session.getTransaction().commit();
            session.close();

            Session session2 = sessionFactory.openSession();
            session2.beginTransaction();

            // Load department with lazy-loaded employees
            Department getDepartment = session2.get(Department.class, 1L);

            // Access employees (lazy loading will trigger here)
            getDepartment.getEmployees().forEach(employee -> System.out.println("Employee Name: " + employee.getName()));

            // Commit the transaction
            session2.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }
}
