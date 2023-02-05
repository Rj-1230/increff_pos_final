package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserForm {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min=8,max=15)
    private String password;

}