package com.spring.ivannikov.project2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "person")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Data
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Обязательное поле")
    @Size(min = 2, max = 100, message = "Имя должно быть длиной от 2 до 100 символов")
    @Column(name = "full_name")
    private String fullName;
    @Min(value = 1900, message = "Год рождения должен быть больше, чем 1900")
    @Column(name = "year_of_birth")
    private Integer yearOfBirth;
    @OneToMany(mappedBy = "person")
    private List<Book> books;



}
