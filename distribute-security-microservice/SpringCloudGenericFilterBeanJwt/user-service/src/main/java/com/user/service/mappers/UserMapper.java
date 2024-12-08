package com.user.service.mappers;

import com.user.service.dto.UserDTO;
import com.user.service.model.User;
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