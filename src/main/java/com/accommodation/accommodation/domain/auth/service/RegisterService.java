package com.accommodation.accommodation.domain.auth.service;

import com.accommodation.accommodation.domain.auth.exception.AuthException;
import com.accommodation.accommodation.domain.auth.exception.errorcode.AuthErrorCode;
import com.accommodation.accommodation.domain.auth.model.entity.User;
import com.accommodation.accommodation.domain.auth.model.request.EmailCheckRequest;
import com.accommodation.accommodation.domain.auth.model.request.EmailSendRequest;
import com.accommodation.accommodation.domain.auth.model.request.RegisterRequest;
import com.accommodation.accommodation.domain.auth.repository.UserRepository;
import com.accommodation.accommodation.global.util.EmailUtil;
import com.accommodation.accommodation.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final RedisUtil redisUtil;
    private final PasswordEncoder pe;

    @Transactional
    public ResponseEntity singUp(RegisterRequest request) {

        checkAccessKey(request.email(), request.accessKey());

        userRepository.save(
                User.builder()
                        .email(request.email())
                        .password(pe.encode(request.password()))
                        .name(request.name())
                        .build()
        );

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity emailSend(EmailSendRequest request) {

        String successKey = getSuccessKey();

        emailUtil.sendSingUpRandomNumberEmail(request.email(), "인증번호 : " + successKey);

        redisUtil.setValue("auth:" + request.email(), successKey, 30L, TimeUnit.MINUTES);

        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity emailCheck(EmailCheckRequest request) {
        checkAccessKey(request.email(), request.accessKey());
        return ResponseEntity.ok().build();
    }


    private void checkAccessKey(String email, String accessKey) {

        String realAccessKey = (String) redisUtil.getValue("auth:" + email).orElseThrow(
                () -> {
                    throw new AuthException(AuthErrorCode.WRONG_EMAIL_SUCCESS_KEY);
                }
        );

        if (!accessKey.equals(realAccessKey))
            throw new AuthException(AuthErrorCode.WRONG_EMAIL_SUCCESS_KEY);

    }

    private String getSuccessKey() {
        String randomStr = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            randomStr += random.nextInt(9);
        }
        return randomStr;
    }

}
