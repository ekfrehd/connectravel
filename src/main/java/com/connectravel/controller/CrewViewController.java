package com.connectravel.controller;

import com.connectravel.constant.SportEnum;
import com.connectravel.domain.dto.Review.ReviewRequest;
import com.connectravel.domain.dto.Review.ReviewResponse;
import com.connectravel.domain.dto.crew.CrewDetailResponse;
import com.connectravel.domain.dto.crew.CrewRequest;
import com.connectravel.domain.dto.crew.CrewResponse;
import com.connectravel.domain.dto.crew.CrewSportRequest;
import com.connectravel.domain.entity.Crew;
import com.connectravel.domain.entity.Member;
import com.connectravel.repository.CrewRepository;
import com.connectravel.repository.MemberRepository;
import com.connectravel.service.CrewService;
import com.connectravel.service.ParticipationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/crews")
@Slf4j
@Transactional
@Api(tags = {"Crew Controller"})
public class CrewViewController {

    private final CrewService crewService;
//    private final LikeViewService likeViewService;
    private final CrewRepository crewRepository;
    private final MemberRepository userRepository;
//    private final CrewReviewService crewReviewService;

    private final ParticipationService participationService;
    /*@ModelAttribute("reviews")
    public Map<String, String> reviews() {
        Map<String, String> reviews = new LinkedHashMap<>();
        reviews.put("01", "시간을 잘 지켜요.");
        reviews.put("02", "다음 모임에서도 함께하고 싶어요.");
        return reviews;
    }*/


//
//    @Value("${aws.access.key}")
//    String AWS_ACCESS_KEY;
//
//    @Value("${aws.secret.access.key}")
//    String AWS_SECRET_ACCESS_KEY;
//
//    @Value("${aws.region}")
//    String AWS_REGION;
//
//    @Value("${aws.bucket.name}")
//    String AWS_BUCKET_NAME;
//
//    String AWS_BUCKET_DIRECTORY = "/crewimages";

    // 크루 게시물 상세 페이지
    @GetMapping("/{crewId}")
    public String detailCrew(@PathVariable Long crewId, Model model, @ModelAttribute("sportRequest") CrewSportRequest crewSportRequest,
                             @PageableDefault(page = 0, size = 1, sort = "modTime", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal Member user) {

//        model.addAttribute("AWS_ACCESS_KEY", AWS_ACCESS_KEY);
//        model.addAttribute("AWS_SECRET_ACCESS_KEY", AWS_SECRET_ACCESS_KEY);
//        model.addAttribute("AWS_REGION", AWS_REGION);
//        model.addAttribute("AWS_BUCKET_NAME", AWS_BUCKET_NAME);
//        model.addAttribute("AWS_BUCKET_DIRECTORY", AWS_BUCKET_DIRECTORY);

        // 크루 게시물 검색 필터(전체조회, 지역조회, 운동종목 조회)
        Page<CrewDetailResponse> list = crewService.findAllCrewsByStrictAndSportEnum2(crewSportRequest,  pageable);
//        if(crewId == null)
//            crewId = list.getContent().get(0).getId();
        log.info("!!!!!!!!!!!!!! crewId : {}", crewId);

        // 참여자 인원 정보
        List<ReviewResponse> members = participationService.findAllPartMember(crewId);
        model.addAttribute("members", members);
//        ReviewRequest crewReviewRequest = new ReviewRequest();
//        model.addAttribute("reviewRequest", crewReviewRequest);

        try {
            //알림 체크
//            if(authentication != null) crewService.readAlarms(crewId, authentication.getName());

            // 좋아요
//            int count = likeViewService.getLikeCrew(crewId);
//            model.addAttribute("likeCnt", count);

            // 상세게시글 정보
            CrewDetailResponse details = list.getContent().get(0);
            model.addAttribute("details", details);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            // 후기 작성여부 파악
            if (user != null) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                Member nowUser = crewService.findByUserName(user.getUsername());
//                boolean userReviewed = crewReviewService.findReviewedUser(crewId, nowUser);
//                model.addAttribute("userReviewed", userReviewed);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                // 참여자 확인
                boolean isPartUser = participationService.isPartUser(crewId, nowUser);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                model.addAttribute("isPartUser", isPartUser);

            }

        } catch (Exception e) {
            return "redirect:/index";
        }

        // 페이징 처리 변수
        int nowPage = list.getPageable().getPageNumber();
        int lastPage = list.getTotalPages() - 1;
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // 페이징 처리 모델
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("lastPage", lastPage);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return "crew/read-crew";
    }


