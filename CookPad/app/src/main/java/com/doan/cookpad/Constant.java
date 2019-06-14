package com.doan.cookpad;

// Class này chứa các thuộc tính con, có thể được gọi trực tiếp từ các class khác, thường dùng làm các Key của Intent,...
public class Constant {
//    public static final String CREATE_TABLE_Account = "CREATE TABLE IF NOT EXISTS Account(ID VARCHAR(100), NAME VARCHAR(100),EMAIL VARCHAR(100),PICTURE VARCHAR(100),POSITION VARCHAR(100),FEELINGS VARCHAR(100))";
//    public static final String DELETE_TABLE_Account = "DELETE FROM Account";
//    public static final String SELECT_TABLE_Account = "SELECT * FROM Account";
//    public static String INSERT_TABLE_Account(String ID,String NAME,String EMAIL,String PICTURE,String POSITION,String FEELINGS){
//        String Query = "INSERT INTO Account VALUES('" + ID + "','" + NAME + "','" + EMAIL + "','" + PICTURE + "','" + POSITION + "','" + FEELINGS + "')";
//        return Query;
//    }
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_PICTURE = "KEY_PICTURE";
//    public static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    public static final String KEY_IMAGE = "KEY_IMAGE";
    public static final String KEY_LIST_IMAGE = "KEY_LIST_IMAGE";

    // Đặt ID ADMIN trước. Sau đó tính năng nào cần admin sửa thì kiểm tra ID user hiện tại với ID ADMIN này, nếu trùng thì mở tính năng đó
    public static final String ADMIN_ID = "2809444975946366";
}
