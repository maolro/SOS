package com.client;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.client.wrappers.*;

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

    // listar todos los usuarios
    public List<Usuario> listarUsuarios(String startsWith, int page, int size) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "usuarios")
            .queryParam("starts_with", startsWith)
            .queryParam("page", page)
            .queryParam("size", size)
            .toUriString();
        try {
            ResponseEntity<UsuarioPagedResponse> resp = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UsuarioPagedResponse>() {}
            );

            UsuarioPagedResponse usuarios = resp.getBody();
            if (usuarios != null && usuarios.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), new String("Usuarios: " 
                + usuarios.get_embedded().getUsuarioList().toString()));
                return usuarios.get_embedded().getUsuarioList();
            } else {
                return Collections.emptyList();
            }

        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }
    // ve todos los libros
    public List<Libro> listarLibros(int page, int size, String titulo, Boolean disponible) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "libros")
            .queryParam("page", page)
            .queryParam("size", size);
        if (titulo != null)     uri.queryParam("titulo", titulo);
        if (disponible != null) uri.queryParam("disponible", disponible);

        String url = uri.toUriString();
        try{
            ResponseEntity<LibroPagedResponse> resp = restTemplate.exchange(url, 
            HttpMethod.GET,null,
            new ParameterizedTypeReference<LibroPagedResponse>() {});       
            LibroPagedResponse libros = resp.getBody();
            if (libros != null && libros.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), new String("Libros: " 
                + libros.get_embedded().getLibroList().toString()));
                return libros.get_embedded().getLibroList();
            } else {
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    //Lista préstamos de un usuario

    public List<Prestamo> listarPrestamosUsuario(long usuarioId) {
        String url = BASE_URL + "prestamos?usuarioId=" + usuarioId;
        try{
            ResponseEntity<PrestamoPagedResponse> resp = restTemplate.exchange(url, 
            HttpMethod.GET,null,
            new ParameterizedTypeReference<PrestamoPagedResponse>() {});       
            PrestamoPagedResponse prestamos = resp.getBody();
            if (prestamos != null && prestamos.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), new String("Préstamos: " 
                + prestamos.get_embedded().getPrestamoList().toString()));
                return prestamos.get_embedded().getPrestamoList();
            } else {
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }


    // extiende un prestamo
    public void ampliarPrestamo(long prestamoId, int dias) {
        String url = BASE_URL + "prestamos/" + prestamoId + "/ampliar?dias=" + dias;
        try {
            ResponseEntity<Void> resp = restTemplate.postForEntity(url, null, Void.class);
            imprimirRespuesta(resp.getStatusCode(), resp.getBody().toString());
        } catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    //devuelve un prestamo

    public void devolverPrestamo(long prestamoId) {
        String url = BASE_URL + "prestamos/" + prestamoId + "/devolucion";
        try {
            ResponseEntity<Prestamo> resp = restTemplate.postForEntity(url, null, Prestamo.class);
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
        "Primera", "123483249545", "Editorial Sudamericana", true);

        // Crear un préstamo
        client.crearPrestamo(1, 1, "2024-05-01");


        // Listar usuarios
        client.listarUsuarios("",0,10);

        //Listar libros (todos / sólo disponibles / por título)
        client.listarLibros(0, 10, null, null);
        client.listarLibros(0, 10, null, true);
        client.listarLibros(0, 10, "Java", null);

        //listar préstamos del usuario 1
        client.listarPrestamosUsuario(1);

        // extender préstamo #1 en 3 días
        client.ampliarPrestamo(1, 3);

        //  devolver préstamo #1
        client.devolverPrestamo(1);

        // Comprobar lista actualizada de préstamos
        System.out.println("Préstamos tras devolución: " + 
        client.listarPrestamosUsuario(1).toString());
}
}
