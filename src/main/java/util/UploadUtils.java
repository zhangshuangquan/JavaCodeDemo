package util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by zsq on 16/11/25.
 */
public class UploadUtils {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy" + File.separator + "MM" + File.separator + "dd" + File.separator + "HH");

    public static File createFile(String folder,String surffix) {
        String fileName = UUIDUtils.getUUID() + surffix;
        File file = new File(buildFilePath(folder,fileName));
        File parent = file.getParentFile();
        if (file.exists()) {
            return createFile(folder,surffix);
        } else {
            if (!parent.exists()){
                parent.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
    }

    public static String buildFilePath(String folder,String fileName) {
        String md5 = Utils.md5(fileName);
        StringBuffer filePath = new StringBuffer(100);
        filePath.append(folder);
        filePath.append(File.separator);
        filePath.append(md5.substring(0,2));
        filePath.append(File.separator);
        filePath.append(md5.substring(2,4));
        filePath.append(File.separator);
        filePath.append(md5.substring(4,6));
        filePath.append(File.separator);
        filePath.append(md5.substring(6,8));
        filePath.append(File.separator);
        filePath.append(fileName);
        return filePath.toString();
    }

    public static String getFileSurffixByFileName(String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            int start = fileName.lastIndexOf(".");
            return fileName.substring(start);
        } else {
            return ".jpg";
        }
    }

    public static String getFileNameWithoutSurffix(String fileName){
        if (StringUtils.isNotBlank(fileName)) {
            int end = fileName.lastIndexOf(".");
            return fileName.substring(0,end);
        } else {
            return null;
        }
    }

    public static String getSmallImageName(String fileName){
        String surffix = getFileSurffixByFileName(fileName);
        String fileNameWithoutSurffix = getFileNameWithoutSurffix(fileName);
        return fileNameWithoutSurffix + ".small" + surffix;
    }
}
