package com.cg.challenge4.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequest {
    private String username;
    private String email;
    private String password;
    private String group;


}
