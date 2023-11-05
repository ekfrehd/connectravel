package com.connectravel.service;

import com.connectravel.dto.ImgDTO;
import com.connectravel.dto.PageRequestDTO;
import com.connectravel.dto.PageResultDTO;
import com.connectravel.dto.TourBoardReivewDTO;
import com.connectravel.entity.Member;
import com.connectravel.entity.TourBoard;
import com.connectravel.entity.TourBoardReview;
import com.connectravel.repository.MemberRepository;
import com.connectravel.repository.TourBoardReivewImgRepository;
import com.connectravel.repository.TourBoardRepository;
import com.connectravel.repository.TourBoardReviewRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private final TourBoardReivewImgRepository tourBoardReivewImgRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResultDTO<TourBoardReivewDTO, TourBoardReview> getTourReviewBoardsAndPageInfoByTourBoardId(Long tbno, PageRequestDTO pageRequestDTO) {
        Sort sort = Sort.by(Sort.Direction.DESC, "tbrno");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(), sort);

        Page<TourBoardReview> result = tourBoardReviewRepository.findByTourBoard_Tbno(tbno, pageable);

        /*Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member authMember;
        if (principal instanceof CustomUserDetails) {
            authMember = ((CustomUserDetails) principal).getMember();
        } else {
            authMember = null;
        }*/

        Function<TourBoardReview, TourBoardReivewDTO> fn = (tourBoardReview -> {
            Member member = tourBoardReview.getMember();
            TourBoardReivewDTO tourBoardReview1 = entityToDTO(tourBoardReview, member);

            /*if (authMember == null) {
                tourBoardReview1.setMemberState(false);
            } else {
                if (authMember != null && authMember.getId().equals(member.getId())) {
                    tourBoardReview1.setMemberState(true);
                } else {
                    tourBoardReview1.setMemberState(false);
                }
            }*/

            return tourBoardReview1;
        });

        return new PageResultDTO<>(result, fn);
    }


    @Transactional
    @Override
    public Long register(TourBoardReivewDTO dto) throws NotFoundException {
        //Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findByEmail(dto.getWriterEmail()));
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findByEmail("sample@sample.com"));
        if (!memberOptional.isPresent()) {
            throw new NotFoundException("Member not found");
        }
        Member member = memberOptional.get(); //멤버 정보 등록

        Optional<TourBoard> tourBoardOptional = tourBoardRepository.findById(dto.getTbno()); // 받은 tbno 값과 일치하는 게시글 정보를 찾는다.
        if (!tourBoardOptional.isPresent()) {throw new NotFoundException("TourBoard not found");}
        TourBoard tourBoard = tourBoardOptional.get();

        double currentGrade = tourBoard.getGrade(); // 현재 평점
        double newGrade = dto.getGrade(); // 새 평점
        int currentCount = tourBoard.getReviewCount(); // 현재 리뷰

        double avarageGrade = ((currentGrade * currentCount) + newGrade) / (currentCount + 1); // 평균 평점 = 현재 평점 * 현재 리뷰 수 / 현재 리뷰 수 + 1

        tourBoard.setReviewCount(currentCount + 1); //게시글의 리뷰 수를 올린다.
        tourBoard.setGrade(avarageGrade); // 게시글의 평균 평점 입력
        tourBoardRepository.saveAndFlush(tourBoard); // DB 반영

        TourBoardReview tourBoardReview = dtoToEntity(dto, tourBoard, member); // 리뷰 정보, 게시글 정보, 회원 정보 입력
        tourBoardReviewRepository.save(tourBoardReview); // DB 반영
        return tourBoardReview.getTbrno();
    }

    @Override
    public TourBoardReivewDTO get(Long tbrno) {
        Optional<TourBoardReview> result = tourBoardReviewRepository.findById(tbrno);
        Member member = memberRepository.findByEmail("sample@sample.com");

        return result.isPresent() ? entityToDTO(result.get(), member) : null;
    }

    @Override
    public void modify(TourBoardReivewDTO tourBoardReivewDTO) {

        Optional<TourBoardReview> optionalTourBoardReview = tourBoardReviewRepository.findById(tourBoardReivewDTO.getTbrno());
        TourBoardReview tourBoardReview = optionalTourBoardReview.orElseThrow(() -> new NoSuchElementException("게시글이 존재하지 않습니다."));

        tourBoardReview.changeContent(tourBoardReivewDTO.getContent());
        tourBoardReview.changeGrade(tourBoardReivewDTO.getGrade());
        tourBoardReviewRepository.save(tourBoardReview); //수정된 객체를 repository에 저장
    }

    @Override
    @Transactional
    public void removeWithReplies(Long tbrno, Long tbno) throws NotFoundException {

        Optional<TourBoard> tourBoardOptional = tourBoardRepository.findById(tbno); // 게시글 정보를 가져온다.
        if (!tourBoardOptional.isPresent()) {throw new NotFoundException("TourBoard not found");}
        TourBoard tourBoard = tourBoardOptional.get();

        Optional<TourBoardReview> tourBoardReview = tourBoardReviewRepository.findById(tbrno); // 게시글 리뷰 정보를 가져온다.

        double currentGrade = tourBoard.getGrade(); // 현재 평점
        double newGrade = tourBoardReview.get().getGrade(); // 새 평점
        int currentCount = tourBoard.getReviewCount(); // 현재 리뷰 수

        if (currentCount > 1) { //리뷰 수가 1개 이상일 때
            double avarageGrade = ((currentGrade * currentCount) - newGrade) / (currentCount - 1); //평균 평점 계산
            tourBoard.setGrade(avarageGrade); // 평균 평점 계산
            tourBoard.setReviewCount(currentCount - 1); // 리뷰 수 계산
        }
        else {
            tourBoardReviewRepository.deleteById(tbrno); // 리뷰를 삭제한다.
            tourBoard.setGrade(0); // 등급 초기화
            tourBoard.setReviewCount(0); // 리뷰 수 초기화
            tourBoardRepository.save(tourBoard);
            return;
        }

        tourBoardRepository.save(tourBoard); // DB 반영
        tourBoardReviewRepository.deleteById(tbrno); // 리뷰 삭제
    }

    @Override
    public List<ImgDTO> getImgList(Long tbrno) {
        List<ImgDTO> list = new ArrayList<>();
        TourBoardReview entity = tourBoardReviewRepository.findById(tbrno).get();
        log.info("testetwtete" + entity);
        /*tourBoardReivewImgRepository.GetImgbyTourBoardReviewId(entity).forEach(i -> {
            ImgDTO imgDTO = modelMapper.map(i, ImgDTO.class); //dto변환
            list.add(imgDTO); // list화
        });*/
        return list;
    }
}