    @GetMapping("/detail/{crewId}")
    public String detailCrew2(@PathVariable Long crewId, Model model, @AuthenticationPrincipal Member user) {

//        model.addAttribute("AWS_ACCESS_KEY", AWS_ACCESS_KEY);
//        model.addAttribute("AWS_SECRET_ACCESS_KEY", AWS_SECRET_ACCESS_KEY);
//        model.addAttribute("AWS_REGION", AWS_REGION);
//        model.addAttribute("AWS_BUCKET_NAME", AWS_BUCKET_NAME);
//        model.addAttribute("AWS_BUCKET_DIRECTORY", AWS_BUCKET_DIRECTORY);

        // 크루 게시물 조회
        Crew crew = crewService.findByCrewId(crewId);

        // 참여자 인원 정보
        List<ReviewResponse> members = participationService.findAllPartMember(crewId);
        model.addAttribute("members", members);
        ReviewRequest crewReviewRequest = new ReviewRequest();
        model.addAttribute("reviewRequest", crewReviewRequest);

        try {
            //알림 체크
//            if(authentication != null) crewService.readAlarms(crewId, authentication.getName());

            // 좋아요
//            int count = likeViewService.getLikeCrew(crewId);
//            model.addAttribute("likeCnt", count);

            // 상세게시글 정보
            CrewDetailResponse details = CrewDetailResponse.of(crew);
            model.addAttribute("details", details);


            // 후기 작성여부 파악
            Member nowUser = crewService.findByUserName(user.getUsername());
//            boolean userReviewed = crewReviewService.findReviewedUser(crewId, nowUser);
//            model.addAttribute("userReviewed", userReviewed);

            // 참여자 확인
            boolean isPartUser = participationService.isPartUser(crewId, nowUser);
            model.addAttribute("isPartUser", isPartUser);


        } catch (Exception e) {
            return "redirect:/index";
        }


        return "crew/read-crew2";
    }

    // 크루 게시글 수정
    @PutMapping("/{crewId}")
    public String modifyCrew(@PathVariable Long crewId, @ModelAttribute CrewRequest crewRequest, @AuthenticationPrincipal Member user) {
        if(user == null){
            log.error("null error");
            return "redirect:/";
        }
        crewService.modifyCrew(crewId, crewRequest, user.getUsername());
        return "redirect:/view/v1/crews/{crewId}";
    }

    // 크루 게시글 수정화면
    @GetMapping("/update/{crewId}")
    public String updateCrew(@PathVariable Long crewId, Model model, @AuthenticationPrincipal Member user) {
        Crew crew = crewRepository.findById(crewId).orElse(null);
        if (crew == null || !crew.getUser().getUsername().equals(user.getUsername())) {
            return "error/404";
        }
        CrewRequest crewRequest = new CrewRequest();
        crewRequest.setTitle(crew.getTitle());
        crewRequest.setContent(crew.getContent());

        model.addAttribute(crewRequest);
        model.addAttribute("crewId", crew.getId());

        return "crew/update-crew";
    }

    // 크루 게시글 삭제
    @DeleteMapping("/{crewId}")
    public String deleteCrew(@PathVariable Long crewId, Model model, @AuthenticationPrincipal Member user) {
        Crew crew = crewRepository.findById(crewId).orElse(null);
        Member cuser = userRepository.findById(crew.getUser().getId()).orElse(null);
        log.info("삭제 조회 중");

        if (crew == null || cuser == null) {
            return "error/404";
        }
        CrewResponse crewResponse = crewService.deleteCrew(crewId, user.getUsername());
        model.addAttribute("response", crewResponse);
        return "redirect:/";
    }

    // 크루 좋아요 누르기
//    @PostMapping("/{crewId}/like")
//    public ResponseEntity likeCrew(@PathVariable Long crewId, @AuthenticationPrincipal Member user) {
//        LikeViewResponse likeViewResponse = likeViewService.pressLike(crewId, authentication.getName());
//        return new ResponseEntity<>(likeViewResponse, HttpStatus.OK);
//    }

