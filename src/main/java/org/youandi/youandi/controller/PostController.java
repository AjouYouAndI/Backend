package org.youandi.youandi.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.youandi.youandi.domain.Post;
import org.youandi.youandi.domain.response.CommonResult;
import org.youandi.youandi.dto.PostRequestDto;
import org.youandi.youandi.service.PostService;
import org.youandi.youandi.service.ResponseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"4. 게시글 컨트롤러"})
@RequestMapping(value = "/posts")
public class PostController {

    private final PostService postService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시글 작성", notes = "게시글을 작성한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("")
    public CommonResult createPost(@RequestBody @ApiParam(value = "이메일 비밀번호", required = true)PostRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getSingleResult(postService.createPost(requestDto, email));
    }

    @ApiOperation(value = "모든 게시글 조회", notes = "모든 게시글을 조회한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("")
    public CommonResult getAllPost() {
        List<Post> postList = postService.getAllPost();
        return responseService.getListResult(postList);
    }

    @ApiOperation(value = "주변 모든 게시글 조회", notes = "주변 모든 게시글을 조회한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/around")
    public CommonResult getAroundPost(double lati, double longi) {
        List<Post> postList = postService.getAroundPosts(lati, longi);
        return responseService.getListResult(postList);
    }

    @ApiOperation(value = "게시글 단건 조회", notes = "게시글 단건을 조회한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/{postId}")
    public CommonResult getPostByIdx(@PathVariable Long postId) {
        return responseService.getSingleResult(postService.getPostByIdx(postId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 게시글 조회", notes = "회원의 모든 게시글을 조회한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/user")
    public CommonResult getPostByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<Post> postList = postService.getPostByUser(email);
        return responseService.getListResult(postList);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @DeleteMapping("/{postId}")
    public CommonResult deletePost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        postService.deletePost(postId, email);
        return responseService.getSuccessResult();
    }


    @ApiOperation(value = "지오 코딩 조회", notes = "경도 위도를 입력하하면 위치 정보 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/geo")
    public CommonResult getGeo(@RequestParam Double lati, @RequestParam Double longi) {
        Object object = postService.getRegion(lati, longi);
        return responseService.getSingleResult(object);
    }

}



