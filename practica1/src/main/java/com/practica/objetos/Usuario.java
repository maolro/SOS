package com.practica.objetos;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "usuarios")
public class Usuario extends RepresentationModel<Usuario>{

        
    @Id
    @NotNull(message = "La matrícula es obligatoria.")
    private String matricula;

    @NotNull(message = "El nombre de usuario es obligatorio.")
    private String nombreUsuario;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Email(message="Correo electrónico no válido")
    private String correoElectronico;

    private Boolean sancionado = false;

    public Usuario() {}

    public Usuario(String nombreUsuario, String matricula, 
        Date fechaNacimiento, String correoElectronico) {
            this.matricula = matricula;
            this.nombreUsuario = nombreUsuario;
            this.fechaNacimiento = fechaNacimiento;
            this.correoElectronico = correoElectronico;
    }

    public String getNombreUsuario() { return nombreUsuario; }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getMatricula() { return matricula; }

    public Date getFechaNacimiento() { return fechaNacimiento; }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreoElectronico() { return correoElectronico;}

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Boolean getSancionado() { return sancionado;}

    public void setSancionado(Boolean sancionado) {
        this.sancionado = sancionado;
    }
}
