package com.practica.objetos;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
public class Usuario extends RepresentationModel<Usuario>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    private String nombreUsuario;

    @NotBlank(message = "La matrícula es obligatoria.")
    private String matricula;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Email(message="Correo electrónico no válido")
    private String correoElectronico;

    public Usuario() {}

    public Usuario(String nombreUsuario, String matricula, 
        Date fechaNacimiento, String correoElectronico) {
            this.nombreUsuario = nombreUsuario;
            this.matricula = matricula;
            this.fechaNacimiento = fechaNacimiento;
            this.correoElectronico = correoElectronico;
    }

    public Long getId() { return id; }

    public void setId(Long id) {
        this.id = id;
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

}
