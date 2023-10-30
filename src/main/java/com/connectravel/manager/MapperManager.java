package com.connectravel.manager;

//import com.connectravel.dto.ReservationDTO;

import com.connectravel.dto.ReservationDTO;
import com.connectravel.dto.RoomDTO;
import com.connectravel.entity.Reservation;
import com.connectravel.entity.Room;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperManager extends ModelMapper {

    @Bean
    public ModelMapper modelMapper () {
        ModelMapper modelMapper = new ModelMapper ();

        modelMapper.createTypeMap (Room.class, RoomDTO.class).addMapping (src -> src.getAccommodation (), RoomDTO::setAcc_id);

        // 필요한 설정 등록
        modelMapper.getConfiguration ().setMatchingStrategy (MatchingStrategies.STANDARD);
        //모델메퍼는 성능상 참조 객체는 매핑하지 않음. 수동적으로 명시해야지 매핑함.
        //RoomDTO를 Entity로 변환할때, 참조 객체 Accommodation도 매핑하겠습니다. 명시
        modelMapper.createTypeMap (RoomDTO.class, Room.class).addMapping (src -> src.getAcc_id (), Room::setAccommodation);
        //ReservationEntity에서 dto로 변환할때 Accentity를 Anodto로 변환.
        modelMapper.createTypeMap (Reservation.class, ReservationDTO.class).addMapping (src -> src.getRvno (), ReservationDTO::setAcc);
        return modelMapper;
    }
}
