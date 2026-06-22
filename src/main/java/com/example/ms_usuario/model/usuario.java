package com.example.ms_usuario.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "usuarios")

@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor

public class usuario {

    @Id

    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Long id;

    @Column(nullable = false)

    private String nombre;

    @Column(
            nullable = false,
            unique = true
    )

    private String email;

    @Column(nullable = false)

    private String password;

    @Column(nullable = false)

    private String rol;
}