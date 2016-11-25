package util;

import java.util.UUID;

/**
 * Created by zsq on 16/11/25.
 */
public class UUIDUtils {

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-","");
        return uuid;
    }

    public static  void main(String []a) {

        System.out.print(Utils.md5(UUIDUtils.getUUID()));
    }
}
