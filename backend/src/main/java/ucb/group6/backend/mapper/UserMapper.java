package ucb.group6.backend.mapper;

import ucb.group6.backend.model.User;
import ucb.group6.backend.requests.UserLoginRequestBody;
import ucb.group6.backend.requests.UserPostRequestBody;
import ucb.group6.backend.requests.UserPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper((UserMapper.class));

    User toUser(UserLoginRequestBody userLoginRequestBody);
    User toUser(UserPostRequestBody userPostRequestBody);
    User toUser(UserPutRequestBody userPutRequestBody);
}
