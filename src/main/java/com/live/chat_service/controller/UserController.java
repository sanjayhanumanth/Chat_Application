package com.live.chat_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.live.chat_service.dto.EditProfileDto;
import com.live.chat_service.dto.UserDto;
import com.live.chat_service.dto.UserListDTO;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/liveChat")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public SuccessResponse<Object> userRegister(@RequestBody UserDto userDto) {
        return service.userRegister(userDto);
    }

    @PostMapping(value = "/editProfile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SuccessResponse<Object> editProfile(@RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        return service.editProfile(imageFile);
    }

    @GetMapping("/userList")
    public SuccessResponse<List<UserListDTO>> getUserList(){
        return service.getUserList();
    }
}
