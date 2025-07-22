package com.example.security.utils;

import com.example.security.dtos.PostDTO;
import com.example.security.entities.PostEntity;
import com.example.security.entities.User;
import com.example.security.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {
    private final PostService postService;

    public boolean isOwnerOfPost(Long postId){
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PostDTO post=postService.findById(postId);
        return post.getAuthor().getId().equals(user.getId());
    }
}
