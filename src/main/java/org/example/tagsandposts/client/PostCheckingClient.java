package org.example.tagsandposts.client;

import org.example.tagsandposts.model.client.postchecking.PostCheckingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-post-checking", url = "http://localhost:8081/post-checking")
public interface PostCheckingClient {
    @GetMapping("/check")
    PostCheckingResponse check(@RequestParam String title);
}