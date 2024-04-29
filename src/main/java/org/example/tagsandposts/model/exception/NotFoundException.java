package org.example.tagsandposts.model.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private String code;

    public NotFoundException(String code){
        super(code);
    }
}
