package com.sparta.wildcard_newsfeed.domain.user.dto.junmo;

import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentResponseDto;
import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import com.sparta.wildcard_newsfeed.domain.comment.repository.CommentRepository;
import com.sparta.wildcard_newsfeed.domain.comment.service.CommentService;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.post.service.PostService;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 PORT 를 랜덤으로 설정합니다.
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;

    User user;
    AuthenticationUser authUser;
    Post post;
    Comment comment;

    String usercode = "testid12342222";
    String password = "currentPWD999!";
    String email = "test2222@naver.com";

    @Test
    @Order(1)
    @DisplayName("회원 가입 테스트")
    void userTest1() {
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

        authUser = new AuthenticationUser(responseDto.getUsercode(), password);
    }

    @Test
    @Order(2)
    @DisplayName("가입한 회원으로 게시글 작성")
    void userTest2() {
        // given
        PostRequestDto requestDto = new PostRequestDto("게시글 제목", "게시글 내용",null);
        post = new Post(requestDto,user);
        post.setId(1L); // DB에서 자동으로 주는 번호

        // when
        PostResponseDto responseDto = postService.addPost(requestDto,authUser);

        // then
        assertEquals(post.getTitle(), responseDto.getTitle());
        assertEquals(post.getContent(), responseDto.getContent());
        assertEquals(post.getLikeCount(),0);
    }

    @Test
    @Order(3)
    @DisplayName("게시글에 댓글 작성")
    void userTest3() {
        // given
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용입니다");
        comment = new Comment("댓글 내용입니다",user,post);

        // when
        CommentResponseDto responseDto = commentService.addComment(post.getId(),requestDto,authUser);

        // then
        assertEquals(comment.getContent(), responseDto.getContent());
        assertEquals(comment.getUser().getUsercode(), responseDto.getUsername());
        //댓글 내용과 댓글 작성자 확인
    }

}
