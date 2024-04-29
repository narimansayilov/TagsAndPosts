package org.example.tagsandposts.model.dto.response;

import lombok.Data;

@Data
//@FieldDefaults(makeFinal = true)
public class TagResponse {
    private Long id;
    private String name;
}
