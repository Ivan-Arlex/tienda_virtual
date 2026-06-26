package com.unipacifico_ingenieria_sistemas.tienda_virtual.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String email;
    private String password;
    private String fullName;
}
