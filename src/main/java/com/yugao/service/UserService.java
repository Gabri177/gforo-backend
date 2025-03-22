package com.yugao.service;

import com.yugao.domain.User;

public interface UserService {


    User getUserById(Long Id);

    User getUserByName(String username);

    User getUserByEmail(String email);

    boolean  addUser(User user);

    boolean  updateStatus(Long id, int status);

    boolean  updateHeader(Long id, String headerUrl);

    boolean  updatePassword(Long id, String password);
}
