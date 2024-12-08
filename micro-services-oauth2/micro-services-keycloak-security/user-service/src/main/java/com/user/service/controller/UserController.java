package com.user.service.controller;

import com.user.service.entities.User;
import com.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // Use RestTemplate
    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        logger.info("Entering getAllUser method");
        List<User> allUser = userService.getUsers();
        logger.info("Exiting getAllUser method");
        return ResponseEntity.ok(allUser);
    }

    // Use FeignClient
    @GetMapping("/getAllUserList")
    public ResponseEntity<List<User>> getAllUserList() {
        logger.info("Entering getAllUserList method");
        List<User> allUser = userService.getUserList();
        logger.info("Exiting getAllUserList method");
        return ResponseEntity.ok(allUser);
    }

    // Use WebClient
    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Entering getAllUsers method");
        List<User> allUser = userService.getAllUsers();
        logger.info("Exiting getAllUsers method");
        return ResponseEntity.ok(allUser);
    }

    // Use RestClient
    @GetMapping("/findAllUsers")
    public ResponseEntity<List<User>> findAllUsers() {
        logger.info("Entering findAllUsers method");
        List<User> allUser = userService.findAllUsers();
        logger.info("Exiting findAllUsers method");
        return ResponseEntity.ok(allUser);
    }

    // Use HttpInterface
    @GetMapping("/findUsers")
    public ResponseEntity<List<User>> findUsers() {
        logger.info("Entering findUsers method");
        List<User> allUser = userService.findUsers();
        logger.info("Exiting findUsers method");
        return ResponseEntity.ok(allUser);
    }

    @GetMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    @RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        logger.info("Entering getSingleUser method with userId: {}", userId);
        User user = userService.getUser(userId);
        logger.info("Exiting getSingleUser method with user: {}", user);
        return ResponseEntity.ok(user);
    }

    // Fallback method for circuit breaker
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
        logger.error("Fallback is executed because service is down for userId: {}. Exception: {}", userId, ex.getMessage());
        ex.printStackTrace();
        User user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("This user is created dummy because some service is down")
                .userId("141234")
                .build();
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/feign/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        logger.info("Entering getUser (Feign) method with userId: {}", userId);
        User user = userService.getUserById(userId);
        logger.info("Exiting getUser (Feign) method with user: {}", user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/webclient/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        logger.info("Entering getUserById (WebClient) method with userId: {}", userId);
        User user = userService.findUserById(userId);
        logger.info("Exiting getUserById (WebClient) method with user: {}", user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/rest-client/{userId}")
    public ResponseEntity<User> findSingleUser(@PathVariable String userId) {
        logger.info("Entering findSingleUser (Rest Client) method with userId: {}", userId);
        User user = userService.findSingleUser(userId);
        logger.info("Exiting findSingleUser (Rest Client) method with user: {}", user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/findUser/{userId}")
    public ResponseEntity<User> findUser(@PathVariable String userId) {
        logger.info("Entering findUser method with userId: {}", userId);
        User user = userService.findUser(userId);
        logger.info("Exiting findUser method with user: {}", user);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Entering createUser method with user: {}", user);
        User createdUser = userService.savedUser(user);
        logger.info("Exiting createUser method with created user: {}", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        logger.info("Entering deleteUser method with userId: {}", userId);
        userService.deleteUser(userId);
        logger.info("Exiting deleteUser method");
        return ResponseEntity.ok(Map.of("httpCode", 200, "httpStatus", "OK", "moreInformation", "SUCCESS", "message", "User deleted"));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        logger.info("Entering updateUser method with userId: {} and user: {}", userId, user);
        User updatedUser = userService.updateUser(userId, user);
        logger.info("Exiting updateUser method with updated user: {}", updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
}
