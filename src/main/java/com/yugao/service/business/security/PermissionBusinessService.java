package com.yugao.service.business.security;

import java.util.List;

public interface PermissionBusinessService {

    List<String> getPermissionCodesByUserId(Long userId);
}
