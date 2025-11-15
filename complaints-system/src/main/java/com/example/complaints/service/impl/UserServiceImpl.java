package com.example.complaints.service.impl;

import com.example.complaints.dao.UserDao;
import com.example.complaints.dto.UpdateUserRequest;
import com.example.complaints.dto.UserResponse;
import com.example.complaints.entity.User;
import com.example.complaints.exception.ResourceNotFoundException;
import com.example.complaints.service.UserService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final CacheManager cacheManager;

    public UserServiceImpl(UserDao userDao, CacheManager cacheManager) {
        this.userDao = userDao;
        this.cacheManager = cacheManager;
    }

    @Override
    @Cacheable(cacheNames = "userById", key = "#id")
    public UserResponse findById(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponse(user);
    }

    @Override
    @Cacheable(cacheNames = "userByEmail", key = "#email")
    public UserResponse findByEmail(String email) {
        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.touchUpdatedAt();

        User saved = userDao.save(user);
        evictCaches(saved);
        return toResponse(saved);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userDao.delete(user);
        evictCaches(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet())
        );
    }

    private void evictCaches(User user) {
        if (cacheManager.getCache("userById") != null) {
            cacheManager.getCache("userById").evict(user.getId());
        }
        if (cacheManager.getCache("userByEmail") != null) {
            cacheManager.getCache("userByEmail").evict(user.getEmail());
        }
    }
}

