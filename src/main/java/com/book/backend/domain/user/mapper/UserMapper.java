package com.book.backend.domain.user.mapper;

import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.entity.User;
import com.book.backend.exception.CustomException;
import com.book.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public UserDto convertToUserDto(User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        UserDto userDto = mapper.map(user, UserDto.class);
        userDto.setGender(user.getGender().name());

        return userDto;
    }
}
