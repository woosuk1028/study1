package com.example.study1.service;

import com.example.study1.dto.PostDto;
import com.example.study1.entity.Post;
import com.example.study1.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void savePost(PostDto dto, String writer) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setWriter(writer);
        post.setCreateDate(LocalDateTime.now());
        post.setUpdateDate(LocalDateTime.now());

        postRepository.save(post);
    }

    public List<Post> getPostList() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
    }

    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
    
    @Transactional
    public Post getPostWithLock(Long id) {
        return postRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }
}
