package org.example.tagsandposts.service

import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.example.tagsandposts.dao.entity.TagEntity
import org.example.tagsandposts.dao.repository.TagRepository
import org.example.tagsandposts.model.dto.request.TagRequest
import org.example.tagsandposts.model.dto.response.TagResponse
import spock.lang.Specification

class TagServiceTest extends Specification {
    private TagService tagService
    private EnhancedRandom random
    private TagRepository tagRepository

    void setup() {
        tagRepository = Mock()
        tagService = new TagService(tagRepository)
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
    }

    def "addTag"() {
        given:
        TagRequest tagRequest = random.nextObject(TagRequest)

        when:
        tagService.addTag(tagRequest)

        then:
        1 * tagRepository.save(_)
    }

    def "GetById"() {
        given:
        Long id = 1L
        TagEntity tagEntity = random.nextObject(TagEntity.class)

        when:
        def result = tagService.getById(id)

        then:
        1 * tagRepository.findById(id) >> Optional.of(tagEntity)

        and:
        result.id == tagEntity.id
        result.name == tagEntity.name
    }

    def "GetAll"() {
        given:
        List<TagEntity> tagEntities = [
                random.nextObject(TagEntity),
                random.nextObject(TagEntity),
                random.nextObject(TagEntity)
        ]

        when:
        def result = tagService.getAll()

        then:
        1 * tagRepository.findAll() >> tagEntities

        and:
        result.size() == tagEntities.size()
        result.every { response ->
            tagEntities.any { entity ->
                entity.id == response.id &&
                entity.name == response.name
            }
        }
    }

    def "DeleteById"() {
        given:
        Long id = 1L

        when:
        tagService.deleteById(id)

        then:
        1 * tagRepository.deleteById(id)
    }

    def "EditById"() {
        given:
        Long id = 1L
        TagRequest tagRequest = TagRequest.builder()
                .name("This is name")
                .build()
        TagEntity tagEntity = TagEntity.builder()
                .id(1L)
                .name("This is name")
                .build()
        TagResponse tagResponse = TagResponse.builder()
                .id(id)
                .name("This is name")
                .build()

        when:
        def result = tagService.editById(id, tagRequest)

        then:
        1 * tagRepository.findById(id) >> Optional.of(tagEntity)
        1 * tagRepository.save(tagEntity) >> tagResponse

        and:
        result.name == tagRequest.name
    }

    def "UpdateName"() {
        given:
        Long id = 1L
        TagRequest tagRequest = TagRequest.builder()
                .name("This is name")
                .build()
        TagEntity tagEntity = TagEntity.builder()
                .id(1L)
                .name("This is name")
                .build()
        TagResponse tagResponse = TagResponse.builder()
                .id(id)
                .name("This is name")
                .build()

        when:
        def result = tagService.editById(id, tagRequest)

        then:
        1 * tagRepository.findById(id) >> Optional.of(tagEntity)
        1 * tagRepository.save(tagEntity) >> tagResponse

        and:
        result.name == tagRequest.name
    }
}
