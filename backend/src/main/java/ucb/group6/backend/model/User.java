package ucb.group6.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "usuarios")
public class User {

    @Id
    private Long id;

    @NotBlank
    @Column(name = "nome")
    private String name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column(name = "senha")
    private String password;

    @Column(name = "grupo_id")
    private Integer groupId;

    @Column(name = "data_cadastro")
    private LocalDateTime registrationDate;

    private Boolean active;
}
