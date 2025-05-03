package com.client;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

public class ClienteLibreria {

    private static final String BASE_URL = "http://localhost:8080/api/v1/"; 

    private RestTemplate restTemplate;

    public ClienteLibreria() {
        restTemplate = new RestTemplate();
    }

    // Función para recibir la respuesa
    private void imprimirRespuesta(HttpStatusCode status, String msg) {
        System.out.println("Estado HTTP: " + status);
        System.out.println("Respuesta: " + msg);
    }

    // Crear usuario (POST /usuarios)
    public void crearUsuario(String nombreUsuario, String matricula, String fechaNacimiento, String correoElectronico) {
        String url = BASE_URL + "/usuarios";
        Usuario usuario = new Usuario(nombreUsuario, matricula, fechaNacimiento, correoElectronico);

        try {
            ResponseEntity<Usuario> resp = restTemplate.postForEntity(url, usuario, Usuario.class);
            imprimirRespuesta(resp.getStatusCode(), resp.getBody().toString());
        } catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    // Crear Libro (POST /libros)
    public void crearLibro(String titulo, String autor, String edicion, 
    String isbn, String editorial, boolean disponible) {
        String url = BASE_URL + "/libros";
        Libro libro = new Libro(titulo, autor, edicion, isbn, editorial, disponible);

        try {
            ResponseEntity<Libro> resp = restTemplate.postForEntity(url, libro, Libro.class);
            imprimirRespuesta(resp.getStatusCode(), resp.getBody().toString());
        } catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    // Crear Prestamo (POST /prestamos)
    public void crearPrestamo(Integer usuario_id, Integer libro_id, String fechaPrestamo) {
        String url = BASE_URL + "/prestamos";
        CrearPrestamoDTO prestamoDTO = new CrearPrestamoDTO(Long.valueOf(usuario_id), Long.valueOf(libro_id), 
        fechaPrestamo);

        try {
            ResponseEntity<Prestamo> resp = restTemplate.postForEntity(url, prestamoDTO, Prestamo.class);
            imprimirRespuesta(resp.getStatusCode(), resp.getBody().toString());
        } catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    public static void main(String[] args) {
        ClienteLibreria client = new ClienteLibreria();

        // Crear usuarios
        client.crearUsuario("Ana Pérez", "123456", 
        "2000-20-4", "ana.perez@ejemplo.com");
        client.crearUsuario("Juan López", "123392", 
        "1999-3-7", "juan.lopez@ejemplo.com");

        // Crear libros
        client.crearLibro("Programación en Java", "Carlos López", "Tercera", 
        "9780307474723", "Pearson", false);
        client.crearLibro("Cien Años de Soledad", "Gabriel García Márquez", 
        "Primera", "123483249545", "Editorial Sudamericana", false);

        // Crear un préstamo
        client.crearPrestamo(1, 1, "2024-05-01");
        // Extender el préstamo dos días

        // Devolver el préstamo

        // Listar préstamos del usuario
    }
}