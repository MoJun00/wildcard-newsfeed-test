package com.sparta.wildcard_newsfeed.domain.user.dto.junmo;

import com.sparta.wildcard_newsfeed.domain.file.service.FileService;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.AuthCodeRepository;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.domain.user.service.AuthCodeService;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    AuthCodeService authCodeService;
    @Mock
    AuthCodeRepository authCodeRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    ApplicationEventPublisher eventPublisher;
    @Mock
    FileService fileService;

    String usercode = "testid12342222";
    String password = "currentPWD999!";
    String email = "test2222@naver.com";

    @Test
    @DisplayName("회원 가입 실패 테스트")
    void userTest1() throws Exception {
        // given
        User user = User.builder()
                .usercode(usercode)
                .password(password)
                .email(email)
                .build();

        UserSignupRequestDto requestDto = new UserSignupRequestDto(usercode, password, email);

        UserService userService = new UserService(userRepository, authCodeService, authCodeRepository,passwordEncoder,eventPublisher,fileService);
        given(userRepository.findByUsercodeOrEmail(usercode,email)).willReturn(Optional.of(user));
        //이미 있는지 확인하는 곳에 값을 넣어줘서 이미 있다고 에러 뜸

        // when
        UserSignupResponseDto result = userService.signup(requestDto);

        // then
        assertEquals(usercode, result.getUsercode());
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void userTest2() throws Exception {
        // given
        User user = User.builder()
                .usercode(usercode)
                .password(password)
                .email(email)
                .build();

        UserSignupRequestDto requestDto = new UserSignupRequestDto(usercode, password, email);

        UserService userService = new UserService(userRepository, authCodeService, authCodeRepository,passwordEncoder,eventPublisher,fileService);
        //given(userRepository.findByUsercodeOrEmail(usercode,email)).willReturn(Optional.of(user));

        // when
        UserSignupResponseDto result = userService.signup(requestDto);

        // then
        assertEquals(usercode, result.getUsercode());
    }
}
