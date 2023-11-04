package com.connectravel.service;

import com.connectravel.dto.OptionDTO;

import java.util.List;

public interface OptionService {
    OptionDTO createOption(OptionDTO optionDTO);
    OptionDTO updateOption(Long ono, OptionDTO optionDTO);
    void deleteOption(Long ono);
    OptionDTO getOptionByOno(Long ono);
    List<OptionDTO> getAllOptions();
}
