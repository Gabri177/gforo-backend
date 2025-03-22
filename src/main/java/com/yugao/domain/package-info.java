/**
 * 本包用于定义系统核心的领域实体类（Domain / Entity），
 * 实体类与数据库中的数据表一一对应，用于 ORM 映射。
 * <p>
 * 说明：
 * - 实体类通常对应数据表结构，字段命名与数据库字段一致；
 * - 使用 MyBatis-Plus 注解进行主键、自增、字段名映射等配置；
 * - 实体类中包含数据库所需的完整字段（如密码、salt、状态等）；
 * - 不建议直接在 controller 层返回实体类，以避免敏感字段泄露；
 * - 对外返回应使用 VO，对内业务传输建议使用 DTO；
 * <p>
 * 示例：
 * {@link com.yugao.domain.User} 表示用户实体，映射至数据库中的 user 表。
 * <p>
 * 默认本包中所有方法参数和返回值不可为 null，
 * 若允许为 null，请显式使用 {@code @Nullable} 标注。
 *
 * @author yugao
 */
@NonNullApi
package com.yugao.domain;

import org.springframework.lang.NonNullApi;
