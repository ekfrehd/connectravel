package com.connectravel.repository;

import com.connectravel.entity.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OptionRepositoryTest {
    // 'Accommodation' 과 연관 없이 'Option' 자체의 CRUD TEST

    private static final Logger log = LoggerFactory.getLogger(OptionRepositoryTest.class);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OptionRepository optionRepository;

    @BeforeEach
    void setUp() {
        // 초기 옵션 데이터 설정
        Option option1 = Option.builder()
                .optionName("옵션1")
                .build();

        Option option2 = Option.builder()
                .optionName("옵션2")
                .build();

        entityManager.persist(option1);
        entityManager.persist(option2);
    }

    @Test
    void saveOption() {
        Option newOption = Option.builder()
                .optionName("새 옵션")
                .build();

        Option savedOption = optionRepository.save(newOption);

        log.debug("저장된 옵션: {}", savedOption);

        assertThat(savedOption).isNotNull();
        assertThat(savedOption.getOptionName()).isEqualTo("새 옵션");
    }

    @Test
    void findOptionByName() {
        Option foundOption = optionRepository.findByOptionName("옵션1").orElse(null);

        log.debug("찾은 옵션: {}", foundOption);

        assertThat(foundOption).isNotNull();
    }

    @Test
    void updateOption() {
        Option existingOption = optionRepository.findByOptionName("옵션1").orElse(null);
        assertThat(existingOption).isNotNull();

        existingOption.setOptionName("수정된 옵션1");
        Option updatedOption = optionRepository.save(existingOption);

        log.debug("수정된 옵션: {}", updatedOption);

        assertThat(updatedOption.getOptionName()).isEqualTo("수정된 옵션1");
    }

    @Test
    void deleteOption() {
        Option existingOption = optionRepository.findByOptionName("옵션2").orElse(null);
        assertThat(existingOption).isNotNull();

        optionRepository.delete(existingOption);
        Option deletedOption = optionRepository.findByOptionName("옵션2").orElse(null);

        log.debug("삭제 후 옵션: {}", deletedOption);

        assertThat(deletedOption).isNull();
    }


}
