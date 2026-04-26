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

        //Endpoint para GUARDAR el teléfono
        app.get("/api/registro", ctx -> {
            String idSesion = ctx.queryParam("u");
            String telefono = ctx.queryParam("tel");
            
            if(idSesion != null && telefono != null) {
                servicio.registrarUsuario(idSesion, telefono);
                ctx.result("OK");
            } else {
                ctx.result("ERROR: Faltan datos");
            }
        });

        //Alquilar
        app.get("/api/alquilar", ctx -> {
            String idSesion = ctx.queryParam("u");
            String respuesta = servicio.iniciarAlquiler(idSesion);
            ctx.result(respuesta);
        });

        //Devolver
        app.get("/api/devolver", ctx -> {
            String idSesion = ctx.queryParam("u");
            String respuesta = servicio.finalizarAlquiler(idSesion);
            ctx.result(respuesta);
        });
        
        app.get("/api/estado", ctx -> ctx.result(servicio.obtenerEstadoInventario()));
    }
}