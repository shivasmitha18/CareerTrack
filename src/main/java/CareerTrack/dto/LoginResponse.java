package CareerTrack.dto;

public class LoginResponse {

    private String message;
    private Long userId;
    private String name;
    private String email;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(
            String message,
            Long userId,
            String name,
            String email) {

        this.message = message;
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public LoginResponse(
            String message,
            Long userId,
            String name,
            String email,
            String token) {

        this.message = message;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}