package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.*;
import com.live.chat_service.exception.CustomExceptionHandler;
import com.live.chat_service.constant.Constant;
import com.live.chat_service.dto.LoginDto;
import com.live.chat_service.dto.UserDto;
import com.live.chat_service.dto.UserListDTO;
import com.live.chat_service.dto.UserOtpValidationDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.Role;
import com.live.chat_service.model.User;
import com.live.chat_service.model.UserValidation;
import com.live.chat_service.repository.ChatMessageRepository;
import com.live.chat_service.repository.RoleRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.repository.UserValidationRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.response.UserContextHolder;
import com.live.chat_service.service.UserService;
import org.modelmapper.ModelMapper;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
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

    private final UserValidationRepository userValidationRepository;

    private final JavaMailSender javaMailSender;

    private final ChatMessageRepository chatMessageRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserValidationRepository userValidationRepository, JavaMailSender javaMailSender,  ChatMessageRepository chatMessageRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userValidationRepository = userValidationRepository;
        this.javaMailSender = javaMailSender;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public SuccessResponse<Object> userRegister(UserDto userDto) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Optional<User> userOptional = userRepository.findByEmailId(userDto.getEmail());
        Optional<Role> roleOptional = roleRepository.findById(userDto.getRoleId());
        if (userOptional.isEmpty()) {
            User user = new User();
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
        } else {
            throw new CustomValidationExceptions("Email Already Exists");
        }
        successResponse.setStatusMessage("User Register Successfully..");
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> editProfile(MultipartFile imageFile) throws IOException {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
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

    public SuccessResponse<Object> getUser(Long id) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        User user = userRepository.findByIdIsActive(id)
                .orElseThrow(() -> new CustomValidationExceptions("Invalid Id"));

        UserGetDTO dto = new UserGetDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmailId(user.getEmailId());
        dto.setRoleId(user.getRole().getId());
        dto.setImage(user.getImage());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setTitle(user.getTitle());
        dto.setDisplayName(user.getDisplayName());
        dto.setStatus(user.getStatus());

        successResponse.setData(dto);
        successResponse.setStatusMessage("Success");
        successResponse.setStatusCode(200);
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> editUser(UserEditDTO userEditDTO) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        User user = userRepository.findByIdIsActive(userId)
                .orElseThrow(() -> new CustomValidationExceptions("Invalid Id"));


        //user.setEmailId(userEditDTO.getUserMail());
        user.setUserName(userEditDTO.getUserName());
        user.setDisplayName(userEditDTO.getDisplayName());
        user.setTitle(userEditDTO.getTitle());
        user.setPhoneNumber(userEditDTO.getPhoneNumber());
        user.setStatus(userEditDTO.getStatus());
        userRepository.save(user);
        successResponse.setStatusMessage("Success");
        successResponse.setStatusCode(200);
        return successResponse;

    }

    @Transactional
    @Override
    public SuccessResponse<Object> forgotPassword(String email) {
        SuccessResponse<Object> successResponse=new SuccessResponse<>();
        SecureRandom fieldRandom = new SecureRandom();
        try{
            if (!email.isEmpty()){
                Optional<User> userOptional=userRepository.findByEmailId(email);
                if (userOptional.isPresent()){
                    User user=userOptional.get();
                    Optional<UserValidation> userValidationOptional=userValidationRepository.findByEmail(email);
                    userValidationOptional.ifPresent(userValidation -> userValidationRepository.deleteById(userValidation.getId()));
                    Integer otp = fieldRandom.nextInt(9999);
                    UserValidation userValidation = new UserValidation();
                    userValidation.setEmail(user.getEmailId());
                    userValidation.setOtp(String.valueOf(otp));
                    userValidationRepository.save(userValidation);
                    String success=sendOtpEmail(userOptional.get().getUserName(),userValidation.getEmail(),userValidation.getOtp());
                    if (success.equalsIgnoreCase(Constant.SUCCESS)) {
                        successResponse.setData(Constant.OTP_SEND);
                    }
                } else {
                    throw new CustomValidationExceptions(Constant.INVALID_EMAIL);
                }
            }else {
                throw new CustomValidationExceptions(Constant.ENTER_EMAIL);
            }
        } catch (Exception e) {
            throw new CustomValidationExceptions(Constant.IO_EXCEPTION);
        }
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> verifyOTP(UserOtpValidationDto userOtpValidationDto) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        if (!userOtpValidationDto.getEmail().isEmpty() && userOtpValidationDto.getOtp() != null) {
            Optional<UserValidation> userValidationOptional = userValidationRepository.findByEmail(userOtpValidationDto.getEmail());
            if (userValidationOptional.isPresent()) {
                if (userValidationOptional.get().getOtp().equalsIgnoreCase(userOtpValidationDto.getOtp())) {
                    successResponse.setStatusMessage(Constant.OTP_VERIFIED);
                } else {
                    throw new CustomValidationExceptions(Constant.INVALID_OTP);
                }
            } else {
                throw new CustomValidationExceptions(Constant.INVALID_EMAIL);
            }
        } else {
            throw new CustomValidationExceptions(Constant.EMAIL_AND_OTP);
        }
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> updatePassword(LoginDto loginDto) {
        SuccessResponse<Object> successResponse=new SuccessResponse<>();
        if (!loginDto.getEmail().isEmpty() && !loginDto.getPassword().isEmpty()) {
            Optional<User> userOptional = userRepository.findByEmailId(loginDto.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String hashedPassword = passwordEncoder.encode(loginDto.getPassword());
                user.setPassword(hashedPassword);
                user.setModifiedBy(user.getId());
                user.setModifiedAT(Timestamp.from(Instant.now()));
                userRepository.save(user);
                successResponse.setStatusMessage(Constant.PASSWORD_UPDATED);
            } else {
                throw new CustomValidationExceptions(Constant.INVALID_EMAIL);
            }
        }else {
            throw new CustomValidationExceptions(Constant.ENTER_EMAIL_PASSWORD);
        }
        return successResponse;
    }


    public String sendOtpEmail(String userName, String email, String otp) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(Constant.NO_REPLY_MAIL);
            mimeMessageHelper.setSubject(Constant.EMAIL_SUBJECT);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText("<html><b style=\"font-size:1rem;\">Dear "+userName+"</b><br>"+
                    "We receive a request to reset your Coherent Chat Application Account. Please use the following One-Time Password(OTP) " +
                    "to proceed with resetting your password:<br><b>"+otp+"</b><br>"+
                    "<p></p><p><b>Thanks & Regards,</b><p>Coherent Team</p><p>web : www.coherent.in</p><p style=margin-top:-50px> " +
                    "</p></html>", true);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (Exception e) {
            throw new CustomValidationExceptions(Constant.ERROR_CODE);
        }
        return Constant.SUCCESS;
    }


    @Override
    public SuccessResponse<List<UserListDTO>> getUserList(String search) {
        SuccessResponse<List<UserListDTO>> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        List<User> userList;
        if (search == null) {
            userList = userRepository.findAllIsActive();

        } else {
            userList = userRepository.findByName(search);
        }

        if (!userList.isEmpty()) {
            List<UserListDTO> userDTOList = userList.stream().map(user -> {
                UserListDTO dto = new UserListDTO();
                dto.setId(user.getId());
                dto.setName(user.getUserName());
                dto.setEmail(user.getEmailId());
                dto.setRoleId(user.getRole().getId());
                //dto.setImage(user.getImage());
                dto.setStatus(user.getStatus());
                dto.setDisplayName(user.getDisplayName());
                dto.setPhoneNumber(user.getPhoneNumber());
                dto.setTitle(user.getTitle());
                Long unreadCount = chatMessageRepository.countBySenderAndReceiverAndReadFlagFalse(dto.getId(), userId);
                dto.setCount(unreadCount);
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
        Optional<User> userOptional = userRepository.findByEmailId(loginDto.getEmail());
        if (userOptional.isPresent()) {
            if (passwordEncoder.matches(loginDto.getPassword(), userOptional.get().getPassword())) {
                user = userOptional.get();
            } else {
                throw new CustomValidationExceptions("Invalid Password");
            }
        } else {
            throw new CustomValidationExceptions("Invalid Email");
        }
        return user;
    }

}
