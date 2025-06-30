package com.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Usuario {
    private String nombreUsuario;
    private String matricula;
    private String fechaNacimiento;
    private String correoElectronico;
    @JsonProperty("_links")
    private ResourceLink _links;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String matricula,
            String fechaNacimiento, String correoElectronico, ResourceLink links) {
        this.nombreUsuario = nombreUsuario;
        this.matricula = matricula;
        this.fechaNacimiento = fechaNacimiento;
        this.correoElectronico = correoElectronico;
        this._links = links;
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

    public ResourceLink get_links() {
        return _links;
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

    public void set_links(ResourceLink links) {
        this._links = links;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario: {")
                .append("nombreUsuario='").append(nombreUsuario).append('\'')
                .append(", matricula='").append(matricula).append('\'')
                .append(", fechaNacimiento='").append(fechaNacimiento).append('\'')
                .append(", correoElectronico='").append(correoElectronico).append('\'');

        if (_links != null && _links.getSelf() != null && _links.getSelf().getHref() != null) {
            sb.append(", enlace='").append(_links.getSelf().getHref()).append('\'');
        }

        sb.append('}');
        return sb.toString();
    }
}
