package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.UserDTO;
import br.com.fiap.rise.model.User;
import br.com.fiap.rise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO create (UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }
        if (userRepository.findByCpf(userDTO.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado.");
        }

        User user = convertToEntity(userDTO);

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
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
