import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        
        RentalService servicio = new RentalService();

        Javalin app = Javalin.create(config -> {

            config.staticFiles.add("web", Location.EXTERNAL);
        }).start(7070);

        System.out.println("⚡ SHARENERGY está funcionando en: http://localhost:7070/index.html");

        //CONEXIONES 

        //Entregar la lista de baterías en formato JSON
        app.get("/api/lista-baterias", ctx -> {
            String json = servicio.obtenerListaJson();
            ctx.contentType("application/json").result(json);
        });

        // 2. Registro
        app.get("/api/registro", ctx -> {
            String idSesion = ctx.queryParam("u");
            String telefono = ctx.queryParam("tel");
            if(idSesion != null && telefono != null) {
                servicio.registrarUsuario(idSesion, telefono);
                ctx.result("OK");
            } else { ctx.result("ERROR"); }
        });

        // 3. Alquiler 
        app.get("/api/alquilar", ctx -> {
            String idSesion = ctx.queryParam("u");
            String idBateria = ctx.queryParam("b"); 
            
            String respuesta;
            if (idBateria != null) {
            
                respuesta = servicio.iniciarAlquilerEspecifico(idSesion, idBateria);
            } else {
                
                respuesta = servicio.iniciarAlquiler(idSesion);
            }
            ctx.result(respuesta);
        });

        // 4. Devolver 
        app.get("/api/devolver", ctx -> {
            String idSesion = ctx.queryParam("u");
            ctx.result(servicio.finalizarAlquiler(idSesion));
        });
        
        // 5. Estado HTML 
        app.get("/api/estado", ctx -> ctx.html(servicio.obtenerEstadoInventario()));
    }
}