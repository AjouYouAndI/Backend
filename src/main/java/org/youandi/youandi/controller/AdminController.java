package org.youandi.youandi.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.youandi.youandi.domain.response.CommonResult;
import org.youandi.youandi.dto.SignUpRequestDto;
import org.youandi.youandi.service.ResponseService;
import org.youandi.youandi.service.UserService;

@RestController
@RequiredArgsConstructor
@Api(tags = {"2. 관리자 컨트롤러"})
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ResponseService responseService;

    @ApiOperation(value = "관리자 회원가입", notes = "관리자 회원가입을 한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/signup")
    public CommonResult signUp(@RequestBody @ApiParam(value = "가입할 관리자 정보", required = true) SignUpRequestDto requestDto) {
        userService.signup(requestDto, true);
        return responseService.getSuccessResult();
    }



}
