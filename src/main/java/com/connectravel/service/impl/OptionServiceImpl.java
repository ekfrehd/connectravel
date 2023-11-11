package com.connectravel.service.impl;

import com.connectravel.domain.dto.OptionDTO;
import com.connectravel.domain.entity.Option;
import com.connectravel.repository.OptionRepository;
import com.connectravel.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    @Override
    public OptionDTO createOption(OptionDTO optionDTO) {
        Option option = dtoToEntity(optionDTO);
        option = optionRepository.save(option);

        return entityToDto(option);
    }

    @Override
    public OptionDTO updateOption(Long ono, OptionDTO optionDTO) {
        Option option = optionRepository.findById(ono)
                .orElseThrow(() -> new EntityNotFoundException("Option not found with id: " + ono));

        // Update properties
        option.setOptionName(optionDTO.getOptionName());
        option = optionRepository.save(option);

        return entityToDto(option);
    }

    @Override
    public void deleteOption(Long ono) {
        optionRepository.deleteById(ono);
    }

    @Override
    public OptionDTO getOptionByOno(Long ono) {
        Option option = optionRepository.findById(ono)
                .orElseThrow(() -> new EntityNotFoundException("Option not found with id: " + ono));

        return entityToDto(option);
    }

    @Override
    public List<OptionDTO> getAllOptions() {
        List<Option> options = optionRepository.findAll();

        return options.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // DTO를 Entity로 변환하는 메소드
    private Option dtoToEntity(OptionDTO optionDTO) {
        return optionRepository.findByOptionName(optionDTO.getOptionName())
                .orElseGet(() -> Option.builder().optionName(optionDTO.getOptionName()).build());
    }

    // Entity를 DTO로 변환하는 메소드
    private OptionDTO entityToDto(Option option) {
        return new OptionDTO(option.getOno(), option.getOptionName());
    }

}