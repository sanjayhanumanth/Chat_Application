package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.*;
import com.live.chat_service.constant.Constant;
import com.live.chat_service.dto.LoginDto;
import com.live.chat_service.dto.UserDto;
import com.live.chat_service.dto.UserListDTO;
import com.live.chat_service.dto.UserOtpValidationDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.model.GroupChatUser;
import com.live.chat_service.model.Role;
import com.live.chat_service.model.User;
import com.live.chat_service.model.UserAccessLog;
import com.live.chat_service.model.UserValidation;
import com.live.chat_service.repository.ChatMessageRepository;
import com.live.chat_service.repository.GroupChatUserRepository;
import com.live.chat_service.repository.RoleRepository;
import com.live.chat_service.repository.UserAccessLogRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.repository.UserValidationRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.response.UserContextHolder;
import com.live.chat_service.service.UserService;
import com.live.chat_service.util.CommonUtil;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final GroupChatUserRepository groupChatUserRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserAccessLogRepository userAccessLogRepository;
    private final RoleRepository roleRepository;

    private final UserValidationRepository userValidationRepository;

    private final JavaMailSender javaMailSender;

    private final ChatMessageRepository chatMessageRepository;

    private final CommonUtil commonUtil;

    public UserServiceImpl(GroupChatUserRepository groupChatUserRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, UserAccessLogRepository userAccessLogRepository, RoleRepository roleRepository, UserValidationRepository userValidationRepository, JavaMailSender javaMailSender, ChatMessageRepository chatMessageRepository, CommonUtil commonUtil) {
        this.groupChatUserRepository = groupChatUserRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userAccessLogRepository = userAccessLogRepository;
        this.roleRepository = roleRepository;
        this.userValidationRepository = userValidationRepository;
        this.javaMailSender = javaMailSender;
        this.chatMessageRepository = chatMessageRepository;
        this.commonUtil = commonUtil;
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
    public SuccessResponse<Object> editProfile(MultipartFile imageFile, MultipartFile coverImage) throws IOException {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        Optional<User> userOptional = userRepository.findByIdIsActive(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (imageFile != null && !imageFile.isEmpty()) {
                byte[] imageBytes = imageFile.getBytes();
                user.setImage(imageBytes);
            }
            if (coverImage != null && !coverImage.isEmpty()) {
                byte[] coverBytes = coverImage.getBytes();
                user.setCoverImage(coverBytes);
            }
            userRepository.save(user);
        }
        return successResponse;
    }

    public SuccessResponse<Object> getUser(Long id) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId(); 

        User user = userRepository.findByIdIsActive(id)
                .orElseThrow(() -> new CustomValidationExceptions("Invalid Id"));

        updateUserAccessLog(userId, id);

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

    @Transactional
    public void updateUserAccessLog(Long userId, Long contactUserId) {
        if (userId.equals(contactUserId)) {
            return;
        }

        Optional<UserAccessLog> logOptional = userAccessLogRepository.findByUserIdAndContactUserId(userId, contactUserId);

        if (logOptional.isPresent()) {
            UserAccessLog log = logOptional.get();
            log.setLastContacted(LocalDateTime.now());
            userAccessLogRepository.save(log);
        } else {
            UserAccessLog newLog = new UserAccessLog();
            newLog.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            newLog.setContactUser(userRepository.findById(contactUserId).orElseThrow(() -> new RuntimeException("Contact User not found")));
            newLog.setLastContacted(LocalDateTime.now());
            userAccessLogRepository.save(newLog);
        }
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
    public SuccessResponse<Map<String, Object>> getUserList(String search) {
        SuccessResponse<Map<String, Object>> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        List<User> userList = new ArrayList<>();
        List<GroupChatProjection> groupUsers;

        if (search == null) {
            List<ChatMessage> chatMessageList=chatMessageRepository.findFrequentlyContacted(userId);


            Set<Long> values=new HashSet<>();
            for (ChatMessage chatMessage:chatMessageList){
                if (!Objects.equals(userId, chatMessage.getSender().getId()) && values.add(chatMessage.getSender().getId())){
                    Optional<User> user=userRepository.findByIdIsActive(chatMessage.getSender().getId());
                    if (user.isPresent()) {
                        userList.add(user.get());
                    }
                }else if (!Objects.equals(userId, chatMessage.getReceiver().getId()) && values.add(chatMessage.getReceiver().getId())) {
                    Optional<User> user=userRepository.findByIdIsActive(chatMessage.getReceiver().getId());
                    if (user.isPresent()) {
                        userList.add(user.get());
                    }
                }
            }

            groupUsers = groupChatUserRepository.findUserGroup(userId);

            Optional<User> loggedInUserOpt = userRepository.findByIdIsActive(userId);
            loggedInUserOpt.ifPresent(userList::add);
        } else {
            userList = userRepository.findByName(search);
            groupUsers = groupChatUserRepository.findByGroupName(search, userId);
        }
        List<GroupDTO> groupDTOList=new ArrayList<>();
        List<UserListDTO> userDTOList=new ArrayList<>();
        if (!groupUsers.isEmpty()) {
            groupDTOList= groupUsers.stream().map(group -> {
                GroupDTO dto = new GroupDTO();
                dto.setGroupId(group.getGroupId());
                dto.setGroupName(group.getGroupName());
                return dto;
            }).toList();
        }
        if (!userList.isEmpty()) {
            userDTOList = userList.stream().map(user -> {
                UserListDTO dto = new UserListDTO();
                dto.setId(user.getId());
                dto.setName(user.getUserName());
                dto.setEmail(user.getEmailId());
                dto.setRoleId(user.getRole().getId());
                dto.setStatus(user.getStatus());
                dto.setDisplayName(user.getDisplayName());
                dto.setPhoneNumber(user.getPhoneNumber());
                dto.setTitle(user.getTitle());
                dto.setImage(user.getImage());
                Long unreadCount = chatMessageRepository.countBySenderIdAndReceiverIdAndReadFlagFalse(user.getId(), userId);
                dto.setCount(unreadCount);

                Optional<ChatMessage> chatMessageOptional = chatMessageRepository.findLastMessage(user.getId(), userId);
                chatMessageOptional.ifPresent(chatMessage -> {
                    String decryptedMessage=commonUtil.decryptMessage(chatMessage.getContent());
                        dto.setMessage(decryptedMessage);
                    dto.setLastMessageDateTime(String.valueOf(chatMessage.getTimestamp()));
                });

                return dto;
            }).toList();
        }
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userList", userDTOList);
        responseData.put("groupList", groupDTOList);

        successResponse.setData(responseData);
        successResponse.setStatusMessage("Users and groups fetched successfully.");

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
