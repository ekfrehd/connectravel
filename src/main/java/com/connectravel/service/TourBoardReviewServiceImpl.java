package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.TourBoardReviewDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBoardRepository;
import com.connectravel.repository.TourBoardReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class TourBoardReviewServiceImpl implements TourBoardReviewService {

    private final TourBoardReviewRepository tourBoardReviewRepository;
    private final TourBoardRepository tourBoardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public Long createTourBoardReview(TourBoardReviewDTO dto, String email) { // email 매개변수 추가

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        TourBoard tourBoard = tourBoardRepository.findById(dto.getTbno())
                .orElseThrow(() -> new EntityNotFoundException("TourBoard not found"));

        double currentGrade = tourBoard.getGrade(); // 현재 평점
        double newGrade = dto.getGrade(); // 새 평점
        int currentCount = tourBoard.getReviewCount(); // 현재 리뷰

        double avarageGrade = ((currentGrade * currentCount) + newGrade) / (currentCount + 1); // 평균 평점 = 현재 평점 * 현재 리뷰 수 / 현재 리뷰 수 + 1

        tourBoard.setReviewCount(currentCount + 1); //게시글의 리뷰 수를 올린다.
        tourBoard.setGrade(avarageGrade); // 게시글의 평균 평점 입력
        tourBoardRepository.saveAndFlush(tourBoard); // DB 반영

        TourBoardReview tourBoardReview = dtoToEntity(dto, tourBoard, member);
        tourBoardReviewRepository.save(tourBoardReview);
        return tourBoardReview.getTbrno();
    }

    @Override
    @Transactional
    public TourBoardReviewDTO getTourBoardReview(Long tbrno) {
        TourBoardReview tourBoardReview = tourBoardReviewRepository.findById(tbrno)
                .orElseThrow(() -> new EntityNotFoundException("TourBoardReview not found"));
        Member member = tourBoardReview.getMember();
        return entityToDTO(tourBoardReview, member);
    }


    @Override
    public void updateTourBoardReview(TourBoardReviewDTO tourBoardReviewDTO) {

        Optional<TourBoardReview> optionalTourBoardReview = tourBoardReviewRepository.findById(tourBoardReviewDTO.getTbrno());
        TourBoardReview tourBoardReview = optionalTourBoardReview.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));

        tourBoardReview.changeContent(tourBoardReviewDTO.getContent());
        tourBoardReview.changeGrade(tourBoardReviewDTO.getGrade());
        tourBoardReviewRepository.save(tourBoardReview); //수정된 객체를 repository에 저장
    }

    @Override
    @Transactional
    public void deleteTourBoardReviewWithReplies(Long tbrno) {
        // TourBoardReview 조회
        TourBoardReview tourBoardReview = tourBoardReviewRepository.findById(tbrno)
                .orElseThrow(() -> new EntityNotFoundException("TourBoardReview not found"));

        // TourBoardReview 삭제
        tourBoardReviewRepository.deleteById(tbrno);

    }



    @Override
    public List<ImgDTO> listTourBoardReviewImages(Long tbrno) {
        List<ImgDTO> list = new ArrayList<>();
        TourBoardReview entity = tourBoardReviewRepository.findById(tbrno).get();

        return list;
    }

    @Override
    public PageResultDTO<TourBoardReviewDTO, TourBoardReview> getPaginatedTourBoardReviews(Long tbno, PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by(Sort.Direction.DESC, "tbrno");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<TourBoardReview> result = tourBoardReviewRepository.findByTourBoardTbno(tbno, pageable);

        Function<TourBoardReview, TourBoardReviewDTO> entityToDtoFunction = (entity -> {
            Member member = entity.getMember();
            TourBoardReviewDTO dto = entityToDTO(entity, member);
            return dto;
        });

        return new PageResultDTO<>(result, entityToDtoFunction);
    }


    /* 변환 메서드 */
    private TourBoardReview dtoToEntity(TourBoardReviewDTO dto, TourBoard tourBoard, Member member) {

        return TourBoardReview.builder()
                .tbrno(dto.getTbrno())
                .content(dto.getContent())
                .grade(dto.getGrade())
                .member(member)
                .tourBoard(tourBoard)
                .build();
    }

    private TourBoardReviewDTO entityToDTO(TourBoardReview tourBoardReview, Member member) {

        return TourBoardReviewDTO.builder()
                .tbrno(tourBoardReview.getTbrno())
                .content(tourBoardReview.getContent())
                .grade(tourBoardReview.getGrade())
                .writerEmail(member.getEmail())
                .writerName(member.getNickName())
                .regDate(tourBoardReview.getRegTime())
                .modDate(tourBoardReview.getModTime())
                .build();
    }
}


