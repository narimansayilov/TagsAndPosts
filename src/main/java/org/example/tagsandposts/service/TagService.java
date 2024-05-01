package org.example.tagsandposts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.tagsandposts.dao.entity.TagEntity;
import org.example.tagsandposts.dao.repository.TagRepository;
import org.example.tagsandposts.mapper.TagMapper;
import org.example.tagsandposts.model.dto.request.TagRequest;
import org.example.tagsandposts.model.dto.response.TagResponse;
import org.example.tagsandposts.model.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;


    public void addTag(TagRequest request){
        log.info("ActionLog.save.start for {}", request);
        tagRepository.save(TagMapper.INSTANCE.requestToEntity(request));
        log.info("ActionLog.save.end for {}", request);
    }

    public List<TagResponse> getAll(){
        log.info("ActionLog.getAll start");
        return TagMapper.INSTANCE.entitiesToResponses(tagRepository.findAll());
    }

    public TagResponse getById(Long id){
        log.info("ActionLog.getById start for {}", id);
        return TagMapper.INSTANCE.entityToResponse(tagRepository.findById(id).
                orElseThrow(() -> new NotFoundException("TAG_NOT_FOUND")));
    }

    public void deleteById(Long id){
        log.info("ActionLog.deleteById start for {}", id);
        tagRepository.deleteById(id);
        log.info("ActionLog.deleteById end for {}", id);
    }

    public TagResponse editById(Long id, TagRequest request){
        log.info("ActionLog.editById start for {}", id);
        TagEntity entity = tagRepository.findById(id).
                orElseThrow(() -> new NotFoundException("TAG_NOT_FOUND"));
        TagMapper.INSTANCE.mapRequestToEntity(entity, request);
        tagRepository.save(entity);
        return TagMapper.INSTANCE.entityToResponse(entity);
    }

    public TagResponse updateName(Long id, TagRequest request){
        log.info("ActionLog.updateName start for {}", id);
        TagEntity entity = tagRepository.findById(id).
                orElseThrow(() -> new NotFoundException("TAG_NOT_FOUND"));
        TagMapper.INSTANCE.mapRequestToEntity(entity, request);
        return TagMapper.INSTANCE.entityToResponse(tagRepository.save(entity));
    }
 }
