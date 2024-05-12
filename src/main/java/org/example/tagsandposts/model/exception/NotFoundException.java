package org.example.tagsandposts.model.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
