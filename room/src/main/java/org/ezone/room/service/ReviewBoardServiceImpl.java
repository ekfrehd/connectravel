package org.ezone.room.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javassist.NotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ezone.room.dto.ImgDTO;
import org.ezone.room.dto.PageRequestDTO;
import org.ezone.room.dto.PageResultDTO;
import org.ezone.room.dto.ReviewBoardDTO;
import org.ezone.room.dto.ReviewReplyDTO;
import org.ezone.room.entity.Accommodation;
import org.ezone.room.entity.Member;
import org.ezone.room.entity.Reservation;
import org.ezone.room.entity.ReviewBoard;
import org.ezone.room.entity.Room;
import org.ezone.room.repository.AccommodationRepository;
import org.ezone.room.repository.MemberRepository;
import org.ezone.room.repository.ReservationRepository;
import org.ezone.room.repository.ReviewBoardImgRepository;
import org.ezone.room.repository.ReviewBoardRepository;
import org.ezone.room.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewBoardServiceImpl implements ReviewBoardService {

    @Autowired
    private ReviewBoardRepository reviewBoardRepository;

    @Autowired
    private ReviewReplyService reviewReplyService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReviewBoardImgRepository reviewBoardImgRepository;

    ModelMapper modelMapper;


    public Accommodation findAccommodationByRoomId(Long rno) {
        Room roomEntity = roomRepository.findById(rno).orElse(null);
        if (roomEntity != null) {
            return roomEntity.getAccommodation();
        }
        return null;
    }

    @Override
    public PageResultDTO<ReviewBoardDTO, ReviewBoard> getReviewBoardsAndPageInfoByAccommodationId(Long ano, PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by(Sort.Direction.DESC, "rbno");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<ReviewBoard> result = reviewBoardRepository.findByAccommodationAno(ano, pageable);

        Function<ReviewBoard, ReviewBoardDTO> fn = (reviewBoard -> {
            Member member = reviewBoard.getMember();
            List<ReviewReplyDTO> replyDTOs = reviewReplyService.getList(reviewBoard.getRbno());
            return entityToDTO(reviewBoard, replyDTOs);
        });

        return new PageResultDTO<>(result, fn);
    }

    @Transactional
    @Override
    public Long register(ReviewBoardDTO dto) throws NotFoundException {
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findByEmail(dto.getWriterEmail()));
        if (!memberOptional.isPresent()) {
            throw new NotFoundException("Member not found");
        }
        Member member = memberOptional.get();

        Optional<Accommodation> accommodationOptional = accommodationRepository.findById(dto.getAno());
        if (!accommodationOptional.isPresent()) {
            throw new NotFoundException("Accommodation not found");
        }
        Accommodation accommodation = accommodationOptional.get();

        Optional<Room> roomOptional = roomRepository.findById(dto.getRno());
        if (!roomOptional.isPresent()) {
            throw new NotFoundException("Room not found");
        }
        Room room = roomOptional.get();

        Optional<Reservation> reservationOptional = reservationRepository.findById(dto.getRvno());
        if (!reservationOptional.isPresent()) {
            throw new NotFoundException("Reservation not found");
        }
        Reservation reservation = reservationOptional.get();

        double currentGrade = accommodation.getGrade();
        double newGrade = dto.getGrade();
        int currentCount = accommodation.getReviewCount();

        double avarageGrade = ((currentGrade * currentCount) + newGrade) / (currentCount + 1);

        accommodation.setReviewCount(currentCount + 1);
        accommodation.setGrade(avarageGrade);
        accommodationRepository.saveAndFlush(accommodation);

        ReviewBoard reviewBoard = dtoToEntity(dto, member, reservation);
        reviewBoardRepository.save(reviewBoard);
        return reviewBoard.getRbno();
    }

    @Override
    public ReviewBoardDTO get(Long bno) {
        return null;
    }

    @Transactional
    @Override
    public void modify(ReviewBoardDTO reviewBoardDTO) {
        ReviewBoard reviewBoard = reviewBoardRepository.findById(reviewBoardDTO.getRbno()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글이 없습니다."));
        reviewBoard.changeContent(reviewBoardDTO.getContent());
        reviewBoardRepository.save(reviewBoard);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        reviewBoardRepository.deleteById(bno);
    }

    @Override
    public List<ImgDTO> getImgList(Long rbno) {
        List<ImgDTO> list = new ArrayList<>();
        ReviewBoard entity = reviewBoardRepository.findById(rbno).get();
        reviewBoardImgRepository.GetImgbyrbno(entity).forEach(i -> { //이미지는 룸을 참조하고 있다 그러니 이미지가 참조하는 룸에 해당하는 모든 이미지를 불러온다
            ImgDTO imgDTO = modelMapper.map(i,ImgDTO.class); //dto변환
            list.add(imgDTO); // list화
        });
        return list;
    }
}