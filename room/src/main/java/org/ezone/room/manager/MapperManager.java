package org.ezone.room.manager;

import org.ezone.room.dto.ReservationDTO;
import org.ezone.room.dto.RoomDTO;
import org.ezone.room.entity.ReservationEntity;
import org.ezone.room.entity.RoomEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperManager extends ModelMapper {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(RoomEntity.class, RoomDTO.class)
                .addMapping(src -> src.getAccommodationEntity(),RoomDTO::setAcc_id);

        // 필요한 설정 등록
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        //모델메퍼는 성능상 참조 객체는 매핑하지 않음. 수동적으로 명시해야지 매핑함.
        //RoomDTO를 Entity로 변환할때, 참조 객체 Accommodation도 매핑하겠습니다. 명시
        modelMapper.createTypeMap(RoomDTO.class, RoomEntity.class)
                .addMapping(src -> src.getAcc_id(),RoomEntity::setAccommodationEntity);
        //ReservationEntity에서 dto로 변환할때 Accentity를 Anodto로 변환.
        modelMapper.createTypeMap(ReservationEntity.class, ReservationDTO.class)
                .addMapping(src -> src.getRvno(), ReservationDTO::setAcc);
        return modelMapper;
    }
}
