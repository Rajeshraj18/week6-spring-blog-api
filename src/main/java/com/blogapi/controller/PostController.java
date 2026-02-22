package com.blogapi.controller;

import com.blogapi.model.dto.PostRequest;
import com.blogapi.model.dto.PostResponse;
import com.blogapi.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Blog Post Management APIs")
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "Get all posts", description = "Retrieve all blog posts with pagination and sorting")
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("REST request to get a page of Posts");
        Page<PostResponse> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Retrieve a specific blog post by its ID")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        log.info("REST request to get Post : {}", id);
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    @Operation(summary = "Create new post", description = "Create a new blog post")
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        log.info("REST request to save Post : {}", postRequest);
        PostResponse createdPost = postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Update an existing blog post")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequest postRequest) {
        log.info("REST request to update Post : {}, {}", id, postRequest);
        PostResponse updatedPost = postService.updatePost(id, postRequest);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Delete a blog post by ID")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        log.info("REST request to delete Post : {}", id);
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get posts by category", description = "Retrieve all posts in a specific category")
    public ResponseEntity<List<PostResponse>> getPostsByCategory(@PathVariable Long categoryId) {
        log.info("REST request to get Posts by Category : {}", categoryId);
        List<PostResponse> posts = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(posts);
    }
}
