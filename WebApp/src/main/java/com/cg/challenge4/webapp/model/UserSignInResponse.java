package com.cg.challenge4.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignInResponse {
    private String accessToken;
    private String idToken;
    private String refreshToken;
    private String tokenType;
    private Integer expiresIn;



}
