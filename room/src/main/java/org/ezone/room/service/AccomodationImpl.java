package org.ezone.room.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.dto.*;
import org.ezone.room.entity.*;
import org.ezone.room.repository.AccommodationImgRepository;
import org.ezone.room.repository.AccommodationRepository;
import org.ezone.room.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class AccomodationImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final AccommodationImgRepository accommodationImgRepository;
    private final MemberRepository memberRepository; // Member 객체를 불러오기 위해 필요

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Long register(AccommodationDTO dto) {
        // memberRepository를 넣으면 불러오기 끝!
        // 보통 dto로 불러올 수 있는 내용이면 dto를 넣고, dto로 불가하면 repository를 실행하면 됨!
        AccommodationEntity accommodationEntity = dtoToEntity(dto, memberRepository);

        accommodationRepository.save(accommodationEntity);

        return accommodationEntity.getAno();
    }

    @Override // 숙소번호 찾기
    public AccommodationDTO findByAno(Long ano){
        AccommodationEntity accommodationEntity = accommodationRepository.findByAno(ano);
        AccommodationDTO accommodation = AccommodationDTO
                .builder()
                .name(accommodationEntity.getName())
                .adminname(accommodationEntity.getAdminname())
                .email(accommodationEntity.getEmail())
                .address(accommodationEntity.getAddress())
                .tel(accommodationEntity.getTel())
                .intro(accommodationEntity.getIntro())
                .postal(accommodationEntity.getPostal())
                .ano(accommodationEntity.getAno())
                .region(accommodationEntity.getRegion())
                .grade(accommodationEntity.getGrade())
                .content(accommodationEntity.getContent())
                .reviewcount(accommodationEntity.getReviewcount())
                .count(accommodationEntity.getCount())
                .build();
        return accommodation;
    }

    @Override
    public List<Object[]> list(LocalDate StartDate, LocalDate EndDate) {
        List<Object[]> list = accommodationRepository.findByRv_LowPrice(StartDate,EndDate);
        List<Object[]> getlist = new ArrayList<>();
        list.forEach(i -> {
            AccommodationDTO dto = modelMapper.map(i[0],AccommodationDTO.class);
            Object[] objects = {dto,i[1],i[2]};
            getlist.add(objects);
        });
        return getlist;
    }

    @Override // MemberId가 운영하는 숙소찾기
    public AccommodationDTO findAccommodationByMemberId(String memberId) {
        AccommodationEntity accommodationEntity = accommodationRepository.findAccommodationByMemberId(memberId);
        Member member = accommodationEntity.getMember();
        return entityToDto(accommodationEntity, member);
    }

    @Override
    public PageResultDTO<AccommodationDTO, Object[]> searchAccommodationList
            (PageRequestDTO pageRequestDTO, String keyword, String category, String region,
             LocalDate startDate, LocalDate endDate, Integer inputedMinPrice, Integer inputedMaxPrice) {
        Sort sort = Sort.by(Sort.Direction.DESC, "ano");
        String[] type = pageRequestDTO.getType();
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<Object[]> result = accommodationRepository.searchPageAccommodation
                (type, keyword, category, region, startDate, endDate,
                        inputedMinPrice, inputedMaxPrice, pageable);

        Function<Object[], AccommodationDTO> fn = (objectArr -> {
            AccommodationEntity accommodation = (AccommodationEntity) objectArr[0];
            RoomEntity roomEntity= (RoomEntity) objectArr[1];
            Integer minPrice = (Integer) objectArr[2];
            AccommodationDTO accommodationDTO = entityToDtoSearch(accommodation, roomEntity, minPrice);
            List<ImgDTO> imgDTOList = getImgList(accommodation.getAno()); // 이미지 리스트화 게시물 번호별 -
            accommodationDTO.setImgDTOList(imgDTOList);
            return accommodationDTO;
        });

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public List<ImgDTO> getImgList(Long ano) {
        List<ImgDTO> list = new ArrayList<>();
        AccommodationEntity entity = accommodationRepository.findByAno(ano);
        accommodationImgRepository.GetImgbyAccommodationId(entity).forEach(i -> { //이미지는 룸을 참조하고 있다 그러니 이미지가 참조하는 룸에 해당하는 모든 이미지를 불러온다
            ImgDTO imgDTO = modelMapper.map(i,ImgDTO.class); //dto변환
            list.add(imgDTO); // list화
        });
        return list;
    }


    @Override
    public List<AccommodationImgDTO> findAccommodationWithImages() {
        List<AccommodationImgDTO> accommodationImgDTOS = new ArrayList<>();
        accommodationRepository.findAccommodationWithImages().forEach(i -> {
            AccommodationDTO accommodationDTO = modelMapper.map(i[0], AccommodationDTO.class); //repository 에서 room 과 img를 조인해서 알맞는 룸과 이미지를 가져온다.
            //Object[] 형태이기때문에 서로 쪼갠다. 그리고 프톤트에 보내기 위해서 dto클래스로 변환한다.
            ImgDTO imgDTO = (i[1] != null) ? modelMapper.map(i[1], ImgDTO.class) : new ImgDTO(); //예외가 발생하니 삼항 연산자를 통해 검증함.(try catch문을 쓰면
            // 종료시켜버리기 떄문에 안씀)
            AccommodationImgDTO accommodationImgDTO = AccommodationImgDTO.builder().
                    accommodationDTO(accommodationDTO).
                    img(imgDTO).
                    build();
            //합체 ( room dto + img dto) 분할해서 보내는거보다는 합쳐서 보내는게 적합하다고 생각함.
            accommodationImgDTOS.add(accommodationImgDTO); //리스트 add
        });
        return accommodationImgDTOS; //return room + img list
    }


    @Override
    public void modify(AccommodationDTO dto) {
        AccommodationEntity accommodation = accommodationRepository.findByEmail(dto.getEmail());
        accommodation.setName(dto.getName());
        accommodation.setAddress(dto.getAddress());
        accommodation.setAdminname(dto.getAdminname());
        accommodation.setCategory(dto.getCategory());
        accommodation.setTel(dto.getTel());
        accommodation.setIntro(dto.getIntro());
        accommodation.setPostal(dto.getPostal());
        accommodation.setContent(dto.getContent());
        accommodation.setMember(memberRepository.findByEmail(dto.getEmail()));
        accommodationRepository.save(accommodation);
    }

    @Override
    public void remove(Long ano) {
        accommodationRepository.deleteById(ano);
    }
}
