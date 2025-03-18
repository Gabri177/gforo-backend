package com.yugao.service;

import com.yugao.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {


    public User getUserById(int Id);

    public User getUserByName(String username);

    public User getUserByEmail(String email);

    @Transactional
    public boolean  addUser(User user);

    @Transactional
    public boolean  updateStatus(int id, int status);

    @Transactional
    public boolean  updateHeader(int id, String headerUrl);

    @Transactional
    public boolean  updatePassword(int id, String password);
}
