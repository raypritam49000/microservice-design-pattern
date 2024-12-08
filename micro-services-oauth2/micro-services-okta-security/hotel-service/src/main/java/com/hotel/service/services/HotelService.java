package com.hotel.service.services;


import com.hotel.service.entites.Hotel;

import java.util.List;

public interface HotelService {
    Hotel create(Hotel hotel);

    //get all
    List<Hotel> getAll();
    //get single
    Hotel get(String id);

}
