package com.client.model;

public class Usuario {
    private Long id;
    private String nombreUsuario;
    private String matricula;
    private String fechaNacimiento;
    private String correoElectronico;
    private ResourceLink _links;

    public Usuario() {}

    public Usuario(String nombreUsuario, String matricula, String fechaNacimiento, String correoElectronico) {
        this.nombreUsuario = nombreUsuario;
        this.matricula = matricula;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
    }

    public Long getId() {
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public ResourceLink getLinks() {
        return _links;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setLinks(ResourceLink links) {
        this._links = links;
    }

    @Override
    public String toString() {
        return "Usuario: {" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", matricula='" + matricula + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", enlace='" + _links.getSelf().getHref() + '\'' +
                '}';
    }
}
