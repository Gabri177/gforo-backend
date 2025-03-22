package com.yugao.service;

import com.yugao.domain.User;

public interface UserService {


    User getUserById(int Id);

    User getUserByName(String username);

    User getUserByEmail(String email);

    boolean  addUser(User user);

    boolean  updateStatus(int id, int status);

    boolean  updateHeader(int id, String headerUrl);

    boolean  updatePassword(int id, String password);
}
