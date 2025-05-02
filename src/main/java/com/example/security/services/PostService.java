package com.example.security.services;

import com.example.security.dtos.PostDTO;

import java.util.List;

public interface PostService {
    List<PostDTO> getAllPosts();
    PostDTO createNewPost(PostDTO postDTO);

    PostDTO findById(Long id);


}
