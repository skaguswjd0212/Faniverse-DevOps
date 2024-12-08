package fantastic.faniverse.auth.controller;

import fantastic.faniverse.auth.dto.LoginRequestDto;
import fantastic.faniverse.auth.dto.LoginResponseDto;
import fantastic.faniverse.auth.dto.LogoutResponseDto;
import fantastic.faniverse.auth.service.AuthService;
import fantastic.faniverse.user.entity.User;
import fantastic.faniverse.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                                  HttpSession session,
                                                  HttpServletResponse response) {
        if (loginRequestDto.getEmail() == null || loginRequestDto.getPassword() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // 로그인 서비스에서 User 객체 반환
        User user = authService.login(loginRequestDto);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 세션에 userId 저장
        session.setAttribute("userId", user.getId());

        // 쿠키 설정
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setHttpOnly(true); // 클라이언트에서 접근 불가
        cookie.setPath("/"); // 쿠키 유효 범위
        cookie.setMaxAge(-1); // 세션 쿠키
        response.addCookie(cookie); // 쿠키 추가

        // User 객체를 LoginResponseDto로 변환
        LoginResponseDto loginResponseDto = new LoginResponseDto(
            user.getId(),
            user.getEmail(),
            user.getUsername() + " - CI/CD Deployed!"
        );

        return ResponseEntity.ok(loginResponseDto);
    }


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            authService.logout(userId);
            session.invalidate(); // 세션 무효화
            return ResponseEntity.ok(new LogoutResponseDto("Successfully logged out"));
        } else {
            return ResponseEntity.status(400).body(new LogoutResponseDto("No user logged in"));
        }
    }
}
