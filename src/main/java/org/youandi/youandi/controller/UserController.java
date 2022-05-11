package org.youandi.youandi.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.youandi.youandi.domain.response.SingleResult;
import org.youandi.youandi.dto.UserResponseDto;
import org.youandi.youandi.service.ResponseService;
import org.youandi.youandi.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(tags = {"3. 사용자 컨트롤러"})
public class UserController {

    private final ResponseService responseService;
    private final UserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK !!"),
            @ApiResponse(code = 400, message = "BAD REQUEST !!"),
            @ApiResponse(code = 404, message = "NOT FOUND !!"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR !!")
    })
    @ApiOperation(value = "회원 정보 조회", notes = "회원 정보를 조회한다.")
    @GetMapping("/user")
    public SingleResult<UserResponseDto> findUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return responseService.getSingleResult(userService.findByEmail(email));
    }

}
