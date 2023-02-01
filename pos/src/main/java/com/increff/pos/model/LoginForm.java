package com.increff.pos.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class LoginForm
{
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min=8,max=15)
    private String password;
}