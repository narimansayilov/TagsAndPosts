package org.example.tagsandposts.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.example.tagsandposts.model.dto.request.TagRequest
import org.example.tagsandposts.model.dto.response.TagResponse
import org.example.tagsandposts.model.exception.NotFoundException
import org.example.tagsandposts.service.TagService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

class TagControllerTest extends Specification {
    private def random
    private def mapper
    private def mockMvc
    private def tagController
    private TagService tagService

    void setup() {
        tagService = Mock()
        tagController = new TagController(tagService)
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build()
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
        mockMvc = MockMvcBuilders.standaloneSetup(tagController).setControllerAdvice(new ErrorHandler()).build()
    }

    def "AddTag 200"() {
        given:
        def url = "/v1/tags"
        def tagRequestJson = '''
            {
                "name": "ThisIsName"
            }
        '''
        def tagRequest = new ObjectMapper().readValue(tagRequestJson, TagRequest)

        when:
        def response = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(tagRequestJson)
        ).andReturn().response

        then:
        1 * tagService.addTag(tagRequest)

        and:
        response.status == HttpStatus.OK.value()
    }

    def "GetAll 200"() {
        given:
        def url = "/v1/tags"
        def tagResponses = [
                random.nextObject(TagResponse),
                random.nextObject(TagResponse),
                random.nextObject(TagResponse)
        ]

        when:
        def response = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * tagService.getAll() >> tagResponses

        and:
        response.status == HttpStatus.OK.value()
    }

    def "GetById 200"() {
        given:
        def id = 1L
        def url = "/v1/tags/$id"
        def tagResponse = TagResponse
                .builder()
                .id(id)
                .name("ThisIsName")
                .build()

        when:
        def result = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * tagService.getById(id) >> tagResponse

        and:
        result.status == HttpStatus.OK.value()
    }

    def "GetById 404"() {
        given:
        def id = 1L
        def url = "/v1/tags/$id"
        def response = '''
            {
                "message": "TAG_NOT_FOUND",
                "code": "NOT_FOUND"
            }
        '''

        when:
        def result = mockMvc.perform(get(url)
            .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * tagService.getById(id) >> {
            throw new NotFoundException("TAG_NOT_FOUND")
        }

        and:
        result.status == HttpStatus.NOT_FOUND.value()
        JSONAssert.assertEquals(result.contentAsString, response, false)
    }

    def "DeleteById 204"() {
        given:
        Long id = 1L
        String url = "/v1/tags/$id"

        when:
        def result = mockMvc.perform(delete(url)
                .contentType(APPLICATION_JSON)
        ).andReturn().response

        then:
        1 * tagService.deleteById(id)

        and:
        result.status == HttpStatus.NO_CONTENT.value()
    }

    def "EditById 200"() {
        given:
        Long id = 1L
        String url = "/v1/tags/$id"
        def tagRequestJson = '''
            {
                "name": "This is Tag"
            }
        '''
        def tagRequest = new ObjectMapper().readValue(tagRequestJson, TagRequest)

        when:
        def result = mockMvc.perform(put(url)
                .contentType(APPLICATION_JSON)
                .content(tagRequestJson)
        ).andReturn().response

        then:
        1 * tagService.editById(id, tagRequest)

        and:
        result.status == HttpStatus.OK.value()
    }

    def "UpdateName 200"() {
        given:
        Long id = 1L
        String url = "/v1/tags/$id"
        def tagRequestJson = '''
            {
                "name": "This is Tag"
            }
        '''
        def tagRequest = new ObjectMapper().readValue(tagRequestJson, TagRequest)

        when:
        def result = mockMvc.perform(patch(url)
                .contentType(APPLICATION_JSON)
                .content(tagRequestJson)
        ).andReturn().response

        then:
        1 * tagService.updateName(id, tagRequest)

        and:
        result.status == HttpStatus.OK.value()
    }
}
