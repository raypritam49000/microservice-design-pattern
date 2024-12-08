package com.user.service.services;

import com.user.service.entities.User;

import java.util.List;

public interface UserService {
    User savedUser(User user);

    List<User> getUsers();

    List<User> getUserList();

    List<User> getAllUsers();

    List<User> findAllUsers();

    List<User> findUsers();

    User getUser(String userId);

    User getUserById(String userId);

    User findUserById(String userId);

    User findSingleUser(String userId);

    User findUser(String userId);

    void deleteUser(String userId);

    User updateUser(String userId, User updateData);
}
