package com.sparta.wildcard_newsfeed.domain.user.dto.junmo;

import com.sparta.wildcard_newsfeed.domain.file.service.FileService;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.AuthCodeRepository;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.domain.user.service.AuthCodeService;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 PORT 를 랜덤으로 설정합니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceIntegrationTest {
    /*
    시크릿 키 환경 변수 넣어줘야 작동
    DB_PASSWORD=
    DB_URL=
    DB_USER=
    JWT-SECRET-KEY=
    MAIL_PASSWORD=
    MAIL_PORT=
    MAIL_USERNAME=
    SERVER_PORT=8080;
    S3_ACCESS_KEY=
    S3_SECRET_KEY=
     */
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    User user;
    UserSignupResponseDto userSignupResponseDto = null;

    String usercode = "testid12342222";
    String password = "currentPWD999!";
    String email = "test2222@naver.com";

    @Test
    @Order(1)
    @DisplayName("회원 가입 테스트")
    void userTest1() throws Exception {
        // given
        user = User.builder()
                .usercode(usercode)
                .password(password)
                .email(email)
                .build();

        UserSignupRequestDto requestDto = new UserSignupRequestDto(usercode, password, email);

        // when
        UserSignupResponseDto responseDto = userService.signup(requestDto);

        // then
        assertEquals(usercode, responseDto.getUsercode());
        assertEquals(email, responseDto.getEmail());
    }

}
