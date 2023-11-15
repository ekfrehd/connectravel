1. 에러
   1. 가이드 게시판에 이미지가 담긴 글 입력 시 main.html의 페이지가 보이지 않는 타임리프 에러 발생
   원인은 메인 페이지에서 불러오는 매개변수가 잘못됨(TourBoardDTO.imgDTOList > TourBoardDTO.imgFiles, ImgDTO.imgfile > ImgDTO.imgFile)
   메인 페이지 당신에게 추천하는 가이드 - 카드 리스트 이미지 없는 것과 있는 것의 틀 맞춤
   2. 어드민 게시판에 이미지가 담긴 글 입력 후 게시글 조회 시 /adminboard/read?abno=의 페이지가 보이지 않는 타임리프 에러 발생
      원인은 read 페이지에서 불러오는 매개변수가 잘못됨(ImgDTO.imgfile > ImgDTO.imgFile)
   3. Qna 게시판 댓글 등록 문제
   댓글 문제는 content와 replyer 값이 없음
   content는 js코드 text로 보내고 있었음
   replyer는 컨트롤러에서 qnaReplyDTO 댓글 작성자 값에 memberDTO.getEmail() 삽입
   
   4.Qna 게시판 댓글 수정 문제
   작성자는 댓글 등록과 동일, content는 모달창 replyText에 입력되는 것이 아닌 댓글 등록창 replyText에 입력되는 것으로 보임
   모달창 입력 name="modal_replyText"로 바꾼 후 js에서도 modal_replyText에 값을 보낸다.

2. 개선, 추가할 점
   