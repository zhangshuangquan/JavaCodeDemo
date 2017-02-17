package base.static_code;

/**
 * Created by zsq on 2017/2/17.
 */
public class TestStatic2 {

    static int a = 3;   //执行步骤 第一步
    static int b;

    // 第四步
    static void method(int x) {
        System.out.println("this is fourth ");
        System.out.println("x= "+x);
        System.out.println("a= "+a);
        System.out.println("b= "+b);
    }

    // 第二步
    static {
        System.out.println("this is second ");
        System.out.println("static block initialized... ");
        b = a * 4;
    }

    //第三步
    public static void main(String[] args) {
        System.out.println("this is third ");
        method(42);
    }
}
