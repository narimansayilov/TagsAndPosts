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

class PostControllerTest extends Specification {
    MockMvc mockMvc
    PostService postService
    def mapper = new ObjectMapper().registerModule(new JavaTimeModule())
    def random = EnhancedRandomBuilder.aNewEnhancedRandom()

    void setup() {
        postService = Mock()
        def postController = new PostController(postService)
        mockMvc = MockMvcBuilders.standaloneSetup(postController).setControllerAdvice(new ErrorHandler()).build()
    }

    def "AddPost 200"() {

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

//    def "DeleteById"() {
//        given:
//        def id = 1L
//        def url = "http://example.com/v1/posts/$id"
//        def restTemplate = new RestTemplate()
//
//        when:
//        ResponseEntity<Void> response = restTemplate.exchange(
//                url,
//                HttpMethod.DELETE,
//                null,
//                Void.class
//        )
//
//        then:
//        response.statusCode == HttpStatus.NO_CONTENT
//    }

    def "EditById"() {

    }

    def "EditContent"() {

    }
}
