package com.sparta.wildcard_newsfeed.domain.user.dto.junmo;

import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserRoleEnum;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserStatusEnum;
import com.sparta.wildcard_newsfeed.exception.validation.ValidationGroups;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtdEntityTest {

    String usercode = "JunMo00";
    String password = "0123456789aA!";
    String email = "ppkkgoaa@naver.com";

    @BeforeAll
    public static void init() {

    }


    @Test
    @DisplayName("User Entity Test1")
    public void userTest1(){
        //given
        User user = User.builder()
                .usercode(usercode)
                .password(password)
                .email(email)
                .build();
        //when

        //then
        assertEquals(user.getUsercode(), "JunMo00");
        assertEquals(user.getPassword(), "0123456789aA!");
        assertEquals(user.getEmail(), "ppkkgoaa@naver.com");
    }

    @Test
    @DisplayName("UserSignupRequestDto  Test")
    public void userTest2(){
        //given
        UserSignupRequestDto requestDto;
        requestDto = new UserSignupRequestDto(null, null, email);

        //when
        //Set<ConstraintViolation<UserSignupRequestDto>> violations = validator.validate(requestDto, ValidationGroups.NotBlankGroup.class, ValidationGroups.PatternGroup.class);

        //then
        Set<ConstraintViolation<UserSignupRequestDto>> violations = new ValidationTest<UserSignupRequestDto>().valid(requestDto);
        for (ConstraintViolation<UserSignupRequestDto> violation : violations) {
            System.err.println(violation.getMessage());
        }
        //System.err.println(violations.size());

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("User Entity 회원 가입 Test3")
    public void userTest3(){
        //given
        UserSignupRequestDto requestDto = new UserSignupRequestDto("aA00000001", "0123456789!aA", email);

        //when
        Set<ConstraintViolation<UserSignupRequestDto>> violations = new ValidationTest<UserSignupRequestDto>().valid(requestDto);
        for (ConstraintViolation<UserSignupRequestDto> violation : violations) {
                System.err.println(violation.getMessage());
        }
        // Dto Validation 검사

        User user = new User(requestDto.getUsercode(), requestDto.getPassword(), requestDto.getEmail());

        //then
        assertEquals(user.getUsercode(), "aA00000001");
        assertEquals(user.getPassword(), "0123456789!aA");
        assertEquals(user.getEmail(), "ppkkgoaa@naver.com");
        assertEquals(user.getUserStatus(), UserStatusEnum.UNAUTHORIZED);
        assertEquals(user.getUserRoleEnum(), UserRoleEnum.USER);

    }

    @Test
    @DisplayName("UserResponseDto  Test")
    public void userTest4(){
        //given
        User user = User.builder()
                .usercode(usercode)
                .name("강준모")
                .introduce("한 줄 소개")
                .email(email)
                .profileImageUrl("프로필 주소")
                .build();


        //when
        UserResponseDto reponseDto = new UserResponseDto(user);

        //then
        assertEquals(reponseDto.getUsercode(), "JunMo00");
        assertEquals(reponseDto.getName(),"강준모");
        assertEquals(reponseDto.getIntroduce(),"한 줄 소개");
        assertEquals(reponseDto.getEmail(),"ppkkgoaa@naver.com");
        assertEquals(reponseDto.getProfileImageUrl(),"프로필 주소");
    }

    @Test
    @DisplayName("Post Entity Test1")
    public void postTest1(){
        //given
        User user = User.builder()
                .usercode(usercode)
                .password(password)
                .email(email)
                .build();

        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("게시물 제목");
        postRequestDto.setContent("게시물 내용");

        //when
        Post post = new Post(postRequestDto,user);

        //then
        assertEquals(post.getTitle(), "게시물 제목");
        assertEquals(post.getContent(), "게시물 내용");
        assertEquals(post.getUser().getUsercode(), "JunMo00");
    }

    @Test
    @DisplayName("Comment Entity Test")
    public void commentTest1(){
        //given
        User user = User.builder()
                .usercode(usercode)
                .password(password)
                .email(email)
                .build();
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("게시물 제목");
        postRequestDto.setContent("게시물 내용");
        Post post = new Post(postRequestDto,user);

        //when
        Comment comment = new Comment("댓글 내용",user,post);

        //then

        assertEquals(comment.getContent(), "댓글 내용");
        assertEquals(comment.getLikeCount(), 0);
        assertEquals(comment.getPost().getContent(), "게시물 내용");
        assertEquals(comment.getUser().getUsercode(), "JunMo00");
    }


}
class ValidationTest<T>{
    private static Validator validator;

    public ValidationTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public Set<ConstraintViolation<T>> valid(T data){
        return validator.validate(data, ValidationGroups.NotBlankGroup.class, ValidationGroups.PatternGroup.class, ValidationGroups.SizeGroup.class,  ValidationGroups.LengthGroup.class);
    }
}
