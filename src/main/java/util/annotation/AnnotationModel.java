package util.annotation;

import java.lang.annotation.*;

/**
 * Created by zsq on 16/12/4.
 */

/**
 * 注解范围  类上注解
 */
@Target(ElementType.TYPE)

/**
 *  注解加载时机
 *  RetentionPolicy.SOURCE 注释将被编译器丢弃。
 *  RetentionPolicy.RUNTIME 注释将由编译器记录在类文件中在运行时由VM保留，因此它们可以被反射读取。
 *  RetentionPolicy.CLASS  注释由编译器记录在类文件,但不需要在运行时由VM保留。 这是默认值行为。
 */
@Retention(RetentionPolicy.RUNTIME)

/**
 * 是否生成注解文档
 */
@Documented
public @interface AnnotationModel {

    String name = "model";

    String name();

    String type();
}
