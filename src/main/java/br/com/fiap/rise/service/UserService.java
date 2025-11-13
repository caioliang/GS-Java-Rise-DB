package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.UserDTO;
import br.com.fiap.rise.model.User;
import br.com.fiap.rise.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return convertToDTO(user);
    }

    public UserDTO create(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }
        if (userRepository.findByCpf(userDTO.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado.");
        }

        User savedUser = userRepository.save(convertToEntity(userDTO));
        return convertToDTO(savedUser);
    }

    public UserDTO update(UUID id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para atualização."));

        if (!existingUser.getEmail().equals(userDTO.getEmail())) {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                throw new RuntimeException("O email " + userDTO.getEmail() + " já está cadastrado para outro usuário.");
            }
        }

        if (!existingUser.getCpf().equals(userDTO.getCpf())) {
            if (userRepository.findByCpf(userDTO.getCpf()).isPresent()) {
                throw new RuntimeException("O CPF " + userDTO.getCpf() + " já está cadastrado para outro usuário.");
            }
        }

        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setCpf(userDTO.getCpf());
        existingUser.setBirthDate(userDTO.getBirthDate());

        User updatedUser = userRepository.save(existingUser);
        return convertToDTO(updatedUser);
    }

    public void delete(UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para exclusão."));

        userRepository.delete(existingUser);
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCpf(dto.getCpf());
        user.setBirthDate(dto.getBirthDate());

        return user;
    }

    private UserDTO convertToDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setCpf(entity.getCpf());
        dto.setBirthDate(entity.getBirthDate());

        return dto;
    }
}
