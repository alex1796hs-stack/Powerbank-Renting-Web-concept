package com.sharenergy.repository;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserRepository {

    @SqlUpdate("INSERT OR REPLACE INTO users (id, phone) VALUES (:id, :phone)")
    void save(String id, String phone);

    @SqlQuery("SELECT phone FROM users WHERE id = :id")
    String getPhoneById(String id);
}
