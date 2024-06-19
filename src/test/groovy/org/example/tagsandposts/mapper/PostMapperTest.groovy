package org.example.tagsandposts.mapper

import io.github.benas.randombeans.EnhancedRandomBuilder
import org.example.tagsandposts.dao.entity.PostEntity
import org.example.tagsandposts.dao.entity.TagEntity
import org.example.tagsandposts.model.dto.request.PostRequest
import spock.lang.Specification

class PostMapperTest extends Specification {
    def random

    void setup() {
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
    }

    def "RequestToEntity"() {
        given:
        PostRequest postRequest = random.nextObject(PostRequest)
        List<TagEntity> tagEntities = [
                random.nextObject(TagEntity),
                random.nextObject(TagEntity),
                random.nextObject(TagEntity)
        ]

//        def tagEntities = (1..3).collect {
//            random.nextObject(TagEntity)
//        }


        when:
        def postEntity = PostMapper.INSTANCE.requestToEntity(postRequest, tagEntities)

        then:
        postEntity.title == postRequest.title
        postEntity.content == postRequest.content
    }

    def "EntityToResponse"() {
        given:
        PostEntity postEntity = random.nextObject(PostEntity)

        when:
        def postResponse = PostMapper.INSTANCE.entityToResponse(postEntity)

        then:
        postResponse.id == postEntity.id
        postResponse.title == postEntity.title
        postResponse.content == postEntity.content
    }

    def entitiesToResponses() {
        given:
        List<PostEntity> postEntities = [
                random.nextObject(PostEntity),
                random.nextObject(PostEntity),
                random.nextObject(PostEntity)
        ]

        when:
        def postResponses = PostMapper.INSTANCE.entitiesToResponses(postEntities)

        then:
        postResponses.size() == postEntities.size()
        postResponses.every { response ->
            postEntities.any { entity ->
                entity.id == response.id &&
                        entity.title == response.title &&
                        entity.content == response.content &&
                        entity.createdAt == response.createdAt
            }
        }
    }

    def "MapRequestToEntity"() {
        given:
        PostRequest request = random.nextObject(PostRequest)
        PostEntity entity = new PostEntity()

        when:
        PostMapper.INSTANCE.mapRequestToEntity(entity, request)

        then:
        entity.title == request.title
        entity.content == request.content
        entity.tags.every { entityTag ->
            request.tagIds.any { requestTag ->
                requestTag.id == entityTag.id
            }
        }
    }
}
