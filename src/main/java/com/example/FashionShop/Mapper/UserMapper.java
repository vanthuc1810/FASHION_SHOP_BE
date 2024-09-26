package com.example.FashionShop.Mapper;

import com.example.FashionShop.Dto.request.UpdateUserRequest;
import com.example.FashionShop.Dto.request.UserCreationRequest;
import com.example.FashionShop.Dto.response.UserResponse;
import com.example.FashionShop.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User toUser(UserCreationRequest request);

    @Mapping(target = "name", expression = "java(updateFieldIfNotNull(request.getName(), user.getName()))")
    @Mapping(target = "email", expression = "java(updateFieldIfNotNull(request.getEmail(), user.getEmail()))")
    @Mapping(target = "phone", expression = "java(updateFieldIfNotNull(request.getPhone(), user.getPhone()))")
    @Mapping(target = "address", expression = "java(updateFieldIfNotNull(request.getAddress(), user.getAddress()))")
    public User updateUser(@MappingTarget User user, UpdateUserRequest request);
    default <T> T updateFieldIfNotNull(T newValue, T oldValue) {
        return (newValue == "" || newValue == null)  ? oldValue : newValue;
    }

    public UserResponse toUserResponse(User user);
}
