package com.sparta.wildcard_newsfeed.domain.user.dto.junmo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wildcard_newsfeed.config.WebSecurityConfig;
import com.sparta.wildcard_newsfeed.domain.comment.controller.CommentController;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.service.CommentService;
import com.sparta.wildcard_newsfeed.domain.post.controller.PostController;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostPageRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.post.service.PostService;
import com.sparta.wildcard_newsfeed.domain.user.controller.UserController;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserRoleEnum;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* 테스트 코드 사용자 환경 변수
DB_URL=localhost:3306;
DB_USER=root;
MAIL_PASSWORD=nnhq wcsp olsz etli;MAIL_PORT=587;
SERVER_PORT=8080;

 */

@WebMvcTest(
        controllers = {UserController.class, PostController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class ControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    //UserController
    @MockBean
    UserService userService;

    //PostController, CommentController
    @MockBean
    PostService postService;
    @MockBean
    CommentService commentService;
    @MockBean
    PostRepository postRepository;


    String usercode = "testid1234";
    String password = "currentPWD999!";
    String email = "test@naver.com";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    @BeforeAll
    public static void init() {

    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = User.builder()
                            .usercode(usercode)
                            .password(password)
                            .userRoleEnum(role)
                            .build();
        AuthenticationUser testUserDetails = AuthenticationUser.of(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("로그인 Page 가져오는 테스트? 인가")
    void test1() throws Exception {
        // when - then
        mvc.perform(get("/api/user/login-page"))
                //.andExpect(status().isOk())
                //.andExpect(view().name("login"))
                .andDo(print());
    }
    @Test
    @DisplayName("회원 가입 요청 처리")
    void userTest2() throws Exception {
        // given
        UserSignupRequestDto requestDto = UserSignupRequestDto.builder()
                .usercode(usercode)
                .password(password)
                .email(email)
                .build();

        String postInfo = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/api/v1/user/signup")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                //.andExpect(view().name("redirect:/api/user/login-page"))
                .andDo(print());
    }

    @Test
    @DisplayName("post 생성 테스트")
    public void postTest1() throws Exception {
        //given
        //this.mockUserSetup();
        PostRequestDto requestDto = new PostRequestDto("게시글 제목", "게시글 내용",null);

        String postInfo = objectMapper.writeValueAsString(requestDto);
        System.err.println(postInfo);
        //json로 보내는 것과 form-data로 보내는 것의 차이 확실하게 잘 모르겠다 더 찾아봐야 할 듯

        MultiValueMap<String, String> requestTest = new LinkedMultiValueMap<>();
        requestTest.add("title", "제목");
        requestTest.add("content", "내용");

        //when - then
        mvc.perform(post("/api/v1/post")
                        .params(requestTest)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        //.principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("post 전체 조회 테스트")
    public void postTest2() throws Exception {
        //given

        //when - then
        mvc.perform(get("/api/v1/post"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("post 단일 조회 테스트")
    public void postTest3() throws Exception {
        //given

        //when - then
        mvc.perform(get("/api/v1/post/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("post 수정 테스트")
    public void postTest4() throws Exception {
        //given
        MultiValueMap<String, String> requestTest = new LinkedMultiValueMap<>();
        requestTest.add("title", "제목");
        requestTest.add("content", "내용");

        //when - then
        mvc.perform(put("/api/v1/post/1")
                        .params(requestTest)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("post 삭제 테스트")
    public void postTest5() throws Exception {
        //given

        //when - then
        mvc.perform(delete("/api/v1/post/1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("post page 테스트")
    public void postTest6() throws Exception {
        //given
        PostPageRequestDto requestDto = new PostPageRequestDto(
                1,
                10,
                "LIKED",
                "2024-05-01",
                "2024-07-15"
        );

        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when - then
        mvc.perform(post("/api/v1/post/page")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Comment 생성 테스트")
    public void commentTest1() throws Exception {
        //given
        //this.mockUserSetup();
        CommentRequestDto requestDto = new CommentRequestDto("댓글 내용");

        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when - then
        mvc.perform(post("/api/v1/post/1/comment") //  api/v1/post/{postId}/comment
                                .content(postInfo)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                        //.principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Comment 댓글 수정 테스트")
    public void commentTest2() throws Exception {
        //given
        //this.mockUserSetup();
        CommentRequestDto requestDto = new CommentRequestDto("댓글 수정 내용");

        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when - then
        mvc.perform(put("/api/v1/post/1/comment/1") //  api/v1/post/{postId}/comment/{commentId}
                                .content(postInfo)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                        //.principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Comment 댓글 삭제 테스트")
    public void commentTest3() throws Exception {
        //given
        this.mockUserSetup();
        //when - then
        mvc.perform(delete("/api/v1/post/1/comment/1") //  api/v1/post/{postId}/comment/{commentId}
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andDo(print());

        //이 위에서는 UserSetup이랑  principal 안 넣어주고도 에러 안 떳는데 왜 여기서만 에러가 뜰까????????????????????
    }

}
