package com.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.client.model.*;

import reactor.core.publisher.Mono;

public class ClienteBiblioteca {
    private static final String BASE_URL = "http://localhost:8080/api/v1";

    private WebClient webClient;

    public ClienteBiblioteca() {
        webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    public void postUsuario(String nombre, String matricula,
            String fechaNacimiento, String correoElectronico) {
        // Crea un usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombre);
        usuario.setMatricula(matricula);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setCorreoElectronico(correoElectronico);
        // Prueba la operacion
        try {
            String referencia = webClient.post() // operación POST
                    .uri("/usuarios") // URI
                    .contentType(MediaType.APPLICATION_JSON) // Añadimos la cabecera content_type
                    .body(Mono.just(usuario), Usuario.class) // Crea un objeto Mono con el contenido del
                    .retrieve() // realiza la solicitud
                    .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                            .doOnNext(body -> System.err.println("Error 4xx: " + body))
                            .then(Mono.empty())) // Permite continuar la ejecución
                    .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                            .doOnNext(body -> System.err.println("Error 5xx: " + body))
                            .then(Mono.empty()))
                    .toBodilessEntity() // Obtiene solo la respuesta HTTP sin cuerpo
                    .map(response -> {// Obtiene la cabecera location de la respuesta
                        if (response.getHeaders().getLocation() != null) {
                            return response.getHeaders().getLocation().toString();
                        } else {
                            throw new RuntimeException("No se recibió una URL en la cabecera Location");
                        }
                    })
                    .block();// Bloquea para obtener el resultado sincrónicamente
            if (referencia != null) {
                System.out.println(referencia);
            }
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void getUsuario(String usuarioId) {
        // Realizamos la petición GET y deserializamos la respuesta en Usuario
        Usuario usuario = webClient.get() // operación a realizar
                .uri("/usuarios/{id}", usuarioId) // URI
                .retrieve() // realiza la solicitud
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(Usuario.class) // Convierte la respuesta en un Usuario
                .block(); // Usamos block() para obtener la respuesta de forma síncrona

        // Imprime la respuesta
        if (usuario != null) {
            System.out.println(usuario.toString());
        }
    }

    public void putUsuario(String usuarioId, String nombreUsuario,
            String matricula, String fechaNacimiento, String correoElectronico) {
        // Creacion del usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(usuarioId);
        usuario.setMatricula(matricula);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setCorreoElectronico(correoElectronico);
        // operación put
        usuario = webClient.put()
                .uri("/usuarios/{id}", usuarioId) // URI
                .contentType(MediaType.APPLICATION_JSON) // Cabecera content_type
                .body(Mono.just(usuario), Usuario.class) // crea el objeto mono con el empleado
                .retrieve() // realiza la solicitud
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty())) // Permite continuar la ejecución
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(Usuario.class)
                .block(); // Bloquea hasta recibir la respuesta

