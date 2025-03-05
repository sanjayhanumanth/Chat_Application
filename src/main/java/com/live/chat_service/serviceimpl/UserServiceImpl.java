package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.EditProfileDto;
import com.live.chat_service.dto.LoginDto;
import com.live.chat_service.dto.UserDto;
import com.live.chat_service.dto.UserListDTO;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.Role;
import com.live.chat_service.model.User;
import com.live.chat_service.repository.RoleRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.response.UserContextHolder;
import com.live.chat_service.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public SuccessResponse<Object> userRegister(UserDto userDto) {
        SuccessResponse<Object> successResponse=new SuccessResponse<>();
        Optional<User> userOptional=userRepository.findByEmailId(userDto.getEmail());
        Optional< Role> roleOptional=roleRepository.findById(userDto.getRoleId());
        if (userOptional.isEmpty()){
            User user=new User();
            String hashedPassword = passwordEncoder.encode(userDto.getPassword());
            user.setUserName(userDto.getName());
            user.setEmailId(userDto.getEmail());
            user.setPassword(hashedPassword);
            user.setActive(true);
            user.setRole(roleOptional.get());
            user.setDeletedFlag(false);
            user.setCreatedAt(Timestamp.from(Instant.now()));
            user.setCreatedBy(1L);
            userRepository.save(user);
        }else {
            throw new CustomValidationExceptions("Email Already Exists");
        }
        successResponse.setStatusMessage("User Register Successfully..");
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> editProfile(EditProfileDto editProfileDto, MultipartFile imageFile) throws IOException {
        SuccessResponse<Object> successResponse=new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        Optional<User> userOptional = userRepository.findByIdIsActive(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                user.setImage(imageBytes);
            }
            user.setUserName(editProfileDto.getUserName());
            userRepository.save(user);
        }
        return successResponse;
    }

    @Override
    public SuccessResponse<List<UserListDTO>> getUserList() {
        SuccessResponse<List<UserListDTO>> successResponse = new SuccessResponse<>();

        List<User> userList = userRepository.findAllIsActive();

        if (!userList.isEmpty()) {
            List<UserListDTO> userDTOList = userList.stream().map(user -> {
                UserListDTO dto = new UserListDTO();
                dto.setId(user.getId());
                dto.setName(user.getUserName());
                dto.setEmail(user.getEmailId());
                dto.setRoleId(user.getRole().getId());
                dto.setImage(user.getImage());
                return dto;
            }).collect(Collectors.toList());

            successResponse.setData(userDTOList);
            successResponse.setStatusMessage("Users fetched successfully.");
        } else {
            successResponse.setStatusMessage("No users found.");
        }

        return successResponse;
    }


    public User userLogin(LoginDto loginDto) {
        User user;
        Optional<User> userOptional=userRepository.findByEmailId(loginDto.getEmail());
        if (userOptional.isPresent()){
            if (passwordEncoder.matches(loginDto.getPassword(), userOptional.get().getPassword())){
                user=userOptional.get();
            }else {
                throw new CustomValidationExceptions("Invalid Password");
            }
        }else {
            throw new CustomValidationExceptions("Invalid Email");
        }
    return user;
    }

}
