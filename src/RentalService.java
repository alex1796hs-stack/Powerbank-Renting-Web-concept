import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RentalService {
    private List<Powerbank> inventario = new ArrayList<>();

    private Map<String, Powerbank> alquileresActivos = new HashMap<>();
    private Map<String, String> usuariosRegistrados = new HashMap<>();
    private Map<String, Long> tiemposInicio = new HashMap<>();

    public RentalService() {
        inventario.add(new Powerbank("PB-001"));
        inventario.add(new Powerbank("PB-002"));
    }

    public void registrarUsuario(String sessionId, String telefono) {
        usuariosRegistrados.put(sessionId, telefono);
        System.out.println("📝 Usuario registrado: " + telefono);
    }

    public String iniciarAlquiler(String sessionId) {
        if (alquileresActivos.containsKey(sessionId)) {
            return "ERROR: Ya tienes alquiler activo.";
        }
        
        String telefono = usuariosRegistrados.getOrDefault(sessionId, "Desconocido");

        for (Powerbank pb : inventario) {
            if (!pb.isAlquilada() && !pb.isCargando()) {
                pb.setAlquilada(true);
                
                alquileresActivos.put(sessionId, pb);
                tiemposInicio.put(sessionId, System.currentTimeMillis());
                
                System.out.println("🔋 Alquiler iniciado: " + telefono + " -> " + pb.getId());
                return "OK|" + pb.getId();
            }
        }
        return "ERROR: No hay baterías disponibles (Todas están en uso o cargando).";
    }

    public String obtenerListaJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < inventario.size(); i++) {
            Powerbank pb = inventario.get(i);
            // Está disponible si NO está alquilada Y NO está cargando
            boolean disponible = !pb.isAlquilada() && !pb.isCargando();
            
            json.append(String.format("{\"id\":\"%s\", \"disponible\":%b}", pb.getId(), disponible));
            
            if (i < inventario.size() - 1) json.append(","); // Coma separadora
        }
        json.append("]");
        return json.toString();
    }

    public String iniciarAlquilerEspecifico(String sessionId, String batteryId) {
        if (alquileresActivos.containsKey(sessionId)) return "ERROR: Ya tienes un alquiler activo.";

        for (Powerbank pb : inventario) {
            // Buscamos la batería que coincida con el ID
            if (pb.getId().equals(batteryId)) {
                if (!pb.isAlquilada() && !pb.isCargando()) {
                    
                    pb.setAlquilada(true);
                    alquileresActivos.put(sessionId, pb);
                    tiemposInicio.put(sessionId, System.currentTimeMillis());
                    
                    System.out.println("🔋 Alquiler ESPECÍFICO: " + batteryId);
                    return "OK|" + pb.getId();
                } else {
                    return "ERROR: Esa batería ya no está disponible (alguien se adelantó).";
                }
            }
        }
        return "ERROR: Batería no encontrada.";
    }
    public String finalizarAlquiler(String sessionId) {
        if (!alquileresActivos.containsKey(sessionId)) return "ERROR: No tienes alquiler.";
        if (!tiemposInicio.containsKey(sessionId)) return "ERROR: Tiempo no encontrado.";

        Powerbank pb = alquileresActivos.get(sessionId);
        Long tiempoInicio = tiemposInicio.get(sessionId);
        Long tiempoFin = System.currentTimeMillis();

  
        long segundosTotales = (tiempoFin - tiempoInicio) / 1000;
        double minutos = Math.ceil(segundosTotales / 60.0);
        if (minutos == 0) minutos = 1; 


        double precioFinal = 0.50 + (minutos * 0.02);

     
        String precioStr = String.format(Locale.US, "%.2f", precioFinal);
        String tiempoTexto = (segundosTotales / 60) + " min " + (segundosTotales % 60) + " s";


        pb.setAlquilada(false);
        pb.setNivelCarga(10);
        pb.setCargando(true);
        

        alquileresActivos.remove(sessionId);
        tiemposInicio.remove(sessionId);
        
        iniciarSimulacionRecarga(pb);

        return pb.getId() + "|" + tiempoTexto + "|" + precioStr + " €";
    }

    private void iniciarSimulacionRecarga(Powerbank pb) {
        new Thread(() -> {
            try {
                Thread.sleep(10000); 
                pb.setNivelCarga(100);
                pb.setCargando(false);
            } catch (InterruptedException e) {}
        }).start();
    }
    
    public String obtenerEstadoInventario() {
        StringBuilder estado = new StringBuilder();
        for (Powerbank pb : inventario) {
            String status = "<span style='color:green'>🟢 DISPONIBLE</span>";
            if (pb.isAlquilada()) status = "<span style='color:orange'>🔴 EN USO</span>";
            if (pb.isCargando()) status = "<span style='color:#FFD700'>⚡ RECARGANDO</span>";
            
            estado.append("<strong>").append(pb.getId()).append("</strong>: ").append(status).append("<br>");
        }
        return estado.toString();
    }
}