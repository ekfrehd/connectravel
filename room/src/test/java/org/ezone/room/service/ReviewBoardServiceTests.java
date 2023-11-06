package org.ezone.room.service;

import javassist.NotFoundException;
import org.ezone.room.dto.PageRequestDTO;
import org.ezone.room.dto.ReviewBoardDTO;
import org.ezone.room.repository.ReviewBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class ReviewBoardServiceTests {

    @Autowired
    private ReviewBoardService reviewBoardService;

    @Test
    public void testRegister() throws NotFoundException {

        ReviewBoardDTO dto = ReviewBoardDTO.builder()

                .grade(5d)
                .content("Test...")
                .rno(1L)
                .ano(1L)
                .rvno(1L)
                .writerEmail("asd@asd")  //현재 데이터베이스에 존재하는 회원 이메일
                .build();

        Long bno = reviewBoardService.register(dto);

    }

//    @Test
//    public void testList() {
//
//        //1페이지 10개
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//
//        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
//
//        for (BoardDTO boardDTO : result.getDtoList()) {
//            System.out.println(boardDTO);
//        }
//
//    }
//
//    @Test
//    public void testGet() {
//
//        Long bno = 101L;
//
//        ReviewBoardDTO reviewBoardDTO = ReviewBoardService.
//
//        System.out.println(boardDTO);
//    }
//
//    @Test
//    public void testRemove() {
//
//        Long bno = 1L;
//
//        boardService.remove(bno);
//
//    }
//
//    @Test
//    public void testModify() {
//
//        BoardDTO boardDTO = BoardDTO.builder()
//                .bno(2L)
//                .title("제목 변경합니다.2")
//                .content("내용 변경합니다.2")
//                .build();
//
//        boardService.modify(boardDTO);
//
//    }
//
//
//    @Test
//    public void testSearch(){
//
//        PageRequestDTO pageRequestDTO = new PageRequestDTO();
//        pageRequestDTO.setPage(1);
//        pageRequestDTO.setSize(10);
//        pageRequestDTO.setType("t");
//        pageRequestDTO.setKeyword("11");
//
//        Pageable pageable = pageRequestDTO.getPageable(Sort.by("bno").descending());
//
//        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
//
//        for (BoardDTO boardDTO : result.getDtoList()) {
//            System.out.println(boardDTO);
//        }
//    }


}
