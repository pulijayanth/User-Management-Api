package com.codegnan.app.usermanagement.service;

import com.codegnan.app.usermanagement.dto.ChangePasswordRequest;
import com.codegnan.app.usermanagement.dto.CreateUserRequest;
import com.codegnan.app.usermanagement.dto.UpdateUserRequest;
import com.codegnan.app.usermanagement.dto.UserResponse;
import com.codegnan.app.usermanagement.entity.User;
import com.codegnan.app.usermanagement.exception.DuplicateUsernameException;
import com.codegnan.app.usermanagement.exception.UserNotFoundException;
import com.codegnan.app.usermanagement.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse create(CreateUserRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new DuplicateUsernameException(request.username());
        }
        User user = new User(request.firstName(), request.lastName(), request.username(),
                passwordEncoder.encode(request.password()));
        return UserResponse.from(repository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return repository.findAll(Sort.by("id").ascending()).stream()
                .map(UserResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return UserResponse.from(getUser(id));
    }

    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return UserResponse.from(user);
    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = getUser(id);
        if (repository.existsByUsernameAndIdNot(request.username(), id)) {
            throw new DuplicateUsernameException(request.username());
        }
        user.updateProfile(request.firstName(), request.lastName(), request.username());
        return UserResponse.from(repository.save(user));
    }

    public UserResponse updateByUsername(String username, UpdateUserRequest request) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (repository.existsByUsernameAndIdNot(request.username(), user.getId())) {
            throw new DuplicateUsernameException(request.username());
        }
        user.updateProfile(request.firstName(), request.lastName(), request.username());
        return UserResponse.from(repository.save(user));
    }

    public void resetPasswordForDemo(String username, String newPassword) {
        repository.findByUsername(username).ifPresent(user -> {
            user.changePassword(passwordEncoder.encode(newPassword));
            repository.save(user);
        });
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
        user.changePassword(passwordEncoder.encode(request.newPassword()));
        repository.save(user);
    }

    public void delete(Long id) {
        repository.delete(getUser(id));
    }

    private User getUser(Long id) {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
