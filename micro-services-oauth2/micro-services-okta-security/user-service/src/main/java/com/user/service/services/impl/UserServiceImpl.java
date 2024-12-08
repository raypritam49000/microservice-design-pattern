package com.user.service.services.impl;

import com.user.service.entities.Hotel;
import com.user.service.entities.Rating;
import com.user.service.entities.User;
import com.user.service.exceptions.ResourceNotFoundException;
import com.user.service.external.services.HotelRestClient;
import com.user.service.external.services.HotelService;
import com.user.service.external.services.RatingRestClient;
import com.user.service.external.services.RatingService;
import com.user.service.repositories.UserRepository;
import com.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private WebClient webClient;

    @Autowired
    private RestClient restClient;

    @Autowired
    private HotelRestClient hotelRestClient;

    @Autowired
    private RatingRestClient ratingRestClient;

    @Override
    public User savedUser(User user) {
        return userRepository.save(user);
    }

    // Use RestTemplate
    @Override
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
            logger.info("{} ", ratingsOfUser);
            List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
            List<Rating> ratingList = ratings.stream().map(rating -> {
                ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
                logger.info("response status code: {} ", forEntity.getStatusCode());
                rating.setHotel(forEntity.getBody());
                return rating;
            }).collect(Collectors.toList());
            user.setRatings(ratingList);
        }
        return users;
    }

    // Use Feign Client
    @Override
    public List<User> getUserList() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Rating> ratings = ratingService.getRatingsByUserId(user.getUserId());
            List<Rating> ratingList = ratings.stream().map(rating -> {
                Hotel hotel = hotelService.getHotel(rating.getHotelId());
                rating.setHotel(hotel);
                return rating;
            }).collect(Collectors.toList());
            user.setRatings(ratingList);
        }
        return users;
    }

    // Use WebClient
    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Rating> ratings = webClient.get().uri("http://RATING-SERVICE/ratings/users/" + user.getUserId()).retrieve().bodyToFlux(Rating.class).collectList().block();
            List<Rating> ratingList = ratings.stream().map(rating -> {
                Hotel hotel = webClient.get().uri("http://HOTEL-SERVICE/hotels/" + rating.getHotelId()).retrieve().bodyToMono(Hotel.class).block();
                rating.setHotel(hotel);
                return rating;
            }).collect(Collectors.toList());
            user.setRatings(ratingList);
        }
        return users;
    }

    // Use RestClient
    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Rating> ratings = restClient.get().uri("http://RATING-SERVICE/ratings/users/" + user.getUserId()).retrieve().body(new ParameterizedTypeReference<List<Rating>>() {
            });
            List<Rating> ratingList = ratings.stream().map(rating -> {
                Hotel hotel = restClient.get().uri("http://HOTEL-SERVICE/hotels/" + rating.getHotelId()).retrieve().body(Hotel.class);
                rating.setHotel(hotel);
                return rating;
            }).collect(Collectors.toList());
            user.setRatings(ratingList);
        }
        return users;
    }

    // Use HTTP Interface
    @Override
    public List<User> findUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Rating> ratings = ratingRestClient.getRatingsByUserId(user.getUserId());
            List<Rating> ratingList = ratings.stream().map(rating -> {
                Hotel hotel = hotelRestClient.getHotel(rating.getHotelId());
                rating.setHotel(hotel);
                return rating;
            }).collect(Collectors.toList());
            user.setRatings(ratingList);
        }
        return users;
    }


    // Use RestTemplate Client
    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not found with given id : " + userId));
        Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);
        logger.info("{} ", ratingsOfUser);
        List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();
        List<Rating> ratingList = ratings.stream().map(rating -> {
            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            logger.info("response status code: {} ", forEntity.getStatusCode());
            rating.setHotel(forEntity.getBody());
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingList);
        return user;
    }

    // Use Feign Client
    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not found with given id : " + userId));
        List<Rating> ratings = ratingService.getRatingsByUserId(user.getUserId());
        List<Rating> ratingList = ratings.stream().map(rating -> {
            Hotel hotel = hotelService.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingList);
        return user;
    }

    // Use WebClient
    @Override
    public User findUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not found with given id : " + userId));
        List<Rating> ratings = webClient.get().uri("http://RATING-SERVICE/ratings/users/" + user.getUserId()).retrieve().bodyToFlux(Rating.class).collectList().block();
        List<Rating> ratingList = ratings.stream().map(rating -> {
            Hotel hotel = webClient.get().uri("http://HOTEL-SERVICE/hotels/" + rating.getHotelId()).retrieve().bodyToMono(Hotel.class).block();
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingList);
        return user;
    }

    // Use RestClient
    @Override
    public User findSingleUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not found with given id : " + userId));
        List<Rating> ratings = restClient.get().uri("http://RATING-SERVICE/ratings/users/" + user.getUserId()).retrieve().body(new ParameterizedTypeReference<List<Rating>>() {
        });
        List<Rating> ratingList = ratings.stream().map(rating -> {
            Hotel hotel = restClient.get().uri("http://HOTEL-SERVICE/hotels/" + rating.getHotelId()).retrieve().body(Hotel.class);
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingList);
        return user;
    }

    // Use HTTP Interface
    @Override
    public User findUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User does not found with given id : " + userId));
        List<Rating> ratings = ratingRestClient.getRatingsByUserId(user.getUserId());
        List<Rating> ratingList = ratings.stream().map(rating -> {
            Hotel hotel = hotelRestClient.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());
        user.setRatings(ratingList);
        return user;
    }

    @Override
    public void deleteUser(String userId) {
        User user = getUser(userId);
        userRepository.delete(user);
    }

    @Override
    public User updateUser(String userId, User updateData) {
        User user = getUser(userId);
        user.setAbout(updateData.getAbout());
        user.setName(updateData.getName());
        user.setEmail(updateData.getEmail());
        return userRepository.save(user);
    }
}
