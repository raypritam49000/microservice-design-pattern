package com.rating.service.services;

import com.rating.service.entities.Rating;

import java.util.List;


public interface RatingService {
    Rating create(Rating rating);
    List<Rating> getRatings();
    List<Rating> getRatingByUserId(String userId);
    List<Rating> getRatingByHotelId(String hotelId);

}
