package com.connectravel.controller;

import com.connectravel.domain.dto.ReviewReplyDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.service.ReviewReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/review-reply")
@RequiredArgsConstructor
public class ReviewReplyController {

    private final ReviewReplyService reviewReplyService;

    @PostMapping("/create")
    public ResponseEntity<String> createReviewReply(@RequestBody ReviewReplyDTO reviewReplyDTO, @AuthenticationPrincipal Member member) {
        try {
            reviewReplyDTO.setReplyer(member.getEmail());
            Long rrno = reviewReplyService.createReviewReply(reviewReplyDTO);
            return ResponseEntity.ok("Review Reply created successfully with ID: " + rrno);
        } catch (Exception e) {

            System.out.println("에러");
            return ResponseEntity.badRequest().body("Error creating Review Reply: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateReviewReply(@RequestBody ReviewReplyDTO reviewReplyDTO) {
        try {
            reviewReplyService.updateReviewReply(reviewReplyDTO);
            return ResponseEntity.ok("Review Reply updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating Review Reply: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReviewReply(@RequestParam Long rrno) {
        try {
            reviewReplyService.deleteReviewReply(rrno);
            return ResponseEntity.ok("Review Reply deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting Review Reply: " + e.getMessage());
        }
    }
}
