package base.final_code;

/**
 * Created by zsq on 2017/2/17.
 */
public class TestFinal2 {

    public static void main(String[] args) {
        String country = "china";
        String city = "hangzhou";

        String userInfo = country + city;    //编译后此处依然是country 和 city 变量
        String user = "china" + "hangzhou";  //编译后此处直接变成chinahangzhou

        //String userInfo = country + city;   //如果对country和city 变量加上final修饰后, 编译后此处直接是chinahangzhou
    }
}
