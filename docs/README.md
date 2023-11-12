## ✅ What to do?!

***

### 1. 기능 요구 사항

+ [x] AdminBoard 병합
+ + [x] 병합 후 필드명 등 오류 안나게 하기 
+ + [x] AdminBoard -> bno, rno -> abno, arno 로 변경
+ + [x] 병합 후 테스트 진행

+ [x] QnaBoard 병합
+ + [x] 병합 후 필드명 등 오류 안나게 하기
+ + [x] QnaBoard/Reply -> bno, rno -> qbno, qrno 로 변경
+ + [x] 병합 후 테스트 진행

+ [x] TourBoard 병합 (TourBoard, TourBoardReview, TourBoardReviewReply)
+ + [x] 병합 후 필드명 등 오류 안나게 하기
+ + [x] 병합 후 테스트 진행 
+ + [x] TourBoard(+Img) - TourBoardReview(+Img) - TourBoardReviewReply 연관 관계 수정함

+ [] 숙박 업소 필터링 기능 구현
+ + [] repository 지역/옵션/기타 메서드 별로 구현
+ + [] service repository 활용해서 동적으로 필터링 기능 구현
    
### 2. 테스트 코드

+ [x] AdminBoard Test
+ + [x] AdminBoardRepository 페이징 검색
+ + [x] AdminBoardService CRUD, 페이징 getList

+ [x] QnaBoard Test
+ + [x] QnaBoardService CRUD, 페이징, 리뷰 게시판 삭제 시 댓글도 같이 삭제 (cascade)
+ + [x] QnaBoardReplyService CRUD
+ 
+ [x] TourBoard Test
+ + [x] TourBoardService CRUD
+ + [x] TourBoardReviewService CRUD
+ + [x] TourBoardReviewReplyService CRUD


### 3. 개선할 점 (미리 체크, 나중에 개선하기)
+ [] Room의 add, removeReservation이 사용이 안되고 있음. 방 예약할 때 이 메서드를 쓰기
+ [] TourBoard에서 member가 없고 프론트 단에서 admin 권한일 경우에 버튼 노출해서 들어가면 admin으로 간주하고
     글을 작성할 수 있게 되어 있음. 이 부분은 나중에 member를 추가해서 admin일 경우에만 작성이 가능하게 변경이 필요함.
     (URL에 검색해서 들어가면 바로 뚫리기 때문)
+ [] TourBoardReview의 recommend는 추천 수인데 나중에 추가하는 로직 구현하기
+ [] TourBoardReview에서 Review(후기글) 삭제 시에 평점 변경하는 로직 구현하기