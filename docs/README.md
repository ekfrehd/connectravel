## ✅ What to do?!

***

### 1. 기능 요구 사항

+ [x] AdminBoard 병합
+ + [x] 병합 후 필드명 등 오류 안나게 하기 
+ + [x] AdminBoard -> bno, rno -> abno, arno 로 변경
+ + [x] 병합 후 테스트 진행
+ 
+ [x] QnaBoard 병합
+ + [x] 병합 후 필드명 등 오류 안나게 하기
+ + [x] QnaBoard/Reply -> bno, rno -> qbno, qrno 로 변경
+ + [x] 병합 후 테스트 진행
+ 
+ [] TourBoard 병합
+ + [] 병합 후 필드명 등 오류 안나게 하기
+ + [] 병합 후 테스트 진행

    
### 2. 테스트 코드

+ [x] AdminBoard Test
+ + [x] AdminBoardRepository 페이징 검색
+ + [x] AdminBoardService CRUD, 페이징 getList

+ [x] QnaBoard Test
+ + [x] QnaBoardService CRUD, 페이징, 리뷰 게시판 삭제 시 댓글도 같이 삭제 (cascade)
+ + [x] QnaBoardReplyService CRUD