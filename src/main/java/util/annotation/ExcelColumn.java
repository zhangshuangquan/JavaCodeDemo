package util.annotation;

import java.lang.annotation.*;

//注解范围   属性注解
@Target({ElementType.FIELD})
// 注解加载时机  运行时加载
@Retention(RetentionPolicy.RUNTIME)
//是否生成注解文档
@Documented
public @interface ExcelColumn {

	String columnName() ;		// === 导出文件列名称
	String autoIncrement() default "";  //自增涨列，如果为自增涨则为Y否则为N或空
}
