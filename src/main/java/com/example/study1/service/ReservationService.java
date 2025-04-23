package com.example.study1.service;

import com.example.study1.entity.Post;
import com.example.study1.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final PostRepository postRepository;

    @Transactional
    public boolean tryReserve(Long postId) {
        Post post = postRepository.findByIdForUpdate(postId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if(post.isReserved()) {
            return false;
        }

        post.setReserved(true);

        return true;
    }
}
