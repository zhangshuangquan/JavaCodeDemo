package base.final_code;

/**
 * Created by zsq on 2017/2/17.
 */
public class TestFinal extends A {

    public String getName() {
        return "";
    }

    //子类重写getAddress() 编译出错, 因为父类的该方法用final修饰,故无法重写.
    /*public String getAddress() {
        return "";
    }*/

    public static void main(String[] args) {

    }
}

class A {

    /**
     * 因为是 private 修饰, 子类中不能继承该方法,子类中的getName()方法是子类中重写定义的方法,
     * 属于子类本身,编译正常
     * @return
     */
    private final String getName() {
        return "";
    }

    /**
     * public+final 修饰的方法
     * @return
     */
    public final String getAddress() {
        return "";
    }
}
