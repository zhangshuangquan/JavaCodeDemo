package util.annotation;

/**
 * Created by zsq on 16/12/4.
 */

@ExcelHeader(headerName = "用户表")
public class ExcelAnnotationEntityTest {

    @ExcelColumn(columnName = "用户ID")
    private Integer userId;

    @ExcelColumn(columnName = "用户名")
    private String userName;

    @ExcelColumn(columnName = "电话")
    private String phone;

    @ExcelColumn(columnName = "地址")
    private String address;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
