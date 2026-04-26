package com.sharenergy.config;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DatabaseConfig {
    private static Jdbi jdbi;

    public static Jdbi getJdbi() {
        if (jdbi == null) {
            // Usamos una base de datos local llamada sharenergy.db
            jdbi = Jdbi.create("jdbc:sqlite:sharenergy.db");
            jdbi.installPlugin(new SqlObjectPlugin());
            
            initializeDatabase();
        }
        return jdbi;
    }

    private static void initializeDatabase() {
        jdbi.useHandle(handle -> {
            // Tabla de Baterías
            handle.execute("CREATE TABLE IF NOT EXISTS powerbanks (" +
                    "id TEXT PRIMARY KEY, " +
                    "is_rented BOOLEAN, " +
                    "is_charging BOOLEAN, " +
                    "battery_level INTEGER)");

            // Tabla de Usuarios
            handle.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id TEXT PRIMARY KEY, " +
                    "phone TEXT)");

            // Tabla de Alquileres Activos
            handle.execute("CREATE TABLE IF NOT EXISTS active_rentals (" +
                    "session_id TEXT PRIMARY KEY, " +
                    "powerbank_id TEXT, " +
                    "start_time LONG, " +
                    "FOREIGN KEY(powerbank_id) REFERENCES powerbanks(id))");

            // Insertar datos iniciales si la tabla está vacía
            Integer count = handle.createQuery("SELECT COUNT(*) FROM powerbanks")
                    .mapTo(Integer.class)
                    .one();
            
            if (count == 0) {
                handle.execute("INSERT INTO powerbanks VALUES ('PB-001', 0, 0, 100)");
                handle.execute("INSERT INTO powerbanks VALUES ('PB-002', 0, 0, 100)");
                handle.execute("INSERT INTO powerbanks VALUES ('PB-003', 0, 0, 85)");
            }
        });
    }
}
