package com.connectravel.service;

import com.connectravel.dto.OptionDTO;
import com.connectravel.entity.Option;
import com.connectravel.repository.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OptionServiceTest {

    private static final Logger log = LoggerFactory.getLogger(OptionServiceTest.class);

    @Autowired
    private OptionService optionService;

    @Autowired
    private OptionRepository optionRepository;

    private OptionDTO optionDTO;
    private Option option;

    @BeforeEach
    void setUp() {
        optionDTO = new OptionDTO(1L,"Wi-Fi");
        option = new Option(); // 기본 생성자 사용 또는 빌더를 사용하여 인스턴스 생성
        option.setOptionName("Wi-Fi");
    }

    @Test
    @Transactional
    void createOptionTest() {
        // 테스트 실행
        OptionDTO savedOptionDTO = optionService.createOption(optionDTO);

        log.debug("Created Option = {} ", savedOptionDTO);

        // 검증
        assertNotNull(savedOptionDTO.getOno()); // 실제로 DB에 저장되어 ID가 생성됐는지 확인
        assertEquals(optionDTO.getOptionName(), savedOptionDTO.getOptionName());
    }

    @Test
    @Transactional
    void updateOptionTest() {
        // 초기 데이터 저장
        Option savedOption = optionRepository.save(option);

        log.debug("Before update: {}", savedOption);

        // 수정할 DTO 생성
        OptionDTO optionDTOToUpdate = new OptionDTO(savedOption.getOno(), "세탁기");

        // 테스트 실행
        OptionDTO updatedOptionDTO = optionService.updateOption(savedOption.getOno(), optionDTOToUpdate);

        log.debug("updated Option : {} ", updatedOptionDTO);

        // 검증
        assertNotNull(updatedOptionDTO);
        assertEquals(optionDTOToUpdate.getOptionName(), updatedOptionDTO.getOptionName());
    }

    @Test
    @Transactional
    void getOptionByIdTest() {
        // 초기 데이터 저장
        Option savedOption = optionRepository.save(option);

        // 테스트 실행
        OptionDTO foundOptionDTO = optionService.getOptionByOno(savedOption.getOno());

        log.debug("Found Option: {}", foundOptionDTO);

        // 검증
        assertNotNull(foundOptionDTO);
        assertEquals(savedOption.getOptionName(), foundOptionDTO.getOptionName());
    }

    @Test
    @Transactional
    void deleteOptionTest() {
        // 초기 데이터 저장
        Option savedOption = optionRepository.save(option);
        assertNotNull(savedOption.getOno());

        // 삭제 전 데이터 확인
        log.debug("Saved Option for delete: {}", savedOption);

        // 테스트 실행
        optionService.deleteOption(savedOption.getOno());

        // 삭제가 됐는지 검증
        assertFalse(optionRepository.findById(savedOption.getOno()).isPresent());

        // 삭제 후 데이터 확인
        log.debug("Option deleted, id: {}", savedOption.getOno());
    }

    @Test
    @Transactional
    void getAllOptionsTest() {
        // 초기 데이터 저장
        Option firstOption = optionRepository.save(new Option(1L, "Wi-Fi"));
        Option secondOption = optionRepository.save(new Option(2L, "Breakfast"));
        Option thirdOption = optionRepository.save(new Option(3L, "Parking"));

        // 테스트 실행
        List<OptionDTO> options = optionService.getAllOptions();

        // 검증
        assertNotNull(options);
        assertEquals(3, options.size());

        log.debug("Retrieved options: {}", options);
    }
}