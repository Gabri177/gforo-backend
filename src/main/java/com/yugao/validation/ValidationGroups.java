package com.yugao.validation;

public interface ValidationGroups {

    // 通用验证
    interface DefaultGroup {}

    interface Login extends DefaultGroup {}

    interface Register extends DefaultGroup {}

    interface Comment extends DefaultGroup {}

    interface Post extends DefaultGroup {}
}
