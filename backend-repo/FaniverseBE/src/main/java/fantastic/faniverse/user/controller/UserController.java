package fantastic.faniverse.user.controller;

import fantastic.faniverse.user.dto.RegisterRequestDto;
import fantastic.faniverse.user.dto.RegisterResponseDto;
import fantastic.faniverse.user.dto.UserDto;
import fantastic.faniverse.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // 사용자 조회
    @GetMapping("/{email}")
    public ResponseEntity<RegisterResponseDto> getUserByEmail(@PathVariable String email) {
        RegisterResponseDto registerResponseDto = userService.getUserByEmail(email);
        if (registerResponseDto != null) {
            return ResponseEntity.ok(registerResponseDto);
        }
        return ResponseEntity.notFound().build();
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        System.out.println("Received UserDto: " + registerRequestDto);
        RegisterResponseDto createdUser = userService.createUser(registerRequestDto);
        return ResponseEntity.ok(createdUser);
    }
}
