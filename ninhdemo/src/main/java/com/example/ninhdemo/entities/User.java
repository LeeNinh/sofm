package com.example.ninhdemo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String avatar;// URL

    @Column(unique = true)
    @NotBlank(message = "{not.empty}")
    @Size(min = 6, max = 20)
    private String username;
    @NotBlank(message = "{not.empty}")
    @Size(min = 6)
    private String password;

    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthdate;

    @JsonIgnore
    @Transient // field is not persistent.
    private MultipartFile file;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;

//	@ElementCollection
//	@CollectionTable(name = "user_role",
//		joinColumns = @JoinColumn(name = "user_id"))
//	@Column(name = "role")
//	private List<String> roles;
    /// ["ADMIN","MEMBER"]

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserRole> userRoles;

}
