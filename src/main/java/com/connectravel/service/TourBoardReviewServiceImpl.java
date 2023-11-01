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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class TourBoardReviewServiceImpl implements TourBoardReviewService {

    @Autowired
    TourBoardReviewRepository tourBoardReviewRepository;
    @Autowired
    TourBoardRepository tourBoardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TourBoardReivewImgRepository tourBoardReivewImgRepository;
    @Autowired
    ModelMapper modelMapper;

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
        Optional<Member> memberOptional = Optional.ofNullable(memberRepository.findByEmail(dto.getWriterEmail()));
        if (!memberOptional.isPresent()) {
            throw new NotFoundException("Member not found");
        }
        Member member = memberOptional.get();

        Optional<TourBoard> tourBoardOptional = tourBoardRepository.findById(dto.getTbno());
        if (!tourBoardOptional.isPresent()) {
            throw new NotFoundException("TourBoard not found");
        }
        TourBoard tourBoard = tourBoardOptional.get();

        double currentGrade = tourBoard.getGrade();
        double newGrade = dto.getGrade();
        int currentCount = tourBoard.getReviewCount();

        double avarageGrade = ((currentGrade * currentCount) + newGrade) / (currentCount + 1);

        tourBoard.setReviewCount(currentCount + 1);
        tourBoard.setGrade(avarageGrade);
        tourBoardRepository.saveAndFlush(tourBoard);

        TourBoardReview tourBoardReview = dtoToEntity(dto, tourBoard, member);
        tourBoardReviewRepository.save(tourBoardReview);
        return tourBoardReview.getTbrno();
    }

    @Override
    public TourBoardReivewDTO get(Long bno) {
        return null;
    }

    @Override
    public void modify(TourBoardReivewDTO tourBoardReivewDTO) {

    }

    @Override
    @Transactional
    public void removeWithReplies(Long tbrno, Long tbno) throws NotFoundException {

        Optional<TourBoard> tourBoardOptional = tourBoardRepository.findById(tbno);
        if (!tourBoardOptional.isPresent()) {
            throw new NotFoundException("TourBoard not found");
        }
        TourBoard tourBoard = tourBoardOptional.get();
        Optional<TourBoardReview> tourBoardReview = tourBoardReviewRepository.findById(tbrno);

        double currentGrade = tourBoard.getGrade();
        double newGrade = tourBoardReview.get().getGrade();
        int currentCount = tourBoard.getReviewCount();

        if (currentCount > 1) {
            double avarageGrade = ((currentGrade * currentCount) - newGrade) / (currentCount - 1);
            tourBoard.setGrade(avarageGrade);
            tourBoard.setReviewCount(currentCount - 1);
        }
        else {
            tourBoardReviewRepository.deleteById(tbrno);
            tourBoard.setGrade(0);
            tourBoard.setReviewCount(0);
            tourBoardRepository.save(tourBoard);
            return;
        }

        tourBoardRepository.save(tourBoard);
        tourBoardReviewRepository.deleteById(tbrno);

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


