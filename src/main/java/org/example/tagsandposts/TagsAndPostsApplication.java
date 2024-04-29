package org.example.tagsandposts;

import org.example.tagsandposts.client.PostCheckingClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {PostCheckingClient.class})
public class TagsAndPostsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TagsAndPostsApplication.class, args);
    }
}
