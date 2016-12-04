package util.annotation;

import java.lang.annotation.*;

/**
 *
 * 用java的注解机制,自定义Excel使用的注解
 *
 */
// 注解范围   类上注解
@Target({ElementType.TYPE})
// 注解加载时机  运行时加载
@Retention(RetentionPolicy.RUNTIME)
// 是否生成注解文档
@Documented
public @interface ExcelHeader {

	String headerName() ;		// === 导出文件头部标题
}
