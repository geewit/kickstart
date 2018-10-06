package io.geewit.business.location.dao.impl;


import io.geewit.business.location.entity.Province;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SuppressWarnings({"unused"})
public class ProvinceDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Province findOne(String id) {
        Province province = (Province) entityManager.createQuery("select a from Province a where a.id = :id").setParameter("id", id).getSingleResult();
        return province;
    }
}
