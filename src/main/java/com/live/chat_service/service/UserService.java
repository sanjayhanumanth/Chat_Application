package com.live.chat_service.service;

import com.live.chat_service.dto.LoginDto;
import com.live.chat_service.dto.UserDto;
import com.live.chat_service.dto.UserListDTO;
import com.live.chat_service.dto.UserOtpValidationDto;
import com.live.chat_service.response.SuccessResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
    SuccessResponse<Object> userRegister(UserDto userDto);

    SuccessResponse<List<UserListDTO>> getUserList(String search);
    SuccessResponse<Object> editProfile(MultipartFile imageFile) throws IOException;

    SuccessResponse<Object> forgotPassword(String email);

    SuccessResponse<Object> verifyOTP(UserOtpValidationDto userOtpValidationDto);

    SuccessResponse<Object> updatePassword(LoginDto loginDto);
}
