package com.sharenergy.repository;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.customizer.Bind;

public interface UserRepository {

    @SqlUpdate("INSERT OR REPLACE INTO users (id, phone) VALUES (:id, :phone)")
    void save(@Bind("id") String id, @Bind("phone") String phone);

    @SqlQuery("SELECT phone FROM users WHERE id = :id")
    String getPhoneById(@Bind("id") String id);
}
