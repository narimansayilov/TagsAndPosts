package org.example.tagsandposts.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@FieldDefaults(makeFinal = true)
public class PostRequest {
    @NotBlank(message = "title is mandatory!")
    @Pattern(regexp = "[a-zA-Z]+")
    @Size(min = 3, max = 1000)
    private String title;

    @NotBlank(message = "content is mandatory!")
    @Pattern(regexp = "[a-zA-Z]+")
    private String content;

    private List<Long> tagIds;
}
