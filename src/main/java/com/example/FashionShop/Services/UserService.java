package com.example.FashionShop.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.FashionShop.Entity.ShippingAddress;
import com.example.FashionShop.Entity.Transaction;
import com.example.FashionShop.Enum.Role;
import com.example.FashionShop.Repository.ShippingAddressRepository;
import com.example.FashionShop.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.UserResponse;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IUserSerive;
import com.example.FashionShop.Mapper.UserMapper;
import com.example.FashionShop.Repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements IUserSerive {
    UserRepository userRepository;
    ShippingAddressRepository shippingAddressRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    TransactionService transactionService;
    TransactionRepository transactionRepository;
    Cloudinary cloudinary;

    @NonFinal
    @Value("${urlServer}")
    private String urlServer;

    @NonFinal
    @Value("${clientId}")
    private String clientId;

    @NonFinal
    @Value("${apiKey}")
    private String apiKey;

    @NonFinal
    @Value("${checkSumKey}")
    private String checkSumKey;

    @NonFinal
    @Value("${urlFE}")
    private String urlFE;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public PageableResponse getUsers(Pageable pageable) {
        Page<User> pageOrigin = userRepository.findAll(pageable);
        List<User> users = pageOrigin.getContent();
        List<UserResponse> userResponsess = new ArrayList<>();
        for (User user : users)
        {
            UserResponse userResponse = userMapper.toUserResponse(user);
            userResponsess.add(userResponse);
        }
        PageableResponse pageableResponse = new PageableResponse()
                .builder()
                .results(userResponsess)
                .size(pageOrigin.getSize())
                .totalElements(pageOrigin.getTotalElements())
                .totalPages(pageOrigin.getTotalPages())
                .number(pageOrigin.getNumber())
                .build();
        return pageableResponse;
    }

    @Override
    public ApiResponse getUserById(Integer idUser) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        UserResponse userResponse = userMapper.toUserResponse(user);
        return new ApiResponse().builder().results(userResponse).build();
    }

    @Override
    public ApiResponse getInfor() {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(Integer.parseInt(idUser)).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        UserResponse userResponse = userMapper.toUserResponse(user);

        return ApiResponse.builder()
                .results(userResponse)
                .build();
    }

    @Override
    public ApiResponse createUser(UserCreationRequest request) {
        boolean isExistedEmail = userRepository.existsByEmail(request.getEmail());
        if(isExistedEmail){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDeleted(false);
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        // create shipping address
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setDefaultAddress(true);
        shippingAddress.setAddress(request.getAddress());
        shippingAddress.setUser(user);
        user.setRole(Role.USER.name());
        user = userRepository.save(user);
        shippingAddressRepository.save(shippingAddress);
        return new ApiResponse<>().builder().results(user).build();
    }

    @Override
    public ApiResponse updateUser(UpdateUserRequest request) {

        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(Integer.parseInt(idUser)).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        user = userMapper.updateUser(user, request);
        userRepository.save(user);
        return new ApiResponse().builder().results(user).build();
    }

    @Override
    public ApiResponse updateRole(UpdateRoleRequest request) {
        Integer idUser = request.getIdUser();
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        String newRole = request.getRole().toUpperCase().trim();
        boolean isValidRole = Arrays.stream(Role.values())
                .anyMatch(r -> r.name().equals(newRole));
        if(!isValidRole)
        {
            throw new AppException(ErrorCode.ROLE_NOTFOUND);
        }else
        {
            user.setRole(newRole);
        }
        user = userRepository.save(user);
        UserResponse userResponse = userMapper.toUserResponse(user);
        return ApiResponse
                .builder()
                .results(userResponse)
                .build();
    }



    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse getRoles() {
        List<String> roleList = userRepository.getRoles();
        return ApiResponse
                .builder()
                .results(roleList)
                .build();
    }

    @Override
    public PageableResponse filterUser(String query, FilterUserRequest request, Pageable pageable) {
        List<Boolean> hasDeleted = request.getIsDeleteds();
        List<Boolean> hasCurrentStatus = request.getCurrentStatus();
        List<String> hasRoles = request.getRoles();


        Specification<User> spec = Specification.where(UserSpecification.hasDeleted(hasDeleted))
                .and(UserSpecification.hasCurrentStatus(hasCurrentStatus))
                .and(UserSpecification.hasRole(hasRoles));

        if (query != null && !query.trim().isEmpty()) {
            String[] words = query.trim().split("\\s+");
            for (String word : words) {
                spec = spec.and(UserSpecification.nameContains(word));
            }
        }
        Page<User> page = userRepository.findAll(spec, pageable);
        List<User> userList = page.getContent();
        List<UserResponse> userResponseList = new ArrayList<>();
        for(User user : userList){
            UserResponse userResponse = userMapper.toUserResponse(user);
            userResponseList.add(userResponse);
        }

        return PageableResponse
                .builder()
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .results(userResponseList)
                .number(page.getNumber())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    public CheckoutResponseData topUpWallet(TopUpWalletRequest request) throws Exception {
        PayOS payOS = new PayOS(clientId, apiKey, checkSumKey);

        Transaction transaction = transactionService.createTransaction(request.getTopUpWalletValue());
        // Tao idOrder ngau nhien
        long timeStamp = System.currentTimeMillis();
        String idCreate = timeStamp + transaction.getIdTransaction().toString();
        Long idOrder = Long.valueOf(idCreate);

        PaymentData paymentData = PaymentData.builder()
                .orderCode(idOrder)
                .amount((int) request.getTopUpWalletValue())
                .description("Thanh toan don hang")
                .returnUrl(urlFE + "/success")
                .cancelUrl(urlFE + "/cancle")
                .build();
        CheckoutResponseData checkoutResponseData = payOS.createPaymentLink(paymentData);

        transactionRepository.save(transaction);
        payOS.confirmWebhook(urlServer+"/transaction/walletWebhook");

        return checkoutResponseData;
    }

    @Override
    public void setAvaiable() {
        var context = SecurityContextHolder.getContext();
        Integer idUser = Integer.parseInt(context.getAuthentication().getName());
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        user.setAvailable(true);
        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse delete(DeleteUserRequest request) {
        List<Integer> idUsers = request.getIdUsers();

        List<User> users = userRepository.findAllById(idUsers);

        if (users.size() != idUsers.size()) {
            throw new AppException(ErrorCode.USER_NOTFOUND);
        }

        users.forEach(user -> {
            if (!user.getRole().equals(Role.ADMIN.name())
            ) {
                user.setDeleted(true);
            }
        });
        userRepository.saveAll(users);
        return ApiResponse
                .builder()
                .code(200)
                .message("Vô hiệu hóa người dùng thành công!!!")
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse active(ActiveUserRequest request) {
        List<Integer> idUsers = request.getIdUsers();

        List<User> users = userRepository.findAllById(idUsers);

        if (users.size() != idUsers.size()) {
            throw new AppException(ErrorCode.USER_NOTFOUND);
        }

        users.forEach(user -> user.setDeleted(false));
        userRepository.saveAll(users);
        return ApiResponse
                .builder()
                .code(200)
                .message("Kích hoạt người dùng thành công!!!")
                .build();
    }

    @Override
    public ApiResponse updatePassword(UpdatePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idUser = Integer.valueOf(authentication.getName());

        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        System.out.println(request.getNewPassword());
        String password = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(password);

        user = userRepository.save(user);
        UserResponse userResponse = userMapper.toUserResponse(user);

        return ApiResponse
                .builder()
                .results(userResponse)
                .build();
    }

    @Override
    public ApiResponse updateImage(UpdateImageRequest request) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idUser = Integer.valueOf(authentication.getName());

        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        if(isBase64Image(request.getImage()))
        {
            Map<String, Object> cloudResponse = uploadCloudinary(request.getImage());

            String newImages = (String) cloudResponse.get("secure_url");

            user.setImage(newImages);
        }

        userRepository.save(user);
        return new ApiResponse();
    }

    public Map<String, Object> uploadCloudinary(String base64Image) throws IOException {
        return cloudinary.uploader()
                .upload(base64Image.getBytes(), ObjectUtils.emptyMap());
    }

    public static boolean isBase64Image(String image) {
        return image != null && image.startsWith("data:image/");
    }
}
