/**
 * 本包用于定义系统中所有用户相关的请求数据传输对象（DTO）。
 * <p>
 * UserDTO 通常用于：
 * - 用户注册、登录时，接收前端传来的参数；
 * - 仅携带与业务相关的数据，不包含数据库字段或敏感信息（如 salt）；
 * <p>
 * 注意：
 * - DTO 与 Domain 对象（如 User）相互独立；
 * - DTO ↔ Domain 的转换应通过 {@code converter} 包中的转换类完成；
 * - 为避免敏感字段外泄，DTO 只保留必要数据字段。
 * <p>
 * 默认所有方法参数和返回值不可为 null。
 * 若需为 null，请显式使用 @Nullable 标注。
 *
 * @author yugao
 */
@NonNullApi
package com.yugao.dto;

import org.springframework.lang.NonNullApi;
