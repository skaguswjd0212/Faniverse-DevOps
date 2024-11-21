package fantastic.faniverse.user.dto;



public class UserDto {
    private String password;
    private String username;
    private String email;

    // 기본 생성자
    public UserDto() {}

    // 모든 필드를 포함한 생성자
    public UserDto(String password, String username, String email) {
        this.password = password;
        this.username = username;
        this.email = email;
    }

    // Getter 및 Setter
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
