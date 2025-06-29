package com.client;

import org.springframework.http.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;

import reactor.core.publisher.Mono;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.client.model.*;
import com.client.wrappers.LibroPagedResponse;
import com.client.wrappers.PrestamoPagedResponse;
import com.client.wrappers.UsuarioPagedResponse;

public class BibliotecaService {

    private static final String BASE_URL = "http://localhost:8080/api/v1/"; 
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
    .ofPattern("yyyy-MM-dd");

    private WebClient webClient;
    private RestTemplate restTemplate;

    public BibliotecaService() {
        webClient = WebClient.builder().baseUrl(BASE_URL).build();
        restTemplate = 
    }

    // Función para imprimir la respuesta con mensajes en español
    private void imprimirRespuesta(HttpStatusCode status, String msg) {
        System.out.println("\n=== RESULTADO DE LA OPERACIÓN ===");
        System.out.println("Estado HTTP: " + status);
        System.out.println("Mensaje: " + msg);
        System.out.println("===============================\n");
    }

    // Crear usuario (POST /usuarios)
    public Usuario crearUsuario(String nombreUsuario, String matricula, String fechaNacimiento, String correoElectronico) {
        String url = BASE_URL + "usuarios";
        Usuario usuario = new Usuario(nombreUsuario, matricula, fechaNacimiento, correoElectronico);

        try {
            ResponseEntity<Usuario> resp = restTemplate.postForEntity(url, usuario, Usuario.class);
            imprimirRespuesta(resp.getStatusCode(), "Usuario creado exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al crear usuario: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Obtener usuario por ID (GET /usuarios/{id})
    public Usuario obtenerUsuario(Long id) {
        Usuario usuario = webClient.get() //operación a realizar
            .uri("/usuarios/", id) // URI
            .retrieve() // realiza la solicitud
            .onStatus(HttpStatusCode::is4xxClientError, response 
                -> response.bodyToMono(String.class)
                .doOnNext(body -> System.err.println("Error 4XX: " + body))
                .then(Mono.empty()) // Permite continuar la ejecución
            )
            .onStatus(HttpStatusCode::is5xxServerError, response 
                -> response.bodyToMono(String.class)
                .doOnNext(body -> System.err.println("Error 5XX: " + body))
                .then(Mono.empty())
            )
            .bodyToMono(Usuario.class) //Convierte la respuesta en un Usuario
            .block(); // Usamos block() para obtener la respuesta de forma síncrona
        
        return usuario;
    }

    // Actualizar usuario (PUT /usuarios/{id})
    public Usuario actualizarUsuario(Long id, String nombreUsuario, String matricula, String fechaNacimiento, String correoElectronico) {
        String url = BASE_URL + "usuarios/" + id;
        Usuario usuario = new Usuario(nombreUsuario, matricula, fechaNacimiento, correoElectronico);
        usuario.setId(id);

        try {
            restTemplate.put(url, usuario);
            imprimirRespuesta(HttpStatus.OK, "Usuario actualizado exitosamente");
            return obtenerUsuario(id); // Volver a obtener para ver cambios
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al actualizar usuario: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Eliminar usuario (DELETE /usuarios/{id})
    public void eliminarUsuario(Long id) {
        String url = BASE_URL + "usuarios/" + id;
        
        try {
            restTemplate.delete(url);
            imprimirRespuesta(HttpStatus.NO_CONTENT, "Usuario eliminado exitosamente");
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al eliminar usuario: " + e.getResponseBodyAsString());
        }
    }

    // Crear Libro (POST /libros)
    public Libro crearLibro(String titulo, String autor, String edicion, 
    String isbn, String editorial, int disponibles) {
        String url = BASE_URL + "libros";
        Libro libro = new Libro(titulo, autor, edicion, isbn, editorial, disponibles);

        try {
            ResponseEntity<Libro> resp = restTemplate.postForEntity(url, libro, Libro.class);
            imprimirRespuesta(resp.getStatusCode(), "Libro creado exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al crear libro: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Obtener libro por ID (GET /libros/{id})
    public Libro obtenerLibro(Long id) {
        String url = BASE_URL + "libros/" + id;
        
        try {
            ResponseEntity<Libro> resp = restTemplate.getForEntity(url, Libro.class);
            imprimirRespuesta(resp.getStatusCode(), "Libro obtenido exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al obtener libro: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Actualizar libro (PUT /libros/{id})
    public Libro actualizarLibro(Long id, String titulo, String autor, String edicion, 
    String isbn, String editorial, int disponibles) {
        String url = BASE_URL + "libros/" + id;
        Libro libro = new Libro(titulo, autor, edicion, isbn, editorial, disponibles);
        libro.setId(id);

        try {
            restTemplate.put(url, libro);
            imprimirRespuesta(HttpStatus.OK, "Libro actualizado exitosamente");
            return obtenerLibro(id); // Volver a obtener para ver cambios
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al actualizar libro: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Eliminar libro (DELETE /libros/{id})
    public void eliminarLibro(Long id) {
        String url = BASE_URL + "libros/" + id;
        
        try {
            restTemplate.delete(url);
            imprimirRespuesta(HttpStatus.NO_CONTENT, "Libro eliminado exitosamente");
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al eliminar libro: " + e.getResponseBodyAsString());
        }
    }

    // Crear Prestamo sin fecha (POST /prestamos)
    public Prestamo crearPrestamo(Long usuario_id, Long libro_id) {
        String url = BASE_URL + "usuarios/" + usuario_id + "/prestamos";
        String fechaPrestamo = LocalDate.now().format(DATE_FORMATTER);
        UsuarioPrestamo up = new UsuarioPrestamo();
        up.setUsuario_id(usuario_id);
        up.setLibro_id(libro_id);
        up.setFechaPrestamo(fechaPrestamo);
        try {
            ResponseEntity<Prestamo> resp = restTemplate.postForEntity(url, up, Prestamo.class);
            imprimirRespuesta(resp.getStatusCode(), "Préstamo creado exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al crear préstamo: " + e.getResponseBodyAsString());
            return null;
        }
    }
    // Crear Prestamo con fecha (POST /prestamos)
    public Prestamo crearPrestamoConFecha(Long usuario_id, Long libro_id, String fechaPrestamo) {
        String url = BASE_URL + "usuarios/" + usuario_id + "/prestamos";
        UsuarioPrestamo up = new UsuarioPrestamo();
        up.setUsuario_id(usuario_id);
        up.setLibro_id(libro_id);
        up.setFechaPrestamo(fechaPrestamo);
        try {
            ResponseEntity<Prestamo> resp = restTemplate.postForEntity(url, up, Prestamo.class);
            imprimirRespuesta(resp.getStatusCode(), "Préstamo creado exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al crear préstamo: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Obtener préstamo por ID (GET /prestamos/{id})
    public Prestamo obtenerPrestamo(Long usuario_id, Long prestamo_id) {
        String url = BASE_URL + "usuarios/" + usuario_id + "/prestamos/" + prestamo_id;
        
        try {
            ResponseEntity<Prestamo> resp = restTemplate.getForEntity(url, Prestamo.class);
            imprimirRespuesta(resp.getStatusCode(), "Préstamo obtenido exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al obtener préstamo: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Listar todos los usuarios con paginación
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
                imprimirRespuesta(resp.getStatusCode(), "Lista de usuarios obtenida exitosamente: " 
                + usuarios.get_embedded().getUsuarioList().toString());
                return usuarios.get_embedded().getUsuarioList();
            } 
            else {
                imprimirRespuesta(resp.getStatusCode(), "No se encontraron usuarios");
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), "Error al listar usuarios: " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    // Listar todos los libros con filtros
    public List<Libro> listarLibros(Integer page, Integer size, String titulo, Boolean disponible) {
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
                imprimirRespuesta(resp.getStatusCode(), "Lista de libros obtenida exitosamente: " 
                + libros.get_embedded().getLibroList().toString());
                return libros.get_embedded().getLibroList();
            } 
            else {
                imprimirRespuesta(resp.getStatusCode(), "No se encontraron libros");
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), "Error al listar libros: " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    // Listar préstamos de un usuario
    public List<Prestamo> listarPrestamosUsuario(Long usuarioId, Integer page, Integer size, String fechaInicio, String fechaFinal) {
        String base = BASE_URL + "usuarios/" + usuarioId + "/prestamos";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(base);
    
        if (page != null) {
            builder.queryParam("page", page);
        }
        if (size != null) {
            builder.queryParam("size", size);
        }
        if (fechaInicio != null) {
            builder.queryParam("fechaInicio", fechaInicio);
        }
        if (fechaFinal != null) {
            builder.queryParam("fechaFinal", fechaFinal);
        }

        String url = builder.toUriString();
        try{
            ResponseEntity<PrestamoPagedResponse> resp = restTemplate.exchange(url, 
            HttpMethod.GET,null,
            new ParameterizedTypeReference<PrestamoPagedResponse>() {});       
            PrestamoPagedResponse prestamos = resp.getBody();
            if (prestamos != null && prestamos.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), "Préstamos del usuario obtenidos exitosamente: " 
                + prestamos.get_embedded().getPrestamoList().toString());
                return prestamos.get_embedded().getPrestamoList();
            } 
            else {
                imprimirRespuesta(resp.getStatusCode(), "El usuario no tiene préstamos");
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), "Error al listar préstamos del usuario: " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    // Listar histórico de préstamos de un usuario
    public List<Prestamo> listarHistoricoPrestamosUsuario(Long usuarioId, Integer page, Integer size, String fechaInicio, String fechaFinal) {
        String base = BASE_URL + "usuarios/" + usuarioId + "/prestamos/historico";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(base);
    
        if (page != null) {
            builder.queryParam("page", page);
        }
        if (size != null) {
            builder.queryParam("size", size);
        }
        if (fechaInicio != null) {
            builder.queryParam("fechaInicio", fechaInicio);
        }
        if (fechaFinal != null) {
            builder.queryParam("fechaFinal", fechaFinal);
        }

        String url = builder.toUriString();
        try{
            ResponseEntity<PrestamoPagedResponse> resp = restTemplate.exchange(url, 
            HttpMethod.GET,null,
            new ParameterizedTypeReference<PrestamoPagedResponse>() {});       
            PrestamoPagedResponse prestamos = resp.getBody();
            if (prestamos != null && prestamos.get_embedded() != null) {
                imprimirRespuesta(resp.getStatusCode(), "Histórico de préstamos del usuario obtenido exitosamente: " 
                + prestamos.get_embedded().getPrestamoList().toString());
                return prestamos.get_embedded().getPrestamoList();
            } 
            else {
                imprimirRespuesta(resp.getStatusCode(), "El usuario no tiene histórico de préstamos");
                return Collections.emptyList();
            }
        }
        catch (HttpClientErrorException | HttpServerErrorException ex) {
            imprimirRespuesta(ex.getStatusCode(), "Error al listar histórico de préstamos: " + ex.getResponseBodyAsString());
            return Collections.emptyList();
        }
    }

    // Obtener actividad completa de un usuario
    public void obtenerActividadUsuario(Long usuarioId, Integer page, Integer size) {
        String base = BASE_URL + "usuarios/" + usuarioId + "/actividad";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(base);
    
        if (page != null) {
            builder.queryParam("page", page);
        }
        if (size != null) {
            builder.queryParam("size", size);
        }

        String url = builder.toUriString();
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            imprimirRespuesta(resp.getStatusCode(), "Actividad del usuario obtenida exitosamente: " + resp.getBody());
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al obtener actividad del usuario: " + e.getResponseBodyAsString());
        }
    }

    // Ampliar préstamo
    public Prestamo ampliarPrestamo(Long usuarioId, Long prestamoId) {
        String url = BASE_URL + "usuarios/" + usuarioId + "/prestamos/" + prestamoId;
        // Creacion del objeto con las actualizaciones
        UsuarioPrestamo up = new UsuarioPrestamo();
        up.setUsuario_id(usuarioId);
        up.setLibro_id(prestamoId);
        up.setAmpliado(true);
        try {
            restTemplate.put(url, up);
            imprimirRespuesta(HttpStatus.OK, "Libro actualizado exitosamente");
            return obtenerPrestamo(usuarioId, prestamoId); 
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al ampliar el prestamo: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Devolver préstamo
    public Prestamo devolverPrestamo(Long prestamoId) {
        String url = BASE_URL + "prestamos/" + prestamoId + "/devolucion";
        try {
            ResponseEntity<Prestamo> resp = restTemplate.postForEntity(url, null, Prestamo.class);
            imprimirRespuesta(resp.getStatusCode(), "Préstamo devuelto exitosamente: " + resp.getBody().toString());
            return resp.getBody();
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error al devolver préstamo: " + e.getResponseBodyAsString());
            return null;
        }
    }

    // Probar devolución tardía
    public void probarDevolucionTardia(Long usuarioId, Long libroId) {
        // Crear un préstamo con fecha antigua (simulando retraso)
        String fechaAntigua = LocalDate.now().minusWeeks(3).format(DATE_FORMATTER);
        String url = BASE_URL + "prestamos";
        CrearPrestamoDTO prestamoDTO = new CrearPrestamoDTO(usuarioId, libroId, fechaAntigua);

        try {
            // Crear préstamo atrasado (esto requeriría que el backend permita fechas pasadas para pruebas)
            ResponseEntity<Prestamo> respCrear = restTemplate.postForEntity(url, prestamoDTO, Prestamo.class);
            Prestamo prestamo = respCrear.getBody();
            
            if (prestamo != null) {
                // Intentar devolver
                Prestamo prestamoDevuelto = devolverPrestamo(prestamo.getId());
                
                if (prestamoDevuelto != null && prestamoDevuelto.getSancion() != null) {
                    imprimirRespuesta(HttpStatus.OK, "SANCION APLICADA CORRECTAMENTE: " + prestamoDevuelto.getSancion());
                    
                    // Intentar crear nuevo préstamo (debería fallar por sanción)
                    try {
                        crearPrestamo(usuarioId, libroId);
                        imprimirRespuesta(HttpStatus.BAD_REQUEST, "ERROR: Se permitió préstamo durante sanción");
                    } catch (Exception e) {
                        imprimirRespuesta(HttpStatus.FORBIDDEN, "CORRECTO: No se permitió préstamo durante sanción");
                    }
                }
            }
        } 
        catch (HttpClientErrorException e) {
            imprimirRespuesta(e.getStatusCode(), "Error en prueba de devolución tardía: " + e.getResponseBodyAsString());
        }
    }
}