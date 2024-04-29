package org.example.tagsandposts.mapper;

import org.example.tagsandposts.dao.entity.TagEntity;
import org.example.tagsandposts.model.dto.request.TagRequest;
import org.example.tagsandposts.model.dto.response.TagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);
    TagEntity requestToEntity(TagRequest request);

    TagResponse entityToResponse(TagEntity entity);

    List<TagResponse> entitiesToResponses(List<TagEntity> entities);

    void mapRequestToEntity(@MappingTarget TagEntity entity, TagRequest request);
}
