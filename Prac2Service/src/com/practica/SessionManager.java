package com.practica;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static Map<String, String> sesionesActivas = new ConcurrentHashMap<>();
    private static Map<String, String> rolesUsuario = new HashMap<>();
    // Otorga privilegios de admin al usuario admin al arrancar
    static{
        rolesUsuario.put("admin", "admin");
    }
    // Métodos para gestionar el funcionamiento de sesiones
    public static String createSession(String usuario){
        String sessionId = UUID.randomUUID().toString();
        sesionesActivas.put(sessionId, usuario);
        return sessionId;
    }
    public static String getUsername(String sessionId){
        return sesionesActivas.get(sessionId);
    }
    public static String getRole(String usuario){
        return rolesUsuario.getOrDefault(usuario, "usuario");
    }
    public static void invalidateSession(String sessionId){
        sesionesActivas.remove(sessionId);
    }
}  
