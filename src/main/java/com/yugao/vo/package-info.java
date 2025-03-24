/**
 * 本包用于定义系统中对外返回的数据视图对象（VO，View Object）。
 * <p>
 * VO 的设计目标是：
 * - 作为 controller 层对前端返回的响应结构；
 * - 只包含安全可见的字段（如用户名、头像等）；
 * - 避免暴露敏感信息（如密码、salt、权限等）；
 * - 与实体类（domain）保持解耦，便于前后端字段隔离和演化。
 * <p>
 * 例如：
 * {@link com.yugao.vo.UserInfoVO} 仅包含用户公开信息，
 * 可由 {@code UserConverter} 将 {@code User} 实体安全转换而来。
 * <p>
 * 默认本包内所有方法参数和返回值不可为 null。
 * 若需为 null，请显式使用 {@code @Nullable} 标注。
 *
 * @author yugao
 */
@NonNullApi
package com.yugao.vo;

import org.springframework.lang.NonNullApi;
