package com.connectravel.repository;

import com.connectravel.entity.AccommodationOption;
import com.connectravel.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {

}
