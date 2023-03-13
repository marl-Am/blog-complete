package com.marlon.blog.service;

import com.marlon.blog.entity.Post;
import com.marlon.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }
    public List<Post> getAll() {
        return postRepository.findAll();
    }
    public List<Post> findAllByOrderByUpdatedOnDesc() {
        return postRepository.findAllByOrderByUpdatedOnDesc();
    }

    public void save(Post post) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
        String stringDateTime = now.format(format);
        if (post.getId() == null) {
            post.setCreatedOn(stringDateTime);
        }
        post.setUpdatedOn(stringDateTime);
        postRepository.save(post);
    }
    public void delete(Post post) {
        postRepository.delete(post);
    }

}
