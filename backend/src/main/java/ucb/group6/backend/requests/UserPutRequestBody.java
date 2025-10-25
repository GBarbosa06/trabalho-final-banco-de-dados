package ucb.group6.backend.requests;

import lombok.Data;

@Data
public class UserPutRequestBody {
    private long id;
    private String name;
    private String email;
    private String password;
}
