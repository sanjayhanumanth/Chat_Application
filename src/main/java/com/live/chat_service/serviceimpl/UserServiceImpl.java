package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.*;
import com.live.chat_service.exception.CustomExceptionHandler;
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
    public SuccessResponse<Object> editProfile(MultipartFile imageFile) throws IOException {
        SuccessResponse<Object> successResponse=new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        Optional<User> userOptional = userRepository.findByIdIsActive(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                user.setImage(imageBytes);
            }
            userRepository.save(user);
        }
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> getUser(Long id) {
        SuccessResponse<Object> successResponse=new SuccessResponse<>();

        Optional<User> userOptional=userRepository.findByIdIsActive(id);
        UserGetDTO userListDTO=new UserGetDTO();

        if(userOptional.isPresent())
        {
            User user=userOptional.get();
            userListDTO.setId(user.getId());
            userListDTO.setUserName(user.getUserName());
            userListDTO.setRoleId(user.getRole().getId());
            userListDTO.setUserMail(user.getEmailId());
            userListDTO.setImage(user.getImage());
            userListDTO.setPhoneNumber(user.getPhoneNumber());
            userListDTO.setTitle(user.getTitle());
            userListDTO.setDisplayName(user.getDisplayName());
            userListDTO.setStatus(user.getStatus());

        }
        else {
            throw new CustomValidationExceptions("Invalid Id");
        }
        successResponse.setData(userListDTO);
        successResponse.setStatusMessage("Success");
        return successResponse;

    }

    @Override
    public SuccessResponse<Object> editUser(UserEditDTO userEditDTO) {
        SuccessResponse<Object> successResponse=new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        Optional<User> userOptional=userRepository.findByIdIsActive(userId);
        User user;
        if(userOptional.isPresent())
        {
            user=userOptional.get();
            user.setEmailId(userEditDTO.getUserMail());
            user.setUserName(userEditDTO.getUserName());
            user.setDisplayName(userEditDTO.getDisplayName());
            user.setTitle(userEditDTO.getTitle());
            user.setPhoneNumber(userEditDTO.getPhoneNumber());
            user.setStatus(userEditDTO.getStatus());
        }
        else {
            throw new CustomValidationExceptions("Invalid Id");
        }
        userRepository.save(user);
        successResponse.setStatusMessage("Success");
        successResponse.setStatusCode(200);
        return successResponse;


    }

    @Override
    public SuccessResponse<List<UserListDTO>> getUserList(String search) {
        SuccessResponse<List<UserListDTO>> successResponse = new SuccessResponse<>();
        List<User> userList;
        if(search==null)
        {
            userList = userRepository.findAllIsActive();

        }
        else {
            userList=userRepository.findByName(search);
        }

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
