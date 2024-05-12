package org.example.tagsandposts.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//@FieldDefaults(makeFinal = true)
public class TagResponse {
    private Long id;
    private String name;
}
