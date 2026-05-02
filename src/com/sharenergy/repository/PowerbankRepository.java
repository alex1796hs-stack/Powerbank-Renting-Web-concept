package com.sharenergy.repository;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import java.util.List;
import com.sharenergy.Powerbank;

@RegisterBeanMapper(Powerbank.class)
public interface PowerbankRepository {

    @SqlQuery("SELECT id, is_rented as alquilada, is_charging as cargando, battery_level as nivelCarga FROM powerbanks")
    List<Powerbank> getAll();

    @SqlQuery("SELECT id, is_rented as alquilada, is_charging as cargando, battery_level as nivelCarga FROM powerbanks WHERE id = :id")
    Powerbank getById(@Bind("id") String id);

    @SqlUpdate("UPDATE powerbanks SET is_rented = :alquilada, is_charging = :cargando, battery_level = :nivelCarga WHERE id = :id")
    void update(@BindBean Powerbank pb);

    @SqlQuery("SELECT id, is_rented as alquilada, is_charging as cargando, battery_level as nivelCarga FROM powerbanks WHERE is_rented = 0 AND is_charging = 0 LIMIT 1")
    Powerbank findFirstAvailable();
}
