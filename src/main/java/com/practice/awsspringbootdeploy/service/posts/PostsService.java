package com.practice.awsspringbootdeploy.service.posts;

import com.practice.awsspringbootdeploy.domain.posts.Posts;
import com.practice.awsspringbootdeploy.domain.posts.PostsRepository;
import com.practice.awsspringbootdeploy.web.dto.PostsListResponseDto;
import com.practice.awsspringbootdeploy.web.dto.PostsResponseDto;
import com.practice.awsspringbootdeploy.web.dto.PostsSaveRequestDto;
import com.practice.awsspringbootdeploy.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAll(Sort.by("id").descending()).stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+id));
        postsRepository.delete(posts);
    }
}
