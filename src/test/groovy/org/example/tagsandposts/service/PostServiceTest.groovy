package org.example.tagsandposts.service

import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.example.tagsandposts.dao.entity.PostEntity
import org.example.tagsandposts.dao.entity.TagEntity
import org.example.tagsandposts.dao.repository.PostRepository
import org.example.tagsandposts.dao.repository.TagRepository
import org.example.tagsandposts.mapper.PostMapper
import org.example.tagsandposts.model.dto.request.PostRequest
import spock.lang.Specification

class PostServiceTest extends Specification {
    private PostRepository postRepository
    private TagRepository tagRepository
    private EnhancedRandom random
    private PostService postService

    void setup() {
        postRepository = Mock()
        tagRepository = Mock()
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()

        postService = new PostService(postRepository, tagRepository)
    }

    def addPost() {
        given:
        def request = random.nextObject(PostRequest)
        def tagEntities = [random.nextObject(TagEntity)]

        when:
        postService.addPost(request)

        then:
        1 * tagRepository.findAllById(request.tagIds) >> tagEntities
        1 * postRepository.save(PostMapper.INSTANCE.requestToEntity(request, tagEntities))
    }

    def "GetById"() {
        given:
        def id = 1L
        def postEntity = random.nextObject(PostEntity.class)

        when:
        def result = postService.getById(id)

        then:
        1 * postRepository.findById(id) >> Optional.ofNullable(postEntity)

        result.id == postEntity.id
        result.title == postEntity.title
        result.content == postEntity.content
    }

    def "GetAll"() {
        given:
        def postEntities = (1..5).collect {
            random.nextObject(PostEntity.class)
        }

        when:
        def result = postService.getAll()

        then:
        1 * postRepository.findAll() >> postEntities

    }

    def "DeleteById"() {
        given:
        def id = 1L

        when:
        def result = postService.deleteById(id)

        then:
        1 * postRepository.deleteById(id)
    }

    def "EditById"() {
    }

    def "EditContent"() {
    }
}
