package org.youandi.youandi.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.youandi.youandi.advice.CAuthenticationEntryPointException;
import org.youandi.youandi.domain.response.CommonResult;

@RequiredArgsConstructor
@RestController
@Api(tags = {"4. 에러 컨트롤러"})
@RequestMapping(value= "/exception")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public CommonResult entrypointException() {
        throw new CAuthenticationEntryPointException();
    }

    @GetMapping(value = "/accessdenied")
    public CommonResult accessdeniedException() {
        throw new AccessDeniedException("");
    }

}
