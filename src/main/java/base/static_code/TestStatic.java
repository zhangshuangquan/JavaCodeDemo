package base.static_code;

/**
 * Created by zsq on 2017/2/17.
 * 静态代码块测试
 */
public class TestStatic {

    private static int a;

    private int b;

    //静态代码块1
    static {
        TestStatic.a = 3;
        System.out.println(a+"_1");

        TestStatic testStatic = new TestStatic();
        testStatic.f();
        testStatic.b = 1000;
        System.out.println(testStatic.b);
    }

    //静态代码块2
    static {
        TestStatic.a = 4;
        System.out.println(a+"_2");
    }

    private void f() {
        System.out.println("this is not a static method");
    }

    public static void main(String[] args){

    }

    //静态代码块3
    static {
        TestStatic.a = 5;
        System.out.println(a+"_3");
    }
}
