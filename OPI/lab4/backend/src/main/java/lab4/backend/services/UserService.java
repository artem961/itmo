package lab4.backend.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import lab4.backend.data.entities.UserEntity;
import lab4.backend.data.repositories.user.postgres.UserRepository;
import lab4.backend.dto.UserDTO;
import lab4.backend.services.exceptions.ServiceException;
import lab4.backend.utils.mapping.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class UserService {
    @EJB
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        if (!isUserExists(userDTO)) {
            return UserMapper.entityToDTO(
                    userRepository.saveUser(UserMapper.dtoToEntity(userDTO)));
        } else {
            throw new ServiceException(String.format("User with name %s already exists",  userDTO.getUsername()));
        }
    }

    public UserDTO findUserByName(String name) {
        UserEntity userEntity = userRepository.findUserByName(name)
                .orElseThrow(() -> new ServiceException(String.format("User with name %s not found", name)));
        return UserMapper.entityToDTO(userEntity);
    }

    public UserDTO findUserById(Integer id) {
        UserEntity userEntity = userRepository.findUserById(id)
                .orElseThrow(() -> new ServiceException(String.format("User with id %s not found", id)));
        return UserMapper.entityToDTO(userEntity);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(UserMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    public Boolean isUserExists(UserDTO userDTO) {
        return userRepository.findUserByName(userDTO.getUsername()).isPresent();
    }

    public UserDTO getUserIfPasswordValid(UserDTO user) {
        UserEntity userEntity = userRepository.findUserByName(user.getUsername())
                .orElseThrow(() -> new ServiceException(String.format("User with name %s not found", user.getUsername())));

        if (userEntity.getPassword().equals(user.getPassword())) {
            return UserMapper.entityToDTO(userEntity);
        } else{
            throw new ServiceException("Invalid password");
        }
    }
}
