package com.unipacifico_ingenieri_sistemas.tienda_virtual.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    private String email;
    private String password;
    private String fullName;
}
