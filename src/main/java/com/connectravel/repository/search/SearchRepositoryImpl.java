package com.connectravel.repository.search;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SearchRepositoryImpl implements SearchRepository {

    @Autowired
    private JPAQueryFactory queryFactory;
}
