package com.blogapi.controller;

import com.blogapi.model.dto.CommentRequest;
import com.blogapi.model.dto.CommentResponse;
import com.blogapi.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Blog Comment Management APIs")
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "Get comments for post")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long postId) {
        log.info("REST request to get Comments by Post ID: {}", postId);
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "Add comment to post")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest) {
        log.info("REST request to add Comment to Post ID: {}, Comment: {}", postId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(postId, commentRequest));
    }

    @PutMapping("/comments/{id}")
    @Operation(summary = "Update comment")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest commentRequest) {
        log.info("REST request to update Comment ID: {}, Comment: {}", id, commentRequest);
        return ResponseEntity.ok(commentService.updateComment(id, commentRequest));
    }

    @DeleteMapping("/comments/{id}")
    @Operation(summary = "Delete comment")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.info("REST request to delete Comment ID: {}", id);
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
