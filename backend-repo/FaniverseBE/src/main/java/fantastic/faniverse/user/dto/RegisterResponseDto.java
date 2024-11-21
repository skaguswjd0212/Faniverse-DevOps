package fantastic.faniverse.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDto {
    private Long id;
    private String username;
    //private String password;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /*)
    @Override
    public String toString() {
        return "UserDto{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    } */
}
