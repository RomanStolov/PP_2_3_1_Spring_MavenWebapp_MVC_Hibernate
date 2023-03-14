package web.model;

import javax.persistence.*;

/**
 * Скрипт создания базы и таблицы в воркбенче:
 * CREATE DATABASE mydb_pp_2_3_1;
 * USE mydb_pp_2_3_1;
 * CREATE TABLE IF NOT EXISTS mydb_pp_2_3_1.users (
 * id BIGINT AUTO_INCREMENT,
 * name VARCHAR(25) NOT NULL,
 * surname VARCHAR(25) NOT NULL,
 * age TINYINT,
 * email VARCHAR(60),
 * CHECK((age > 0 AND age < 100) AND (name != '') AND (surname != '')),
 * PRIMARY KEY (id)
 * );
 *  Скрипт заполнения таблицы в воркбенче:
 * INSERT INTO users(name, surname, age, email) VALUES
 * ("Name-1", "Surname-1", 10, "email_1@mail.com"),
 * ("Name-2", "Surname-2", 20, "email_2@mail.com"),
 * ("Name-3", "Surname-3", 30, "email_3@mail.com"),
 * ("Name-4", "Surname-4", 40, "email_4@mail.com"),
 * ("Name-5", "Surname-5", 50, "email_51mail.com"),
 * ("Name-6", "Surname-6", 60, "email_6@mail.com"),
 * ("Name-7", "Surname-7", 70, "email_7@mail.com"),
 * ("Name-8", "Surname-8", 80, "email_8@mail.com"),
 * ("Name-9", "Surname-9", 90, "email_9@mail.com"),
 * ("Name-10", "Surname-10", 95, "email0_1@mail.com")
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false, length = 25)
    private String name;
    @Column(name = "surname", nullable = false, length = 25)
    private String surname;
    @Column(name = "age", nullable = true)
    private Byte age;
    @Column(name = "email", nullable = true)
    private String email;

    public User() {

    }

    public User(String name, String surname, Byte age, String email) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Byte getAge() {
        return age;
    }

    public void setAge(Byte age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }

}
