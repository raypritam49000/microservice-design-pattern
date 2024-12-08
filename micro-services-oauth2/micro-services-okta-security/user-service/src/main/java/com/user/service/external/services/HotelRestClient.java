package com.user.service.external.services;


import com.user.service.entities.Hotel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Service
@HttpExchange(url = "http://HOTEL-SERVICE")
public interface HotelRestClient {

    @GetExchange("/hotels/{hotelId}")
    Hotel getHotel(@PathVariable("hotelId") String hotelId);
}
