package com.wo.springsecurity.controller;

import com.wo.springsecurity.controller.dto.CreateTweetDto;
import com.wo.springsecurity.entities.Tweet;
import com.wo.springsecurity.repository.TweetRepository;
import com.wo.springsecurity.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CreateTweetController {

    private final TweetRepository tweetRepository;

    private final UserRepository userRepository;

    public CreateTweetController(TweetRepository tweetRepository,
                                 UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto dto
            , JwtAuthenticationToken token) {

        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(dto.content());

        tweetRepository.save(tweet);
        return ResponseEntity.ok().build();
    }
}
