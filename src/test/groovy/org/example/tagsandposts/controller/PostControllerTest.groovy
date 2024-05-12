package org.example.tagsandposts.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.example.tagsandposts.model.dto.request.PostRequest
import org.example.tagsandposts.model.dto.response.PostResponse
import org.example.tagsandposts.model.exception.NotFoundException
import org.example.tagsandposts.service.PostService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

class PostControllerTest extends Specification {
    private def mapper
    private def random
    private def mockMvc
    private def postController
    private PostService postService

    void setup() {
        postService = Mock()
        postController = new PostController(postService)
        random = EnhancedRandomBuilder.aNewEnhancedRandom()
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
        mockMvc = MockMvcBuilders.standaloneSetup(postController).setControllerAdvice(new ErrorHandler()).build()
    }

    def "AddPost 200"() {
        given:
        def url = "/v1/posts"
        def postRequestJson = '''
            {
                "title": "NewPost",
                "content": "NewContent"
            }
        '''
        def postRequest = new ObjectMapper().readValue(postRequestJson, PostRequest)

        when:
        def response = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(postRequestJson)
        ).andReturn().response

        then:
        1 * postService.addPost(postRequest)

        and:
        response.status == HttpStatus.OK.value()
    }


    def "GetAll 200"() {
        given:
        def url = "/v1/posts"
        def postResponses = [
                random.nextObject(PostResponse),
                random.nextObject(PostResponse),
                random.nextObject(PostResponse)
        ]

        when:
        def response = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * postService.getAll() >> postResponses

        and:
        response.status == HttpStatus.OK.value()
    }

    def "GetById 200"() {
        given:
        def id = 1L
        def url = "/v1/posts/$id"

        def postResponse = PostResponse.builder()
                .id(1L)
                .title("This is Title")
                .content("This is content")
                .build()

        def postResponseJson = '''
             {
                  "id": 1,
                  "title": "This is Title",
                  "content": "This is content"
             }
        '''

        when:
        def response = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * postService.getById(id) >> postResponse

        and:
        response.status == 200
        JSONAssert.assertEquals(postResponseJson, response.contentAsString, false)
    }

    def "GetById 404"() {
        given:
        def id = 1L
        def url = "/v1/posts/$id"
        def response = '''
            {
                "message":"POST_NOT_FOUND",
                "code":"NOT_FOUND"
            }
        '''

        when:
        def result = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * postService.getById(id) >> {
            throw new NotFoundException("POST_NOT_FOUND")
        }

        and:
        result.status == NOT_FOUND.value()
        JSONAssert.assertEquals(result.contentAsString, response, false)
    }

    def "DeleteById 204"() {
        given:
        def id = 1L
        def url = "/v1/posts/$id"

        when:
        def response = mockMvc.perform(delete(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * postService.deleteById(id)

        and:
        response.status == HttpStatus.NO_CONTENT.value()
    }

    def "EditById 200"() {
        given:
        Long id = 1L
        String url = "/v1/posts/$id"
        def postRequestJson = '''
            {
                "title": "New Post",
                "content": "New Content"
            }
        '''
        def postRequest = new ObjectMapper().readValue(postRequestJson, PostRequest)

        when:
        def response = mockMvc.perform(put(url)
                .contentType(APPLICATION_JSON)
                .content(postRequestJson)
        ).andReturn().response

        then:
        1 * postService.editById(id, postRequest)

        and:
        response.status == HttpStatus.OK.value()
    }


    def "EditContent 200"() {
        given:
        Long id = 1L
        String url = "/v1/posts/$id"
        def postRequestJson = '''
            {
                "title": "New Post",
                "content": "New Content"
            }
        '''
        def postRequest = new ObjectMapper().readValue(postRequestJson, PostRequest)

        when:
        def response = mockMvc
                .perform(patch(url)
                        .contentType(APPLICATION_JSON)
                        .content(postRequestJson)
                ).andReturn().response

        then:
        1 * postService.editContent(id, postRequest)

        and:
        response.status == HttpStatus.OK.value()
    }
}
