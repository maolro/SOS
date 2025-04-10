package com.practica;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ServicioPrestamo {
    private final Map<Integer, Prestamo> prestamos = new HashMap<>();
    
    private final AtomicInteger idCounter = new AtomicInteger(1);
    
    public Prestamo crearPrestamo(Prestamo prestamo) {
        int id = idCounter.getAndIncrement();
        prestamo.setId(id);
        prestamos.put(id, prestamo);
        return prestamo;
    }
    
    public List<Prestamo> obtenerTodosPrestamos() {
        return new ArrayList<>(prestamos.values());
    }
    
    public Prestamo obtenerPrestamoPorId(Integer id) {
        return prestamos.get(id);
    }
    
    
    // Método para DELETE
    public boolean eliminarPrestamo(Integer id) {
        return prestamos.remove(id) != null;
    }
}