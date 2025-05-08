package com.uade.desarrollo.desarrolloAPP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "turnos")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"profesional", "usuario", "notificaciones", "resultadoEstudio"})
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_profesional", nullable = false)
    private Profesional profesional;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private String estado;

    @OneToOne(mappedBy = "turno", cascade = CascadeType.ALL)
    private ResultadoEstudio resultadoEstudio;

    @OneToMany(mappedBy = "turno")
    private List<Notificacion> notificaciones = new ArrayList<>();
}

