package com.practica.objetos;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Embeddable significa que se incrusta en otra entidad
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPrestamo {
    @NotNull(message = "El id del usuario es obligatorio y no puede ser null")
    private Long usuario_id;

    @NotNull(message = "El id del libro es obligatorio y no puede ser null")
    private Long libro_id;

    @NotNull(message = "La fecha del préstamo es obligatoria y no puede ser null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaPrestamo;
}
