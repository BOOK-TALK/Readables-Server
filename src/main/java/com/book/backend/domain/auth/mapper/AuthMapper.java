package com.book.backend.domain.auth.mapper;

import com.book.backend.domain.auth.dto.SignupDto;
import com.book.backend.domain.user.entity.Gender;
import com.book.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public User convertToUser(SignupDto signupDto) {
        User user = mapper.map(signupDto, User.class);
        user.setGender(convertStringToGender(signupDto.getGender()));
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        return user;
    }

    private Gender convertStringToGender(String gender) {
        return switch (gender) {
            case "NOT_SELECTED" -> Gender.G0;
            case "MAN" -> Gender.G1;
            case "WOMAN" -> Gender.G2;
        };
    }
}
