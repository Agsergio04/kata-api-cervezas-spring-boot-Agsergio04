package com.kata.beer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "breweries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Brewery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre de la cervecería es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "address1", nullable = false)
    private String address1;

    @Size(max = 255)
    @Column(name = "address2", nullable = false)
    private String address2;

    @Size(max = 255)
    @Column(name = "city", nullable = false)
    private String city;

    @Size(max = 255)
    @Column(name = "state", nullable = false)
    private String state;

    @Size(max = 25)
    @Column(name = "code", nullable = false)
    private String code;

    @Size(max = 255)
    @Column(name = "country", nullable = false)
    private String country;

    @Size(max = 50)
    @Column(name = "phone", nullable = false)
    private String phone;

    @Size(max = 255)
    @Column(name = "website", nullable = false)
    private String website;

    @Size(max = 255)
    @Column(name = "filepath", nullable = false)
    private String filepath;

    @Column(name = "descript", columnDefinition = "TEXT", nullable = false)
    private String descript;

    @Column(name = "add_user", nullable = false)
    private Integer addUser = 0;

    @Column(name = "last_mod", nullable = false)
    private LocalDateTime lastMod;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastMod = LocalDateTime.now();
    }
}

