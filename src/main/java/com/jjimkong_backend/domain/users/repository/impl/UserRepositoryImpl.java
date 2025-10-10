package com.jjimkong_backend.domain.users.repository.impl;

import com.jjimkong_backend.domain.users.repository.UserRepositoryCustom;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final EntityManager entityManager;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void enableActiveFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("activeFilter").setParameter("status", "ACTIVE");
    }
}