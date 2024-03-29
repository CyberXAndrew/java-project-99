package hexlet.code.mapper;

import hexlet.code.dto.UserDTO;
import hexlet.code.model.User;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserUpdateDTO;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO userCreateDTO);
    public abstract UserDTO map(User user);
    public abstract void update(UserUpdateDTO updateDTO, @MappingTarget User user);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO createDTO) {
        String password = createDTO.getPassword();
        createDTO.setPassword(passwordEncoder.encode(password));
    }

    @BeforeMapping
    public void encryptPassword(UserUpdateDTO updateDTO) {
        if (updateDTO.getPassword() != null) {
            String password = updateDTO.getPassword().get();
            updateDTO.setPassword(JsonNullable.of(passwordEncoder.encode(password)));
        }
    }
}
