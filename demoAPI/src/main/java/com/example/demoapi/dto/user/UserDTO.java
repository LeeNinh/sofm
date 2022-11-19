package com.example.demoapi.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserDTO {

    private int id;

    @NotEmpty(message = "{not.empty}")
    private String name;
    @NotEmpty(message = "{not.empty}")
    private String username;
    @NotEmpty(message = "{not.empty}")
    private String password;

    @JsonIgnore
    private MultipartFile file;

    private String avatar;

    @NotEmpty(message = "{not.empty}")
    private String birthdate;

    @NotEmpty(message = "{not.empty}")
    private String email;

    List<String> roles;

}
