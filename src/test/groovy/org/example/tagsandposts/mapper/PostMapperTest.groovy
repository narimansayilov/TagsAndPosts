package org.example.tagsandposts.mapper

import io.github.benas.randombeans.EnhancedRandomBuilder
import net.bytebuddy.build.CachedReturnPlugin.Enhance
import org.example.tagsandposts.dao.entity.PostEntity
import org.example.tagsandposts.dao.entity.TagEntity
import org.example.tagsandposts.model.dto.request.PostRequest
import spock.lang.Specification

class PostMapperTest extends Specification {
    def random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
    void setup() {

    }

    def "RequestToEntity"() {
        given:
        def postRequest = random.nextObject(PostRequest)
//        def tagEntities = (1..5).collect {
//            random.nextObject(TagEntity)
//        }
        def tagEntities = [
                random.nextObject(TagEntity),
                random.nextObject(TagEntity),
                random.nextObject(TagEntity)
        ]

        when:
        def postEntity = PostMapper.INSTANCE.requestToEntity(postRequest, tagEntities)

        then:
        postEntity.title == postRequest.title
        postEntity.content == postRequest.content
    }

    def "EntityToResponse"() {
        given:
        def postEntity = random.nextObject(PostEntity)

        when:
        def postResponse = PostMapper.INSTANCE.entityToResponse(postEntity)

        then:
        postResponse.id == postEntity.id
        postResponse.title == postEntity.title
        postResponse.content == postEntity.content
    }

    def entitiesToResponses() {
        given:
        def postEntities = [
                random.nextObject(PostEntity),
                random.nextObject(PostEntity),
                random.nextObject(PostEntity)
        ]

        when:
        def postResponses = PostMapper.INSTANCE.entitiesToResponses(postEntities)

        then:
        postResponses.size() == postEntities.size()
    }

    def "MapRequestToEntity"() {

    }
}
