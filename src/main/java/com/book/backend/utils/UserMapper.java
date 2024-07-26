package com.book.backend.utils;

import com.book.backend.domain.user.dto.UserDto;
import com.book.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = modelMapper.map(user, UserDto.class);

        // 생년월일은 수동 처리
        if (user.getBirthDate() != null) {
            LocalDate birthDate = user.getBirthDate();
            userDto.setBirthYear(birthDate.getYear());
            userDto.setBirthMonth(birthDate.getMonthValue());
            userDto.setBirthDay(birthDate.getDayOfMonth());
        }

        if (user.getSex() != null) {
            userDto.setSex(user.getSex().name());
        }

        return userDto;
    }
}
