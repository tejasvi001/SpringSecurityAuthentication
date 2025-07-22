package com.example.security.controllers;

import com.example.security.dtos.PostDTO;
import com.example.security.services.PostService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @Secured("ROLE_USER")
    public List<PostDTO> getAllposts(){
        return postService.getAllPosts();
    }
    @PostMapping
    public PostDTO createPost(@RequestBody PostDTO post) {
        return postService.createNewPost(post);
    }
    @GetMapping(path="/{id}")
    @PreAuthorize("@postSecurity.isOwnerOfPost(#id)")
    public PostDTO getPostById(@PathVariable Long id){
        return postService.findById(id);
    }

}
