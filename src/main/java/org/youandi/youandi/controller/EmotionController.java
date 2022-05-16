package org.youandi.youandi.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.youandi.youandi.domain.response.CommonResult;
import org.youandi.youandi.dto.EmotionRequestDto;
import org.youandi.youandi.dto.PostRequestDto;
import org.youandi.youandi.service.EmotionService;
import org.youandi.youandi.service.ResponseService;

@RestController
@RequiredArgsConstructor
@Api(tags = {"5. 감정 컨트롤러"})
@RequestMapping(value = "/emotions")
public class EmotionController {

    private final ResponseService responseService;
    private final EmotionService emotionService;

    @ApiOperation(value = "게시글별 감정 추가", notes = "게시글에 감정 결과를 추가한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("")
    public CommonResult addEmotionToPost(@RequestBody @ApiParam(value = "게시글과 감정 분석 결과", required = true)EmotionRequestDto emotionRequestDto) {
       return  responseService.getSingleResult(emotionService.addEmotionToPost(emotionRequestDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "게시글별 감정 조회", notes = "게시글별 감정 결과를 조회한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/{postId}")
    public CommonResult getEmotionByPost(@PathVariable Long postId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getSingleResult(emotionService.getEmotionByPost(email, postId));
    }
}
