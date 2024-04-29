package org.example.tagsandposts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tagsandposts.model.dto.request.PostRequest;
import org.example.tagsandposts.model.dto.response.PostResponse;
import org.example.tagsandposts.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
@Validated
//@FieldDefaults(makeFinal = true)
public class PostController {
    private final PostService service;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addPost(@RequestBody PostRequest request){
        service.addPost(request);
    }

    @GetMapping
    public List<PostResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("/{id}")
    public PostResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public PostResponse editById(@PathVariable Long id,
                                 @RequestBody PostRequest request){
        return service.editById(id, request);
    }

    @PatchMapping("{id}")
    public PostResponse editContent(@PathVariable Long id,
                                    @RequestBody PostRequest request){
        return service.editContent(id, request);
    }
}
