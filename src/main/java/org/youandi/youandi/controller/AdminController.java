package org.youandi.youandi.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.youandi.youandi.domain.User;
import org.youandi.youandi.domain.response.CommonResult;
import org.youandi.youandi.dto.SignUpRequestDto;
import org.youandi.youandi.service.AdminService;
import org.youandi.youandi.service.ResponseService;
import org.youandi.youandi.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"2. 관리자 컨트롤러"})
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ResponseService responseService;
    private final AdminService adminService;

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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "모든 회원 조회", notes = "관리자가 모든 회원을 조회한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/users")
    public CommonResult findAllUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<User> userList = adminService.findAllUser(email);
        return responseService.getListResult(userList);
    }


}
