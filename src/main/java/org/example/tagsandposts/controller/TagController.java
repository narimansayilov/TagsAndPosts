package org.example.tagsandposts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tagsandposts.model.dto.request.TagRequest;
import org.example.tagsandposts.model.dto.response.TagResponse;
import org.example.tagsandposts.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
//@FieldDefaults(makeFinal = true)
public class TagController {
    private final TagService service;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addTag(@Valid @RequestBody TagRequest request){
        service.addTag(request);
    }

    @GetMapping
    public List<TagResponse> getAll(){
        return service.getAll();
    }

    @GetMapping("{id}")
    public TagResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    @PutMapping("{id}")
    public TagResponse editById(@PathVariable Long id,
                                @RequestBody TagRequest request){
        return service.editById(id, request);
    }

    @PatchMapping("{id}")
    public TagResponse updateName(@PathVariable Long id,
                                  @RequestBody TagRequest request){
        return service.updateName(id, request);
    }
}
