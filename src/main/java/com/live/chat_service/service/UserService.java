package com.live.chat_service.service;

import com.live.chat_service.dto.EditProfileDto;
import com.live.chat_service.dto.UserDto;
import com.live.chat_service.dto.UserListDTO;
import com.live.chat_service.response.SuccessResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
    SuccessResponse<Object> userRegister(UserDto userDto);

    SuccessResponse<Object> editProfile(EditProfileDto editProfileDto, MultipartFile imageFile) throws IOException;

    SuccessResponse<List<UserListDTO>> getUserList();
}
