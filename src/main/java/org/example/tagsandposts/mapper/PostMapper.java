package org.example.tagsandposts.mapper;

import org.example.tagsandposts.dao.entity.PostEntity;
import org.example.tagsandposts.dao.entity.TagEntity;
import org.example.tagsandposts.model.dto.request.PostRequest;
import org.example.tagsandposts.model.dto.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostEntity requestToEntity(PostRequest request, List<TagEntity> tags);

    PostResponse entityToResponse(PostEntity entity);

    List<PostResponse> entitiesToResponses(List<PostEntity> entityList);

    void mapRequestToEntity(@MappingTarget PostEntity entity, PostRequest request);
}
