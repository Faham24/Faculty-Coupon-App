package com.example.facultycouponqr;

public class LoginRequest {
    private String username;
    private String password;
    private String user_type;

    public LoginRequest(String username, String password, String user_type) {
        this.username = username;
        this.password = password;
        this.user_type = user_type;
    }
}
