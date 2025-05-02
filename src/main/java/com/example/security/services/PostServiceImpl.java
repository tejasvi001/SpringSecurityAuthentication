package com.example.security.services;

import com.example.security.dtos.PostDTO;
import com.example.security.entities.PostEntity;
import com.example.security.entities.User;
import com.example.security.exceptions.ResourceNotFoundException;
import com.example.security.repositories.PostRepository;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@ToString
@Service
public class PostServiceImpl implements PostService{
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<PostEntity> posts=postRepository.findAll();
        return posts.
                stream().
                map(
                        postEntity -> modelMapper.map(postEntity,PostDTO.class)
                ).collect(Collectors.toList());
    }

    @Override
    public PostDTO createNewPost(PostDTO postDTO) {
        PostEntity post=modelMapper.map(postDTO,PostEntity.class);
        return modelMapper.map(postRepository.save(post),PostDTO.class);
    }

    @Override
    public PostDTO findById(Long id)  {
        User user =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("User",user);
        PostEntity post= null;
        try {
            post = postRepository.findById(id).orElseThrow(
                    ()->new ResourceNotFoundException("No post with tht id "+id)
            );
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return modelMapper.map(post,PostDTO.class);
    }

}
