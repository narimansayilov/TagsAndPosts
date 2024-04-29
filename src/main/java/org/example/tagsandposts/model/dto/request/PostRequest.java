package org.example.tagsandposts.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
//@FieldDefaults(makeFinal = true)
public class PostRequest {
    @NotBlank(message = "title is mandatory!")
    @Pattern(regexp = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm")
    @Size(min = 3, max = 1000)
    private String title;

    @NotBlank(message = "content is mandatory!")
    @Pattern(regexp = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm")
    private String content;

    private List<Long> tagIds;
}
