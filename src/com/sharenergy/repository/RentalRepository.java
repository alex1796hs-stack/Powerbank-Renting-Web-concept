package com.sharenergy.repository;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.customizer.Bind;

public interface RentalRepository {

    @SqlUpdate("INSERT INTO active_rentals (session_id, powerbank_id, start_time) VALUES (:sessionId, :powerbankId, :startTime)")
    void startRental(@Bind("sessionId") String sessionId, @Bind("powerbankId") String powerbankId, @Bind("startTime") long startTime);

    @SqlQuery("SELECT powerbank_id FROM active_rentals WHERE session_id = :sessionId")
    String getActivePowerbankId(@Bind("sessionId") String sessionId);

    @SqlQuery("SELECT start_time FROM active_rentals WHERE session_id = :sessionId")
    Long getStartTime(@Bind("sessionId") String sessionId);

    @SqlUpdate("DELETE FROM active_rentals WHERE session_id = :sessionId")
    void endRental(@Bind("sessionId") String sessionId);
}
