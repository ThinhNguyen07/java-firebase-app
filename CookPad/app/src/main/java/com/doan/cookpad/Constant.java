package com.doan.cookpad;

// Class này chứa các thuộc tính con, có thể được gọi trực tiếp từ các class khác, thường dùng làm các Key của Intent,...
public class Constant {
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_PICTURE = "KEY_PICTURE";
//    public static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    public static final String KEY_IMAGE = "KEY_IMAGE";
    public static final String KEY_LIST_IMAGE = "KEY_LIST_IMAGE";

    // Đặt ID ADMIN trước. Sau đó tính năng nào cần admin sửa thì kiểm tra ID user hiện tại với ID ADMIN này, nếu trùng thì mở tính năng đó
    public static final String ADMIN_ID = "1216102441905541";
}