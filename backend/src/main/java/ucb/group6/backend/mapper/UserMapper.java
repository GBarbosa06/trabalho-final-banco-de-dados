package ucb.group6.backend.mapper;

import ucb.group6.backend.model.User;
import ucb.group6.backend.requests.UserLoginRequestBody;
import ucb.group6.backend.requests.UserPostRequestBody;
import ucb.group6.backend.requests.UserPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(UserLoginRequestBody userLoginRequestBody);

    //@Mapping(target = "id", ignore = true)
    User toUser(UserPostRequestBody userPostRequestBody);

//    @Mapping(target = "id", ignore = true)
    User toUser(UserPutRequestBody userPutRequestBody);
}
