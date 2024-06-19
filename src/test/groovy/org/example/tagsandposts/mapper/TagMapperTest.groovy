package org.example.tagsandposts.mapper

import io.github.benas.randombeans.EnhancedRandomBuilder
import org.example.tagsandposts.dao.entity.TagEntity
import org.example.tagsandposts.model.dto.request.TagRequest
import spock.lang.Specification

class TagMapperTest extends Specification {
    private def random
    void setup() {
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
    }

    def "RequestToEntity"() {
        given:
        TagRequest request = random.nextObject(TagRequest)

        when:
        def entity = TagMapper.INSTANCE.requestToEntity(request)

        then:
        entity.name == request.name
    }

    def "EntityToResponse"() {
        given:
        TagEntity entity = random.nextObject(TagEntity)

        when:
        def response = TagMapper.INSTANCE.entityToResponse(entity)

        then:
        response.id == entity.id
        response.name == entity.name
    }

    def "entitiesToResponses"() {
        given:
        List<TagEntity> entities = [
                random.nextObject(TagEntity),
                random.nextObject(TagEntity),
                random.nextObject(TagEntity)
        ]

        when:
        def responses = TagMapper.INSTANCE.entitiesToResponses(entities)

        then:
        responses.size() == entities.size()
        responses.every { response ->
            entities.any { entity ->
                entity.id == response.id &&
                entity.name == response.name
            }
        }
     }

    def "MapRequestToEntity"() {
        given:
        TagRequest request = random.nextObject(TagRequest)
        TagEntity entity = new TagEntity()

        when:
        TagMapper.INSTANCE.mapRequestToEntity(entity, request)

        then:
        entity.name == request.name
    }
}
