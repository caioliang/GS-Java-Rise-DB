package br.com.fiap.rise.service;

import br.com.fiap.rise.dto.AuthDTO;
import br.com.fiap.rise.dto.RegisterDTO;
import br.com.fiap.rise.exception.DataConflictException;
import br.com.fiap.rise.model.Auth;
import br.com.fiap.rise.model.User;
import br.com.fiap.rise.repository.AuthRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
    }

    public UserDetails loadUserById(UUID userId) throws UsernameNotFoundException {
        return authRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }

    public String authenticate(AuthDTO dto) {
        Auth auth = (Auth) loadUserByUsername(dto.getEmail());

        if (!passwordEncoder.matches(dto.getPassword(), auth.getPassword())) {
            throw new BadCredentialsException("Email ou senha inválidos.");
        }

        return jwtService.generateToken(auth.getId());
    }

    public void register(RegisterDTO dto, User user) {
        if (authRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new DataConflictException("E-mail já cadastrado.");
        }

        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        Auth newCredential = new Auth();
        newCredential.setEmail(dto.getEmail());
        newCredential.setPassword(encryptedPassword);
        newCredential.setUser(user);

        authRepository.save(newCredential);
    }
}
