package com.connectravel.repository.search;

import com.connectravel.entity.Accommodation;

import java.util.List;

public interface SearchRepository {
    List<Accommodation> findByRegion(String Region);
}
