package com.sharenergy;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {
        
        RentalService servicio = new RentalService();
        ObjectMapper objectMapper = new ObjectMapper();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("web", Location.EXTERNAL);
            config.jsonMapper(new JavalinJackson());
        }).start(7070);

        System.out.println("⚡ SHARENERGY PREMIUM está funcionando en: http://localhost:7070/index.html");

        // --- API ROUTES ---

        // 1. Catálogo de baterías (GET)
        app.get("/api/lista-baterias", ctx -> {
            ctx.contentType("application/json").result(servicio.obtenerListaJson());
        });

        // 2. Registro de usuario (Sigue usando GET por simplicidad con el front actual, pero persistente)
        app.get("/api/registro", ctx -> {
            String idSesion = ctx.queryParam("u");
            String telefono = ctx.queryParam("tel");
            if(idSesion != null && telefono != null) {
                servicio.registrarUsuario(idSesion, telefono);
                ctx.status(201).result("OK");
            } else { 
                ctx.status(400).result("ERROR: Datos de registro incompletos"); 
            }
        });

        // 3. Iniciar Alquiler
        app.get("/api/alquilar", ctx -> {
            String idSesion = ctx.queryParam("u");
            String idBateria = ctx.queryParam("b"); 
            
            String respuesta = (idBateria != null) 
                ? servicio.iniciarAlquilerEspecifico(idSesion, idBateria)
                : servicio.iniciarAlquiler(idSesion);
            
            if (respuesta.startsWith("OK")) ctx.status(200);
            else ctx.status(400);
            
            ctx.result(respuesta);
        });

        // 4. Finalizar Alquiler
        app.get("/api/devolver", ctx -> {
            String idSesion = ctx.queryParam("u");
            String respuesta = servicio.finalizarAlquiler(idSesion);
            
            if (respuesta.startsWith("ERROR")) ctx.status(400);
            else ctx.status(200);
            
            ctx.result(respuesta);
        });
        
        // 5. Estado del Sistema (Debug/Admin)
        app.get("/api/estado", ctx -> ctx.html(servicio.obtenerEstadoInventario()));

        // Manejo de errores globales
        app.exception(Exception.class, (e, ctx) -> {
            System.err.println("❌ Error en el servidor: " + e.getMessage());
            ctx.status(500).result("ERROR INTERNO: " + e.getMessage());
        });
    }
}