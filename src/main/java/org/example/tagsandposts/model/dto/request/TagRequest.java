package org.example.tagsandposts.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@FieldDefaults(makeFinal = true)
public class TagRequest {
    @NotBlank(message = "Name is mandatory!")
    @Size(min = 3, max = 127)
    @Pattern(regexp = "[a-zA-Z]+")
    private String name;
}
