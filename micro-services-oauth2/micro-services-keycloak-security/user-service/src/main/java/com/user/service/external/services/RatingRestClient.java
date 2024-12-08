package com.user.service.external.services;


import com.user.service.entities.Rating;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.List;

@HttpExchange(url = "http://RATING-SERVICE")
public interface RatingRestClient {

    @PostExchange("/ratings")
    ResponseEntity<Rating> createRating(@RequestBody Rating values);

    @PutExchange("/ratings/{ratingId}")
    ResponseEntity<Rating> updateRating(@PathVariable("ratingId") String ratingId, Rating rating);

    @DeleteExchange("/ratings/{ratingId}")
    void deleteRating(@PathVariable String ratingId);

    @GetExchange("/ratings/users/{userId}")
    List<Rating> getRatingsByUserId(@PathVariable("userId") String userId);

}
