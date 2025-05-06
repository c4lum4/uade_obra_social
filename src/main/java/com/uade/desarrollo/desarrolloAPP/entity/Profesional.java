package com.uade.desarrollo.desarrolloAPP.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Table(name = "profesional")
@Entity
@Data
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String especialidad;

    @OneToMany(mappedBy = "profesional")
    private List<Turno> turnos;

    @OneToMany(mappedBy = "profesional")
    private List<Horario> horarios;

    @OneToMany(mappedBy = "profesional")
    private List<Disponibilidad> disponibilidades;
}