        // Imprime la respuesta
        if (usuario != null) {
            System.out.println(usuario.toString());
        }
    }

    public void deleteUsuario(String usuarioId) {
        webClient.delete() // Operación DELETE
                .uri("/usuarios/{id}", usuarioId) // URI
                .retrieve() // realiza la solicitud
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                .then(Mono.empty())) // Permite continuar la ejecución
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .toBodilessEntity() // Obtiene solo la respuesta HTTP sin cuerpo
                .block();// Bloquea para obtener el resultado sincrónicamente
    }

    public void getUsuarioList(String startWith, Integer page, Integer size) {
        PageUsuario usuarios = webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder
                            .path("/usuarios");

                    if (startWith != null && !startWith.isEmpty()) {
                        builder.queryParam("starts_with", startWith);
                    }
                    if (page != null) {
                        builder.queryParam("page", page);
                    }
                    if (size != null) {
                        builder.queryParam("size", size);
                    }

                    return builder.build();
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty())) // Permite continuar la ejecución
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(PageUsuario.class)
                .block();
        usuarios.imprimirPaginaUsuarios();
    }

    public void postLibro(String titulo, String autor, String edicion,
            String isbn, String editorial, int disponibles) {

        // Crea un libro
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEdicion(edicion);
        libro.setIsbn(isbn);
        libro.setEditorial(editorial);
        libro.setDisponibles(disponibles);

        // Prueba la operacion
        try {
            String referencia = webClient.post()
                    .uri("/libros")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(libro), Libro.class)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                            .doOnNext(body -> System.err.println("Error 4xx: " + body))
                            .then(Mono.empty()))
                    .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                            .doOnNext(body -> System.err.println("Error 5xx: " + body))
                            .then(Mono.empty()))
                    .toBodilessEntity()
                    .map(response -> {
                        if (response.getHeaders().getLocation() != null) {
                            return response.getHeaders().getLocation().toString();
                        } else {
                            throw new RuntimeException("No se recibió una URL en la cabecera Location");
                        }
                    })
                    .block();

            if (referencia != null) {
                System.out.println(referencia);
            }
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void getLibro(String libroId) {
        // Realizamos la petición GET y deserializamos la respuesta en Libro
        Libro libro = webClient.get()
                .uri("/libros/{id}", libroId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(Libro.class)
                .block();

        // Imprime la respuesta
        if (libro != null) {
            System.out.println(libro.toString());
        }
    }

    public void putLibro(String libroId, String titulo, String autor,
            String edicion, String isbn, String editorial, int disponibles) {
        // Creacion del libro
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setEdicion(edicion);
        libro.setIsbn(isbn);
        libro.setEditorial(editorial);
        libro.setDisponibles(disponibles);

        // operación put
        libro = webClient.put()
                .uri("/libros/{id}", libroId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(libro), Libro.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(Libro.class)
                .block();

        // Imprime la respuesta
        if (libro != null) {
            System.out.println(libro.toString());
        }
    }

    public void deleteLibro(String libroId) {
        webClient.delete()
                .uri("/libros/{id}", libroId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .toBodilessEntity()
                .block();
    }

    public void getLibroList(String pattern, Boolean disponible, Integer page, Integer size) {
        PageLibro libros = webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder
                            .path("/libros");

                    if (pattern != null && !pattern.isEmpty()) {
                        builder.queryParam("titulo", pattern);
                    }
                    if (disponible != null) {
                        builder.queryParam("disponible", disponible);
                    }
                    if (page != null) {
                        builder.queryParam("page", page);
                    }
                    if (size != null) {
                        builder.queryParam("size", size);
                    }

                    return builder.build();
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(PageLibro.class)
                .block();

        if (libros != null) {
            libros.imprimirPaginaLibros();
        }
    }

    public void postPrestamo(String usuarioId, String libroId, String fechaPrestamoStr) {
        try {
            DatoPrestamo datoPrestamo = new DatoPrestamo(libroId, fechaPrestamoStr);
            String referencia = webClient.post()
                    .uri("/usuarios/{usuarioId}/prestamos", usuarioId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(datoPrestamo), DatoPrestamo.class)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                            .doOnNext(body -> System.err.println("Error 4xx: " + body))
                            .then(Mono.empty()))
                    .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                            .doOnNext(body -> System.err.println("Error 5xx: " + body))
                            .then(Mono.empty()))
                    .toBodilessEntity()
                    .map(response -> {
                        if (response.getHeaders().getLocation() != null) {
                            return response.getHeaders().getLocation().toString();
                        } else {
                            throw new RuntimeException("No se recibió una URL en la cabecera Location");
                        }
                    })
                    .block();

            if (referencia != null) {
                System.out.println(referencia.toString());
            }
        } catch (RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void getPrestamo(String usuarioId, Long prestamoId) {
        Prestamo prestamo = webClient.get()
                .uri("/usuarios/{usuarioId}/prestamos/{prestamoId}", usuarioId, prestamoId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(Prestamo.class)
                .block();

        if (prestamo != null) {
            System.out.println(prestamo.toString());
        }
    }

    public void getPrestamoList(String usuarioId, Boolean actual,
            String fromDate, String toDate, Integer page, Integer size) {
        PagePrestamo prestamos = webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder
                            .path("/usuarios/{usuarioId}/prestamos");

                    if (actual != null) {
                        builder.queryParam("actual", actual);
                    }
                    if (fromDate != null && !fromDate.isEmpty()) {
                        builder.queryParam("from", fromDate);
                    }
                    if (toDate != null && !toDate.isEmpty()) {
                        builder.queryParam("to", toDate);
                    }
                    if (page != null) {
                        builder.queryParam("page", page);
                    }
                    if (size != null) {
                        builder.queryParam("size", size);
                    }

                    return builder.build(usuarioId);
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(PagePrestamo.class)
                .block();

        if (prestamos != null) {
            prestamos.imprimirPaginaPrestamos();
        }
    }

    public void extendPrestamo(String usuarioId, Long prestamoId) {
        PrestamoAccionDTO accion = new PrestamoAccionDTO("ampliar", null);

        Prestamo prestamo = webClient.put()
                .uri("/usuarios/{usuarioId}/prestamos/{prestamoId}", usuarioId, prestamoId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accion), PrestamoAccionDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(Prestamo.class)
                .block();

        if (prestamo != null) {
            System.out.println(prestamo.toString());
        }
    }

    public void returnPrestamo(String usuarioId, Long prestamoId,
    String fechaDevolucionStr) {
        
        PrestamoAccionDTO accion = new PrestamoAccionDTO("devolver", fechaDevolucionStr);

        RespResultado resultado = webClient.put()
                .uri("/usuarios/{usuarioId}/prestamos/{prestamoId}", usuarioId, prestamoId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(accion), PrestamoAccionDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(RespResultado.class)
                .block();

        if (resultado != null) {
            System.out.println(resultado.getResultado());
        }
    }

    public void deletePrestamo(String usuarioId, Long prestamoId) {
        webClient.delete()
                .uri("/usuarios/{usuarioId}/prestamos/{prestamoId}", usuarioId, prestamoId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> System.err.println("Error 4xx: " + body))
                                .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .toBodilessEntity()
                .block();
    }

    public void getActividadUsuario(String usuarioId) {
        ActividadPrestamoDTO actividad = webClient.get()
                .uri("/usuarios/{id}/actividad", usuarioId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 4xx: " + body))
                        .then(Mono.empty()))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .doOnNext(body -> System.err.println("Error 5xx: " + body))
                        .then(Mono.empty()))
                .bodyToMono(ActividadPrestamoDTO.class)
                .block();

        if (actividad != null) {
            actividad.printActividadUsuario();
        }
    }

}
