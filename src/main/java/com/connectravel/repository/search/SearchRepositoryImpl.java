package com.connectravel.repository.search;

import com.connectravel.domain.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class SearchRepositoryImpl extends QuerydslRepositorySupport implements SearchRepository {

    // 생성자
    public SearchRepositoryImpl() {
        super(Accommodation.class);
    }

    @Override
    public Page<Accommodation> searchFilteredAccommodations(String region, Set<Long> optionIds,
                                                       LocalDate startDate, LocalDate endDate,
                                                       Integer minPrice, Integer maxPrice, Pageable pageable) {
        QAccommodation accommodation = QAccommodation.accommodation;
        QRoom room = QRoom.room;
        QReservation reservation = QReservation.reservation;
        QAccommodationOption accommodationOption = QAccommodationOption.accommodationOption;

        JPQLQuery<Accommodation> query = from(accommodation);
        query.leftJoin(accommodation.rooms, room)
                .leftJoin(room.reservations, reservation);

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        // 지역 필터링
        if (region != null) {
            conditionBuilder.and(accommodation.region.eq(region));
        }

        // 옵션 필터링
        if (optionIds != null && !optionIds.isEmpty()) {
            query.join(accommodation.accommodationOptions, accommodationOption)
                    .on(accommodationOption.option.ono.in(optionIds));
        }

        // 가격 필터링
        if (minPrice != null && maxPrice != null) {
            conditionBuilder.and(room.price.between(minPrice, maxPrice));
        }

        // 입/퇴실 날짜 필터링
        if (startDate != null && endDate != null) {
            conditionBuilder.and(reservation.startDate.after(startDate)
                    .and(reservation.endDate.before(endDate)));
        }

        // 예약이 없는 방만 필터링
        conditionBuilder.and(reservation.isNull());

        query.where(conditionBuilder)
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .orderBy(getOrderSpecifiers(pageable.getSort(), accommodation, room));

        List<Accommodation> results = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(results, pageable, count);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort, QAccommodation accommodation, QRoom room) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            switch (property) {
                case "grade":
                    orders.add(new OrderSpecifier<>(direction, accommodation.grade));
                    break;
                case "lowestPrice":
                    orders.add(new OrderSpecifier<>(direction, room.price.min()));
                    break;
                case "highestPrice":
                    orders.add(new OrderSpecifier<>(direction, room.price.max()));
                    break;
            }
        }

        return orders.toArray(new OrderSpecifier[0]);
    }
}