package com.connectravel.repository.search;

import com.connectravel.domain.entity.Accommodation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SearchRepositoryImpl implements SearchRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Accommodation> findByRegion(String Region) {
        return em.createQuery("SELECT a FROM Accommodation a WHERE a.region = :Region", Accommodation.class)
                .setParameter("Region", Region)
                .getResultList();
    }

}