package com.blogapi.controller;

import com.blogapi.model.dto.CommentRequest;
import com.blogapi.model.dto.CommentResponse;
import com.blogapi.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@SuppressWarnings("null")
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentResponse commentResponse;
    private CommentRequest commentRequest;

    @BeforeEach
    void setUp() {
        commentResponse = CommentResponse.builder()
                .id(1L)
                .content("Test Comment")
                .author("Test Author")
                .build();

        commentRequest = new CommentRequest();
        commentRequest.setContent("Test Comment");
        commentRequest.setAuthor("Test Author");
    }

    @Test
    void getCommentsByPostId_ShouldReturn200() throws Exception {
        List<CommentResponse> comments = Arrays.asList(commentResponse);
        when(commentService.getCommentsByPostId(1L)).thenReturn(comments);

        mockMvc.perform(get("/api/posts/{postId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test Comment"));
    }

    @Test
    void addComment_ShouldReturn201() throws Exception {
        when(commentService.addComment(eq(1L), any(CommentRequest.class))).thenReturn(commentResponse);

        mockMvc.perform(post("/api/posts/{postId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Test Comment"));
    }

    @Test
    void updateComment_ShouldReturn200() throws Exception {
        when(commentService.updateComment(eq(1L), any(CommentRequest.class))).thenReturn(commentResponse);

        mockMvc.perform(put("/api/comments/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test Comment"));
    }

    @Test
    void deleteComment_ShouldReturn204() throws Exception {
        doNothing().when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/api/comments/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
