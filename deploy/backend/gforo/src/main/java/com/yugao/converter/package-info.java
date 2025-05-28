/**
 * 本包用于封装 DTO、Domain 和 VO 之间的数据转换逻辑。
 * <p>
 * 转换类命名规范为：XxxConverter，例如 {@link com.yugao.converter.UserConverter}。
 * <p>
 * 设计目的：
 * - 解耦 DTO ↔ Entity ↔ VO 之间的转换逻辑；
 * - 避免将数据映射逻辑耦合进 service 或 controller；
 * - 提升代码的可维护性、复用性、清晰度；
 * - 保证对外输出对象（VO）不包含敏感字段；
 * - 保证输入对象（DTO）只包含业务需要的字段。
 * <p>
 * 转换方法通常为静态方法，不涉及状态，适合在 controller/service 层直接调用。
 * <p>
 * 默认本包中所有方法参数与返回值不可为 null，
 * 若允许为 null，请显式使用 {@code @Nullable} 标注。
 *
 * @author yugao
 */
@NonNullApi
package com.yugao.converter;

import org.springframework.lang.NonNullApi;
