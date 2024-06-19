package org.example.tagsandposts.model.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException{
    public PostException(String message) {
        super(message);
    }
}
