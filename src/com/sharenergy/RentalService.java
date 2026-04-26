package com.sharenergy;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import com.sharenergy.config.DatabaseConfig;
import com.sharenergy.repository.PowerbankRepository;
import com.sharenergy.repository.UserRepository;
import com.sharenergy.repository.RentalRepository;
import org.jdbi.v3.core.Jdbi;

public class RentalService {
    private Jdbi jdbi;

    public RentalService() {
        this.jdbi = DatabaseConfig.getJdbi();
    }

    public void registrarUsuario(String sessionId, String telefono) {
        jdbi.useExtension(UserRepository.class, repo -> repo.save(sessionId, telefono));
        System.out.println("📝 Usuario registrado persistentemente: " + telefono);
    }

    public String iniciarAlquiler(String sessionId) {
        return jdbi.withExtension(RentalRepository.class, rentalRepo -> {
            if (rentalRepo.getActivePowerbankId(sessionId) != null) {
                return "ERROR: Ya tienes un alquiler activo.";
            }

            return jdbi.withExtension(PowerbankRepository.class, pbRepo -> {
                Powerbank pb = pbRepo.findFirstAvailable();
                if (pb != null) {
                    pb.setAlquilada(true);
                    pbRepo.update(pb);
                    rentalRepo.startRental(sessionId, pb.getId(), System.currentTimeMillis());
                    
                    String tel = jdbi.withExtension(UserRepository.class, uRepo -> uRepo.getPhoneById(sessionId));
                    System.out.println("🔋 Alquiler iniciado: " + tel + " -> " + pb.getId());
                    return "OK|" + pb.getId();
                }
                return "ERROR: No hay baterías disponibles.";
            });
        });
    }

    public String obtenerListaJson() {
        List<Powerbank> inventario = jdbi.withExtension(PowerbankRepository.class, PowerbankRepository::getAll);
        
        // Usamos streams para mapear a un formato simple. 
        // Nota: Javalin puede serializar objetos directamente, pero mantenemos el formato exacto que pide el front.
        return "[" + inventario.stream()
                .map(pb -> String.format("{\"id\":\"%s\", \"disponible\":%b}", 
                        pb.getId(), !pb.isAlquilada() && !pb.isCargando()))
                .collect(Collectors.joining(",")) + "]";
    }

    public String iniciarAlquilerEspecifico(String sessionId, String batteryId) {
        return jdbi.withExtension(RentalRepository.class, rentalRepo -> {
            if (rentalRepo.getActivePowerbankId(sessionId) != null) return "ERROR: Ya tienes un alquiler activo.";

            return jdbi.withExtension(PowerbankRepository.class, pbRepo -> {
                Powerbank pb = pbRepo.getById(batteryId);
                if (pb != null) {
                    if (!pb.isAlquilada() && !pb.isCargando()) {
                        pb.setAlquilada(true);
                        pbRepo.update(pb);
                        rentalRepo.startRental(sessionId, pb.getId(), System.currentTimeMillis());
                        return "OK|" + pb.getId();
                    } else {
                        return "ERROR: Esa batería ya no está disponible.";
                    }
                }
                return "ERROR: Batería no encontrada.";
            });
        });
    }

    public String finalizarAlquiler(String sessionId) {
        return jdbi.withExtension(RentalRepository.class, rentalRepo -> {
            String pbId = rentalRepo.getActivePowerbankId(sessionId);
            Long tiempoInicio = rentalRepo.getStartTime(sessionId);

            if (pbId == null || tiempoInicio == null) return "ERROR: No tienes un alquiler activo.";

            long segundosTotales = (System.currentTimeMillis() - tiempoInicio) / 1000;
            double minutos = Math.ceil(segundosTotales / 60.0);
            if (minutos == 0) minutos = 1;
            double precioFinal = 0.50 + (minutos * 0.02);

            String precioStr = String.format(Locale.US, "%.2f", precioFinal);
            String tiempoTexto = (segundosTotales / 60) + " min " + (segundosTotales % 60) + " s";

            jdbi.useExtension(PowerbankRepository.class, pbRepo -> {
                Powerbank pb = pbRepo.getById(pbId);
                pb.setAlquilada(false);
                pb.setNivelCarga(10);
                pb.setCargando(true);
                pbRepo.update(pb);
                
                iniciarSimulacionRecarga(pb);
            });

            rentalRepo.endRental(sessionId);
            return pbId + "|" + tiempoTexto + "|" + precioStr + " €";
        });
    }

    private void iniciarSimulacionRecarga(Powerbank pb) {
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                jdbi.useExtension(PowerbankRepository.class, pbRepo -> {
                    Powerbank updatedPb = pbRepo.getById(pb.getId());
                    updatedPb.setNivelCarga(100);
                    updatedPb.setCargando(false);
                    pbRepo.update(updatedPb);
                });
            } catch (InterruptedException e) {}
        }).start();
    }

    public String obtenerEstadoInventario() {
        List<Powerbank> inventario = jdbi.withExtension(PowerbankRepository.class, PowerbankRepository::getAll);
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