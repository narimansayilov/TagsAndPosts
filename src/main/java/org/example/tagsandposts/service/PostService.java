package org.example.tagsandposts.service;

import lombok.extern.slf4j.Slf4j;
import org.example.tagsandposts.client.PostCheckingClient;
import org.example.tagsandposts.dao.entity.PostEntity;
import org.example.tagsandposts.dao.entity.TagEntity;
import org.example.tagsandposts.dao.repository.PostRepository;
import org.example.tagsandposts.dao.repository.TagRepository;
import org.example.tagsandposts.mapper.PostMapper;
import org.example.tagsandposts.model.client.postchecking.PostCheckingResponse;
import org.example.tagsandposts.model.dto.request.PostRequest;
import org.example.tagsandposts.model.dto.response.PostResponse;
import org.example.tagsandposts.model.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostCheckingClient postCheckingClient;

    public PostService(PostRepository postRepository, TagRepository tagRepository, PostCheckingClient postCheckingClient) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postCheckingClient = postCheckingClient;
    }

    public void addPost(PostRequest request) {
        log.info("ActionLog.save.start for {}", request.getTitle());
        List<TagEntity> tagEntities = tagRepository.findAllById(request.getTagIds());
        PostCheckingResponse postCheckingResponse = postCheckingClient.check(request.getTitle());
        if(!postCheckingResponse.getSuccess()){
            PostEntity postEntity = PostMapper.INSTANCE.requestToEntity(request, tagEntities);
            postRepository.save(postEntity);
            log.info("ActionLog.save.end for {}", request.getTitle());
        }
    }

    public List<PostResponse> getAll() {
        log.info("ActionLog.getAll.start");
        return PostMapper.INSTANCE.entitiesToResponses(postRepository.findAll());
    }

    public PostResponse getById(Long id) {
        log.info("ActionLog.getById.start for {}", id);
        return PostMapper.INSTANCE.entityToResponse(postRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.info("ActionLog.NotFoundException for {}", id);
                    return new NotFoundException("POST_NOT_FOUND");
                }));
    }

    public void deleteById(Long id) {
        log.info("ActionLog.deleteById.start for {}", id);
                postRepository.deleteById(id);
    }

    public PostResponse editById(Long id, PostRequest request) {
        log.info("ActionLog.editById.start for {}", id);
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("ActionLog.NotFoundException for {}", id);
                    return new NotFoundException("POST_NOT_FOUND");
                });
        PostMapper.INSTANCE.mapRequestToEntity(entity, request);
        postRepository.save(entity);
        return PostMapper.INSTANCE.entityToResponse(entity);
    }

    public PostResponse editContent(Long id, PostRequest request) {
        log.info("ActionLog.editContent.start for {}", id);
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("POST_NOT_FOUND"));
        PostMapper.INSTANCE.mapRequestToEntity(entity, request);
        postRepository.save(entity);
        return PostMapper.INSTANCE.entityToResponse(entity);
    }
}
