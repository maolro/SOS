package com.practica;

public class Usuario {
    private Integer id;
    private String nombreUsuario;
    private String matricula;
    private String fechaNacimiento;
    private String correoElectronico;

    public Usuario() {}

    public Usuario(String nombreUsuario, String matricula, 
        String fechaNacimiento, String correoElectronico) {
            this.nombreUsuario = nombreUsuario;
            this.matricula = matricula;
            this.fechaNacimiento = fechaNacimiento;
            this.correoElectronico = correoElectronico;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUsuario() { return nombreUsuario; }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getMatricula() { return matricula; }

    public String getFechaNacimiento() { return fechaNacimiento; }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreoElectronico() { return correoElectronico;}

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
}
