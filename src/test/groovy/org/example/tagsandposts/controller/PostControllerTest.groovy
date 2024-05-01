package org.example.tagsandposts.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.example.tagsandposts.model.dto.response.PostResponse
import org.example.tagsandposts.model.exception.NotFoundException
import org.example.tagsandposts.service.PostService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class PostControllerTest extends Specification {
    private def mapper
    private def random
    private MockMvc mockMvc
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
        def url = "v1/posts"
        def postRequestJson = '''
             {
                 "title" = "This is test title",
                 "content" = "This is test content"
             } 
        '''

        when:
        def response = mockMvc
                .perform (post(url)
                        .contentType(APPLICATION_JSON))
                .andReturn().response

    }

    def "GetAll 200"() {
        given:
        def postResponses = [
                random.nextObject(PostResponse),
                random.nextObject(PostResponse),
                random.nextObject(PostResponse)
        ]

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
        def response = mockMvc
                .perform(get(url)
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

        when:
        def response = mockMvc
                .perform(get(url).contentType(APPLICATION_JSON))
                .andReturn().response

        then:
        1 * postService.getById(id) >> {
            throw new NotFoundException("POST_NOT_FOUND")
        }

        def ex = thrown(NotFoundException)
        ex.code == "POST_NOT_FOUND"
        response.status == NOT_FOUND.value()

    }

    def "DeleteById 204"() {
        given:
        def id = 1L
        def url = "/v1/posts/$id"

        when:
        def response = mockMvc
                .perform(delete(url).contentType(APPLICATION_JSON))
                .andReturn().response

        then:
        1 * postService.deleteById(id)

        and:
        response.status == HttpStatus.NO_CONTENT.value()
    }

    def "EditById"() {

    }

    def "EditContent"() {

    }
}
