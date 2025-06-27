package com.practica.objetos;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Calendar;
import java.util.Date;

import lombok.*;

@Entity
@Data
@Table(name = "prestamos")
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo extends RepresentationModel<Prestamo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @NotNull(message = "La fecha de préstamo es obligatoria.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucionPrevista;

    @Temporal(TemporalType.DATE)
    private Date fechaDevolucionReal = null;

    private boolean ampliado = false;

    private String sancion;
}