    // 크루 게시물 전체 조회, 검색 조회, 운동 종목 조회
    @GetMapping()
    @ApiOperation(value = "크루 게시글 전체조회", notes = "")
    public String findAllCrew(Model model, @AuthenticationPrincipal Member user,
                              @ModelAttribute("sportRequest") CrewSportRequest crewSportRequest,
                              @PageableDefault(page = 0, size = 9, sort = "modTime", direction = Sort.Direction.DESC) Pageable pageable) {

//        model.addAttribute("AWS_ACCESS_KEY", AWS_ACCESS_KEY);
//        model.addAttribute("AWS_SECRET_ACCESS_KEY", AWS_SECRET_ACCESS_KEY);
//        model.addAttribute("AWS_REGION", AWS_REGION);
//        model.addAttribute("AWS_BUCKET_NAME", AWS_BUCKET_NAME);
//        model.addAttribute("AWS_BUCKET_DIRECTORY", AWS_BUCKET_DIRECTORY);


        // 유저 로그인 확인 후 운동 종목 데이터 확인
//        List<String> userSportsList = crewService.getUserSports(authentication, CollectionUtils.isEmpty(crewSportRequest.getSportsList()));

        // 처음 로그인 시, My운동종목 클릭 시 유저 종목 Model에 추가
//        if (!CollectionUtils.isEmpty(userSportsList) && !crewSportRequest.isLoginStatus()) {
//            crewSportRequest.setSportsList(userSportsList);
//            crewSportRequest.setLoginStatus(true);
//        }

        // 유저 등록된 지역 확인
//        if(crewSportRequest.getStrict() == null){
//            crewSportRequest.setStrict(crewService.getUserStrict(authentication));
//            log.info("!!!!!!!!!!!strict : {}", crewSportRequest.getStrict());
//        }
//        else{
//            log.info("!!!!!!!!!!!strict : not empty");
//        }

        // 크루 게시물 검색 필터(전체조회, 지역조회, 운동종목 조회)
        Page<CrewDetailResponse> list = crewService.findAllCrewsByStrictAndSportEnum2(crewSportRequest, pageable);

        // 페이징 처리 변수
        int nowPage = list.getPageable().getPageNumber() + 1;
        int startNumPage = Math.max(nowPage - 4, 1);
        int endNumPage = Math.min(nowPage + 5, list.getTotalPages());
        int lastPage = list.getTotalPages();

        // 게시글 리스트
        model.addAttribute("crewList", list);

        // 페이징 처리 모델
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startNumPage", startNumPage);
        model.addAttribute("endNumPage", endNumPage);
        model.addAttribute("lastPage", lastPage);

        // 운동 종목 Enum리스트
        List<SportEnum> sportEnums = List.of(SportEnum.values());
        model.addAttribute("sportEnums", sportEnums);

        model.addAttribute("isLoginStatus", crewSportRequest.isLoginStatus());

        return "main/main";
    }

    // 크루 리뷰 작성
//    @GetMapping("/review/{crewId}")
//    public String reviewCrew(@PathVariable Long crewId, @AuthenticationPrincipal Member user, Model model) {
//
//        if(authentication == null){
//            log.error("null pointer Error");
//            return "redirect:/";
//        }
//
//        //현재 유저 정보
//        User nowUser = crewService.findByUserName(authentication.getName());
//        model.addAttribute("nowUser", nowUser.getId());
//
//        // 크루 게시글 정보
//        Crew crew = crewService.findByCrewId(crewId);
//        model.addAttribute("crew", CrewDetailResponse.of(crew));
//
//        // 참여자 인원 정보
//        List<ReviewResponse> members = participationService.findAllPartMember(crewId);
//        model.addAttribute("members", members);
//
//        if(crewReviewService.isContainReview(crew,nowUser)){
//            return "redirect:/";
//        }
//
//
//        ReviewRequest crewReviewRequest = new ReviewRequest();
//        model.addAttribute("reviewRequest", crewReviewRequest);
//
//        return "crew/review-crew";
//    }
//
//    // 크루 리뷰 저장
//    @PostMapping("/review")
//    public String reviewCrew(Model model, @ModelAttribute("reviewRequest") ReviewRequest crewReviewRequest) {
//        crewReviewService.addReview(crewReviewRequest);
//        return "redirect:/";
//    }
//
//    // 리뷰 리스트
//    @GetMapping("/{userName}/reviewList")
//    public String findReviewList(@PathVariable String userName, Model model, @PageableDefault(page = 0, size = 5) @SortDefault.SortDefaults({
//            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)}) Pageable pageable) {
//
//        model.addAttribute("AWS_ACCESS_KEY", AWS_ACCESS_KEY);
//        model.addAttribute("AWS_SECRET_ACCESS_KEY", AWS_SECRET_ACCESS_KEY);
//        model.addAttribute("AWS_REGION", AWS_REGION);
//        model.addAttribute("AWS_BUCKET_NAME", AWS_BUCKET_NAME);
//        model.addAttribute("AWS_BUCKET_DIRECTORY", "/profileimages");
//
//
//        Page<CrewReviewResponse> reviewList = crewReviewService.findAllReviewList(userName, pageable);
//        model.addAttribute("reviewList", reviewList);
//
//        // paging
//        int startPage = Math.max(1,reviewList.getPageable().getPageNumber() - 4);
//        int endPage = Math.min(reviewList.getTotalPages(),reviewList.getPageable().getPageNumber() + 4);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//
//        long reviewAllCount = crewReviewService.getReviewAllCount(userName);
//        model.addAttribute("reviewAllCount", reviewAllCount);
//
//
//        return "review/review-list";
//    }
//
//    // 리뷰 detail
//    @GetMapping("/{userName}/reviewList/{reviewId}")
//    public String findReview(@PathVariable String userName, @PathVariable Long reviewId, Model model, @AuthenticationPrincipal Member user) {
//
//        if(authentication == null){
//            log.error("null pointer Error");
//            return "redirect:/";
//        }
//
//        //알림 체크
//        if(authentication != null) crewService.readAlarmsReview(reviewId, authentication.getName());
//
//        CrewReviewDetailResponse review = crewReviewService.findReviewById(reviewId);
//        model.addAttribute("review", review);
//        model.addAttribute("userName",userName);
//        return "review/review-content";
//    }

