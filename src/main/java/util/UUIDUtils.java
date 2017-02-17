package util;

import org.apache.commons.lang3.StringUtils;

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

    public static void main(String[] args) {

        System.out.print(Utils.md5(UUIDUtils.getUUID()));

        String str = "分拣成品维修库";
        System.out.println(str.contains("维修库"));


        String a = "27a49b69-f12A幢单元室z";
        System.out.println(toUpperCase(a));

    }

    public static String toUpperCase(String str) {
        if(StringUtils.isEmpty(str)) {
            return str;
        } else {
            char[] buffer = str.toCharArray();

            for(int i = 0; i < buffer.length; ++i) {
                char ch = buffer[i];
                if(Character.isUpperCase(ch)) {
                    continue;
                } else if(Character.isTitleCase(ch)) {
                    buffer[i] = Character.toLowerCase(ch);
                } else if(Character.isLowerCase(ch)) {
                    buffer[i] = Character.toUpperCase(ch);
                }
            }
            return new String(buffer);
        }
    }
}
