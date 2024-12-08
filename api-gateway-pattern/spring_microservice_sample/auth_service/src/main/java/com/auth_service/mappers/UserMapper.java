package com.auth_service.mappers;

import com.auth_service.dto.UserDTO;
import com.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
    List<UserDTO> usersToUserDTOs(List<User> books);
    List<User> userDTOsToUsers(List<UserDTO> bookDTOs);
}