package com.condocker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity()
@Table(name="medianos")
@Data//getters y setters
@NoArgsConstructor// constructor vacio
@AllArgsConstructor//constructor completo
public class Mediano {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique=true)
    private String name;

    private long height;

    private String email;

//    @OneToMany(mappedBy = "mediano",cascade = CascadeType.ALL,
//            orphanRemoval = true)
//    @JsonIgnore
//    private Set<Photo> photos;
}