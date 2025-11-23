package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.RegisterDTO;
import br.com.fiap.rise.dto.UserDTO;
import br.com.fiap.rise.exception.DataConflictException;
import br.com.fiap.rise.exception.ResourceNotFoundException;
import br.com.fiap.rise.model.User;
import br.com.fiap.rise.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Transactional
    public UserDTO registerUserAndCredentials(RegisterDTO dto) {
        if (userRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new DataConflictException("CPF já cadastrado.");
        }

        User newUser = convertRegistrationToUserEntity(dto);
        User savedUser = userRepository.save(newUser);
        authService.register(dto, savedUser);
        return convertUserEntityToDTO(userRepository.save(newUser));
    }

    @Cacheable(value = "users", key = "#id")
    public UserDTO findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        return convertUserEntityToDTO(user);
    }

    @CacheEvict(value = "users", key = "#id")
    public UserDTO update(UUID id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para atualização."));

        if (!existingUser.getCpf().equals(userDTO.getCpf())) {
            if (userRepository.findByCpf(userDTO.getCpf()).isPresent()) {
                throw new DataConflictException("O CPF " + userDTO.getCpf() + " já está cadastrado para outro usuário.");
            }
        }

        existingUser.setName(userDTO.getName());
        existingUser.setCpf(userDTO.getCpf());
        existingUser.setBirthDate(userDTO.getBirthDate());

        User updatedUser = userRepository.save(existingUser);
        return convertUserEntityToDTO(updatedUser);
    }

    @CacheEvict(value = "users", key = "#id")
    public void delete(UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para exclusão."));

        userRepository.delete(existingUser);
    }

    private User convertRegistrationToUserEntity(RegisterDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setCpf(dto.getCpf());
        user.setBirthDate(dto.getBirthDate());
        return user;
    }

    private UserDTO convertUserEntityToDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCpf(entity.getCpf());
        dto.setBirthDate(entity.getBirthDate());
        return dto;
    }
}
