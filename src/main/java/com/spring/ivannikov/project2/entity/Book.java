package com.spring.ivannikov.project2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Обязательное поле")
    @Size(min = 2, max = 100, message = "Название книги должно быть длиной от 2 до 100 символов")
    @Column(name = "author")
    private String author;
    @NotEmpty(message = "Обязательное поле")
    @Size(min = 2, max = 100, message = "Название книги должно быть длиной от 2 до 100 символов")
    @Column(name = "name")
    private String name;
    @Min(value = 1500, message = "Год должен быть больше, чем 1500")
    @Column(name = "year_of_publishing")
    private Integer yearOfPublishing;
    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;
    @Column(name = "take_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takeAt;
    @Transient
    private Boolean expired;

    public boolean isExpired(){
        return expired;
    }
}
