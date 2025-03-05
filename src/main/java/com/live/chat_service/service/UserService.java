package com.live.chat_service.service;

import com.live.chat_service.dto.EditProfileDto;
import com.live.chat_service.dto.UserDto;
import com.live.chat_service.response.SuccessResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {
    SuccessResponse<Object> userRegister(UserDto userDto);

    SuccessResponse<Object> editProfile(MultipartFile imageFile) throws IOException;
}
