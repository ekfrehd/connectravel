package com.connectravel.repository.search;

import com.connectravel.domain.entity.Accommodation;
import com.connectravel.domain.entity.QAccommodation;
import com.connectravel.domain.entity.QReservation;
import com.connectravel.domain.entity.QRoom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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