package com.connectravel.repository;

import com.connectravel.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
    Optional<Option> findByOptionName(String optionName);
}
