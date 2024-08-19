package com.book.backend.domain.user.mapper;

import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.dto.UserInfoDto;
import com.book.backend.domain.user.entity.Gender;
import com.book.backend.domain.user.entity.User;
import com.book.backend.domain.user.repository.UserRepository;
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
        if (userDto.getGender() == null) {
            userDto.setGender(user.getGender().name());
        }

        return userDto;
    }

    public UserInfoDto convertToUserInfoDto(User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        UserInfoDto userInfoDto = mapper.map(user, UserInfoDto.class);
        userInfoDto.setGender(user.getGender().name());
        if (userInfoDto.getGender() == null) {
            userInfoDto.setGender(user.getGender().name());
        }

        return userInfoDto;
    }

    public Gender convertStringToGender(String gender) {
        return switch (gender) {
            case "MAN" -> Gender.G1;
            case "WOMAN" -> Gender.G2;
            default -> Gender.G0;
        };
    }
}
