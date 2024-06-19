package org.example.tagsandposts.service

import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.example.tagsandposts.client.PostCheckingClient
import org.example.tagsandposts.dao.entity.PostEntity
import org.example.tagsandposts.dao.entity.TagEntity
import org.example.tagsandposts.dao.repository.PostRepository
import org.example.tagsandposts.dao.repository.TagRepository
import org.example.tagsandposts.model.client.postchecking.PostCheckingResponse
import org.example.tagsandposts.model.dto.request.PostRequest
import org.example.tagsandposts.model.dto.response.PostResponse
import org.example.tagsandposts.model.exception.PostException
import spock.lang.Specification

class PostServiceTest extends Specification {
    private EnhancedRandom random
    private PostService postService
    private TagRepository tagRepository
    private PostRepository postRepository
    private PostCheckingClient checkingClient

    void setup() {
        postRepository = Mock()
        tagRepository = Mock()
        checkingClient = Mock()
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
        postService = new PostService(postRepository, tagRepository, checkingClient)
    }

    def "addPost Failed"() {
        given:
        def request = random.nextObject(PostRequest)
        def tagEntities = [
                random.nextObject(TagEntity),
                random.nextObject(TagEntity),
                random.nextObject(TagEntity)
        ]
        def postCheckingResponse = random.nextObject(PostCheckingResponse)
        postCheckingResponse.success = true

        when:
        postService.addPost(request)

        then:
        1 * tagRepository.findAllById(request.tagIds) >> tagEntities
        1 * checkingClient.check(request.title) >> postCheckingResponse
        0 * postRepository.save(_) >> {
            throw new PostException("POST_ALREADY_EXISTS")
        }

        def ex = thrown(PostException)
        ex.message == "POST_ALREADY_EXISTS"
    }

    def "addPost Successfully"() {
        given:
        def request = random.nextObject(PostRequest)
        def tagEntities = [
                random.nextObject(TagEntity),
                random.nextObject(TagEntity),
                random.nextObject(TagEntity)
        ]
        def postCheckingResponse = random.nextObject(PostCheckingResponse)
        postCheckingResponse.success = false

        when:
        postService.addPost(request)

        then:
        1 * tagRepository.findAllById(request.tagIds) >> tagEntities
        1 * checkingClient.check(request.title) >> postCheckingResponse
        1 * postRepository.save(_)
    }

    def "GetById"() {
        given:
        def id = 1L
        def postEntity = random.nextObject(PostEntity.class)

        when:
        def result = postService.getById(id)

        then:
        1 * postRepository.findById(id) >> Optional.ofNullable(postEntity)

        and:
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

        and:
        result.size() == postEntities.size()
        result.every { response ->
            postEntities.any { entity ->
                entity.id == response.id &&
                entity.title == response.title &&
                entity.content == response.content &&
                entity.createdAt == response.createdAt

            }

        }
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
        given:
        Long id = 1L;
        PostRequest request = PostRequest.builder()
                .title("This is Title")
                .content("This is content")
                .tagIds([1L, 2L, 3L])
                .build()
        PostEntity entity = PostEntity.builder()
                .id(id)
                .title("This is Title")
                .content("This is content")
                .tags([
                        random.nextObject(TagEntity),
                        random.nextObject(TagEntity),
                        random.nextObject(TagEntity)
                ])
                .build()
        PostResponse response = PostResponse.builder()
                .id(id)
                .title("This is Title")
                .content("This is content")
                .build()

        when:
        def result = postService.editById(id, request)

        then:
        1 * postRepository.findById(id) >> Optional.ofNullable(entity)
        1 * postRepository.save(entity) >> response

        and:
        result.title == request.title
        result.content == request.content
    }

    def "EditContent"() {
        given:
        Long id = 1L;
        PostRequest request = PostRequest.builder()
                .title("This is Title")
                .content("This is content")
                .tagIds([1L, 2L, 3L])
                .build()
        PostEntity entity = PostEntity.builder()
                .id(id)
                .title("This is Title")
                .content("This is content")
                .tags([
                        random.nextObject(TagEntity),
                        random.nextObject(TagEntity),
                        random.nextObject(TagEntity)
                ])
                .build()
        PostResponse response = PostResponse.builder()
                .id(id)
                .title("This is Title")
                .content("This is content")
                .build()

        when:
        def result = postService.editById(id, request)

        then:
        1 * postRepository.findById(id) >> Optional.ofNullable(entity)
        1 * postRepository.save(entity) >> response

        and:
        result.title == request.title
        result.content == request.content
    }
}
