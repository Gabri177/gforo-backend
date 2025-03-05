package com.yugao.service;

import com.yugao.domain.User;

import java.util.List;

public interface UserService {


    public User getUserById(int Id);
    public User getUserByName(String username);
    public User getUserByEmail(String email);
    public boolean  addUser(User user);
    public boolean  updateStatus(int id, int status);
    public boolean  updateHeader(int id, String headerUrl);
    public boolean  updatePassword(int id, String password);
}
