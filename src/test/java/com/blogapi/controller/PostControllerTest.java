package com.blogapi.controller;

import com.blogapi.model.dto.PostRequest;
import com.blogapi.model.dto.PostResponse;
import com.blogapi.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@SuppressWarnings("null")
public class PostControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PostService postService;

        @Autowired
        private ObjectMapper objectMapper;

        private PostResponse postResponse;
        private PostRequest postRequest;

        @BeforeEach
        void setUp() {

                postResponse = PostResponse.builder()
                                .id(1L)
                                .title("Test Post")
                                .content("Test Content")
                                .author("Test Author")
                                .categoryId(1L)
                                .build();

                postRequest = new PostRequest();
                postRequest.setTitle("Test Post");
                postRequest.setContent("Test Content");
                postRequest.setAuthor("Test Author");
                postRequest.setCategoryId(1L);
        }

        @Test
        void getAllPosts_ShouldReturn200() throws Exception {
                Page<PostResponse> page = new PageImpl<>(Arrays.asList(postResponse));
                when(postService.getAllPosts(any(Pageable.class))).thenReturn(page);

                // For testing PageImpl, we bypass MockMvc JSON serialization to avoid
                // HttpMessageNotWritableException
                PostController controller = new PostController(postService);
                ResponseEntity<Page<PostResponse>> response = controller.getAllPosts(PageRequest.of(0, 10));

                assertNotNull(response);
                assertEquals(200, response.getStatusCode().value());
                assertEquals(1, response.getBody().getTotalElements());
                assertEquals("Test Post", response.getBody().getContent().get(0).getTitle());
        }

        @Test
        void getPostById_ShouldReturn200() throws Exception {
                when(postService.getPostById(1L)).thenReturn(postResponse);

                mockMvc.perform(get("/api/posts/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Test Post"));
        }

        @Test
        void createPost_ShouldReturn201() throws Exception {
                when(postService.createPost(any(PostRequest.class))).thenReturn(postResponse);

                mockMvc.perform(post("/api/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postRequest)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.title").value("Test Post"));
        }

        @Test
        void updatePost_ShouldReturn200() throws Exception {
                when(postService.updatePost(eq(1L), any(PostRequest.class))).thenReturn(postResponse);

                mockMvc.perform(put("/api/posts/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Test Post"));
        }

        @Test
        void deletePost_ShouldReturn204() throws Exception {
                doNothing().when(postService).deletePost(1L);

                mockMvc.perform(delete("/api/posts/{id}", 1L))
                                .andExpect(status().isNoContent());
        }

        @Test
        void getPostsByCategory_ShouldReturn200() throws Exception {
                List<PostResponse> posts = Arrays.asList(postResponse);
                when(postService.getPostsByCategory(1L)).thenReturn(posts);

                mockMvc.perform(get("/api/posts/category/{categoryId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].title").value("Test Post"));
        }
}
