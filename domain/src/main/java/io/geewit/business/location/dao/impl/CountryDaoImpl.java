package io.geewit.business.location.dao.impl;

import io.geewit.business.location.entity.Country;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SuppressWarnings({"unused"})
public class CountryDaoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Country findOne(String id) {
        Country country = (Country) entityManager.createQuery("select c from Country c where c.id = :id").setParameter("id", id).getSingleResult();
        return country;
    }

    @SuppressWarnings({"unchecked"})
    public void deleteById(String id) {
        entityManager.createNativeQuery("update locations set del_flag = true where id = :id").setParameter("id", id).executeUpdate();
    }
}
