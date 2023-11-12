package com.connectravel.repository.search;

import com.connectravel.entity.Accommodation;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

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

    @Override
    public List<Accommodation> findByOptions(Set<Long> optionIds) {
        return em.createQuery("SELECT DISTINCT a FROM Accommodation a JOIN a.accommodationOptions ao WHERE ao.option.ono IN :optionIds", Accommodation.class)
                .setParameter("optionIds", optionIds)
                .getResultList();
    }

    @Override
    public List<Accommodation> findByRoomCriteria(int price, int minimumOccupancy, int maximumOccupancy, boolean operating) {
        return em.createQuery("SELECT DISTINCT a FROM Accommodation a JOIN a.rooms r WHERE r.price <= :price AND r.minimumOccupancy >= :minimumOccupancy AND r.maximumOccupancy <= :maximumOccupancy AND r.operating = :operating", Accommodation.class)
                .setParameter("price", price)
                .setParameter("minimumOccupancy", minimumOccupancy)
                .setParameter("maximumOccupancy", maximumOccupancy)
                .setParameter("operating", operating)
                .getResultList();
    }


}