    @ModelAttribute("sportEnums")
    private List<SportEnum> sportEnums() {
        List<SportEnum> sportEnums = List.of(SportEnum.values());
        return sportEnums;
    }


    // 내가 참여중인 모임 리스트
    @GetMapping("/{userName}/active")
    public String getActiveCrewList(@PathVariable String userName, Model model, @PageableDefault(page = 0, size = 5) @SortDefault.SortDefaults({
                                            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)}) Pageable pageable) {

//        model.addAttribute("AWS_ACCESS_KEY", AWS_ACCESS_KEY);
//        model.addAttribute("AWS_SECRET_ACCESS_KEY", AWS_SECRET_ACCESS_KEY);
//        model.addAttribute("AWS_REGION", AWS_REGION);
//        model.addAttribute("AWS_BUCKET_NAME", AWS_BUCKET_NAME);
//        model.addAttribute("AWS_BUCKET_DIRECTORY", AWS_BUCKET_DIRECTORY);



        // list
        Page<CrewDetailResponse> crewList = crewService.findAllCrew(2,userName, pageable); // 2: 참여 완료
        model.addAttribute("crewList",crewList);
        // paging
        int startPage = Math.max(1,crewList.getPageable().getPageNumber() - 4);
        int endPage = Math.min(crewList.getTotalPages(),crewList.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        // count
        putCategorizeCrewCount(userName,model);
        return "part/get-current-crew";
    }

    // 종료된 모임 리스트
    @GetMapping("/{userName}/end")
    public String getEndCrewList(@PathVariable String userName, Model model, @PageableDefault(page = 0, size = 5) @SortDefault.SortDefaults({
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)}) Pageable pageable) {

//        model.addAttribute("AWS_ACCESS_KEY", AWS_ACCESS_KEY);
//        model.addAttribute("AWS_SECRET_ACCESS_KEY", AWS_SECRET_ACCESS_KEY);
//        model.addAttribute("AWS_REGION", AWS_REGION);
//        model.addAttribute("AWS_BUCKET_NAME", AWS_BUCKET_NAME);
//        model.addAttribute("AWS_BUCKET_DIRECTORY", AWS_BUCKET_DIRECTORY);


        // list
        Page<CrewDetailResponse> crewList = crewService.findAllCrew(3, userName, pageable); // 3: 모집 종료
        model.addAttribute("crewList",crewList);
        // paging
        int startPage = Math.max(1,crewList.getPageable().getPageNumber() - 4);
        int endPage = Math.min(crewList.getTotalPages(),crewList.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        // count
        putCategorizeCrewCount(userName,model);
        model.addAttribute("userName",userName);

        return "part/get-end-crew";
    }

    // 특정 crew를 count하는 메소드
    private void putCategorizeCrewCount(String userName, Model model) {
        long activeCrewCount = crewService.getCrewByUserAndStatus(2,userName);
        long endCrewCount = crewService.getCrewByUserAndStatus(3,userName);
        model.addAttribute("activeCrewCount", activeCrewCount);
        model.addAttribute("endCrewCount", endCrewCount);

    }


}