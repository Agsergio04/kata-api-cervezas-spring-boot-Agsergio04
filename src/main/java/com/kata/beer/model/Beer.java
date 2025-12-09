package com.kata.beer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@Entity
@Table(name = "beers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brewery_id", nullable = false)
    @JsonProperty("brewery")
    private Brewery brewery;

    @NotBlank(message = "El nombre de la cerveza es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_id", nullable = false)
    @JsonProperty("category")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "style_id", nullable = false)
    @JsonProperty("style")
    private Style style;

    @DecimalMin(value = "0.0", message = "El ABV debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", message = "El ABV debe ser menor o igual a 100")
    @Column(name = "abv", nullable = false)
    private Float abv;

    @DecimalMin(value = "0.0", message = "El IBU debe ser mayor o igual a 0")
    @Column(name = "ibu", nullable = false)
    private Float ibu;

    @DecimalMin(value = "0.0", message = "El SRM debe ser mayor o igual a 0")
    @Column(name = "srm", nullable = false)
    private Float srm;

    @Min(value = 0, message = "El UPC debe ser mayor o igual a 0")
    @Column(name = "upc", nullable = false)
    private Integer upc;

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

