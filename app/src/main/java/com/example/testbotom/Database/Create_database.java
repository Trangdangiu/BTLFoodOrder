package com.example.testbotom.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class Create_database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "users";
    private static final String TABLE_FOOD = "food";
    private static final String TABLE_FEEDBACK = "feedback";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_CART_ITEMS = "cart_items";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_ITEMS = "order_items";

    public Create_database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng người dùng
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USER +
                " (user_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT UNIQUE, password TEXT, role TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Tạo bảng món ăn
        String CREATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, price INTEGER, image TEXT, description TEXT)";
        db.execSQL(CREATE_FOOD_TABLE);

        // Tạo bảng phản hồi
        String CREATE_FEEDBACK_TABLE = "CREATE TABLE " + TABLE_FEEDBACK +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, hoTen TEXT, sdt TEXT, email TEXT, binhLuan TEXT, " +
                "FOREIGN KEY (email) REFERENCES " + TABLE_USER + "(email) ON DELETE CASCADE)";
        db.execSQL(CREATE_FEEDBACK_TABLE);

        // Tạo bảng giỏ hàng
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART +
                "(cart_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES " + TABLE_USER + "(user_id))";
        db.execSQL(CREATE_CART_TABLE);


        // Tạo bảng chi tiết giỏ hàng
        String CREATE_CART_ITEMS_TABLE = "CREATE TABLE " + TABLE_CART_ITEMS +
                "(cart_item_id INTEGER PRIMARY KEY AUTOINCREMENT, cart_id INTEGER, food_id INTEGER, quantity INTEGER, " +
                "food_name TEXT, food_price REAL, food_image TEXT, " + // Thêm các trường mới
                "FOREIGN KEY (cart_id) REFERENCES " + TABLE_CART + "(cart_id), " +
                "FOREIGN KEY (food_id) REFERENCES " + TABLE_FOOD + "(id))";
        db.execSQL(CREATE_CART_ITEMS_TABLE);





        // Tạo bảng đơn hàng
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS +
                "(order_id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, full_name TEXT, phone_number TEXT, " +
                "address TEXT, payment_method TEXT, order_date DATETIME DEFAULT CURRENT_TIMESTAMP, total_amount REAL, " +
                "FOREIGN KEY (user_id) REFERENCES " + TABLE_USER + "(user_id))";
        db.execSQL(CREATE_ORDERS_TABLE);

        // Tạo bảng chi tiết đơn hàng
//        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + TABLE_ORDER_ITEMS +
//                "(order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, order_code TEXT, full_name TEXT, phone_number TEXT, " +
//                "address TEXT, menu TEXT, order_date DATETIME, total_amount REAL, payment_method TEXT,isDelivery INTEGER DEFAULT 0, " +
//                "FOREIGN KEY (order_code) REFERENCES " + TABLE_ORDERS + "(order_id))";
//        db.execSQL(CREATE_ORDER_ITEMS_TABLE);

        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + TABLE_ORDER_ITEMS +
                "(order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, order_code TEXT, full_name TEXT, phone_number TEXT, " +
                "address TEXT, menu TEXT, order_date DATETIME, total_amount REAL, payment_method TEXT, isDelivery INTEGER DEFAULT 0, " +
                "user_id INTEGER, " +  // Thêm trường user_id
                "FOREIGN KEY (order_code) REFERENCES " + TABLE_ORDERS + "(order_id), " +  // Khóa ngoại với bảng orders
                "FOREIGN KEY (user_id) REFERENCES " + TABLE_USER + "(user_id))"; // Khóa ngoại với bảng users
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        onCreate(db);
    }

    // Đăng kí người dùng
    public boolean registerUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", username);
        values.put("password", password);
        values.put("role", role); // Lưu loại người dùng

        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1; // Trả về true nếu thêm thành công
    }

    // Đăng nhập người dùng
    public String loginUserCheckPass(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{"role"}, "email=? AND password=?",
                new String[]{email, password}, null, null, null);
        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(0); // Lấy loại người dùng
        }
        cursor.close();
        db.close();
        return role; // Trả về vai trò người dùng (Admin hoặc User)
    }


    // Get Role
    public String getRoleByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{"role"}, "email=?",
                new String[]{email}, null, null, null);
        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(0); // Lấy loại người dùng
        }
        cursor.close();
        db.close();
        return role; // Trả về vai trò người dùng (Admin hoặc User)
    }
    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{"email, password"}, "email=? AND password = ?",
                new String[]{email, password}, null, null, null);
        boolean success = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return success; // Trả về vai trò người dùng (Admin hoặc User)
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE email=?", new String[]{email});
        User user = null;

        // Kiểm tra nếu Cursor không rỗng và di chuyển thành công đến hàng đầu tiên
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();

            int idIndex = cursor.getColumnIndex("user_id");
            int emailIndex = cursor.getColumnIndex("email");
            int roleIndex = cursor.getColumnIndex("role");
            int passwordIndex = cursor.getColumnIndex("password");

            if (idIndex != -1) user.setId(cursor.getInt(idIndex));
            if (emailIndex != -1) user.setEmail(cursor.getString(emailIndex));
            if (roleIndex != -1) user.setRole(cursor.getString(roleIndex));
            if(passwordIndex != -1) user.setPassword(cursor.getString(passwordIndex));
        }

        if (cursor != null) {
            cursor.close(); // Đóng Cursor nếu nó đã được khởi tạo
        }
        return user; // Trả về đối tượng User hoặc null nếu không tìm thấy
    }


    @SuppressLint("Range")
    public Integer getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Integer userId = null; // Khởi tạo biến userId

        try {
            // Thực hiện câu truy vấn để lấy ID người dùng dựa trên email
            cursor = db.rawQuery("SELECT user_id FROM " + TABLE_USER + " WHERE email=?", new String[]{email});

            // Kiểm tra xem cursor có dữ liệu không
            if (cursor != null && cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndex("user_id")); // Lấy ID từ cột
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        } finally {
            if (cursor != null) {
                cursor.close(); // Đảm bảo đóng cursor để tránh rò rỉ bộ nhớ
            }
        }

        return userId; // Trả về ID hoặc null nếu không tìm thấy
    }




    // thêm món ăn vào bang
    public void insertFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put("id", food.getId());
        values.put("name", food.getName());
        values.put("price", food.getPrice());
        values.put("image", food.getImage());
        values.put("description", food.getDescription());
        long result = db.insert(TABLE_FOOD, null, values);
        db.close();

    }

    // thêm feedback vào bảng
    public boolean insertFeedback(String hoTen, String sdt, String email, String binhLuan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hoTen", hoTen);
        values.put("sdt", sdt);
        values.put("email", email);  // Khóa ngoại
        values.put("binhLuan", binhLuan);

        long result = db.insert("feedback", null, values);
        db.close();
        return result != -1; // Trả về true nếu thêm thành công
    }

    // Lấy danh sách phản hồi dựa trên email của người dùng
    public Cursor getFeedbackByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM feedback WHERE email = ?", new String[]{email});
    }

    // Xóa phản hồi dựa trên id
    public boolean deleteFeedback(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("feedback", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public List<Feedback> getAllFeedback() {
        List<Feedback> feedbackList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn tất cả dữ liệu từ bảng feedback
        Cursor cursor = db.rawQuery("SELECT * FROM feedback", null);

        if (cursor.moveToFirst()) {
            do {
                // Lấy từng giá trị của các trường trong bảng
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String hoTen = cursor.getString(cursor.getColumnIndex("hoTen"));
                @SuppressLint("Range") String sdt = cursor.getString(cursor.getColumnIndex("sdt"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String binhLuan = cursor.getString(cursor.getColumnIndex("binhLuan"));

                // Tạo đối tượng Feedback và thêm vào danh sách
                Feedback feedback = new Feedback(id, hoTen, sdt, email, binhLuan);
                feedbackList.add(feedback);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return feedbackList; // Trả về danh sách các phản hồi
    }

    // lấy danh sách từ bảng food
    public Cursor getAllFoods() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FOOD, null);
    }

    // xóa món ăn trong bảng
    public boolean deleteFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("food", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // cập nhật món ăn
    public boolean updateFood(int id, String name, int price, String description, String img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("description", description);
        values.put("image", img);
        int result = db.update("food", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;

    }


    public int getNextFoodId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(id) FROM " + TABLE_FOOD, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(0) + 1;
        }
        cursor.close();
        db.close();
        return 1; // Nếu không có dữ liệu, trả về ID đầu tiên
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int result = db.update(TABLE_USER, values, "email = ?", new String[]{email});
        db.close();
        return result > 0; // Trả về true nếu cập nhật thành công
    }

    // Tạo giỏ hàng mới cho người dùng
    public long createCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);  // Liên kết với người dùng
        long cartId = db.insert("cart", null, values);  // Tạo giỏ hàng mới
        db.close();
        return cartId;
    }
//
//    public int getCartIdByUserId(int userId) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query("Cart", new String[]{"id"}, "userId = ?", new String[]{String.valueOf(userId)}, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            @SuppressLint("Range") int cartId = cursor.getInt(cursor.getColumnIndex("id"));
//            cursor.close();
//            return cartId;
//        }
//
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        return -1; // Nếu không tìm thấy giỏ hàng
//    }

    // Thêm mục vào giỏ hàng
    public long addCartItem(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cart_id", cartItem.getCartId());
        values.put("food_id", cartItem.getFoodId());
        values.put("food_name", cartItem.getFoodName());
        values.put("food_price", cartItem.getFoodPrice());
        values.put("food_image", cartItem.getFoodImage());
        values.put("quantity", cartItem.getQuantity());
        db.insert("cart_items", null, values);
        db.close();
        return 0;
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart_items");
        db.close();
    }

    // Cập nhật số lượng của một mục trong giỏ hàng
    public boolean updateCartItem(int cartItemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        int result = db.update("cart_items", values, "cart_item_id = ?", new String[]{String.valueOf(cartItemId)});
        db.close();
        return result > 0;  // Trả về true nếu cập nhật thành công
    }


    @SuppressLint("Range")
    public int getCartIdByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{"cart_id"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, null);
        int cartId = -1; // Giá trị mặc định nếu không tìm thấy giỏ hàng
        if (cursor != null && cursor.moveToFirst()) {
            cartId = cursor.getInt(cursor.getColumnIndex("cart_id"));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return cartId; // Trả về cart_id hoặc -1 nếu không tìm thấy
    }
    public Integer getCartIdByUserId(Integer userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT cart_id FROM " + TABLE_CART + " WHERE user_id = ?", new String[]{userId.toString()});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int cartId = cursor.getInt(0);
                cursor.close();
                return cartId;
            }
            cursor.close();
        }
        return null;
    }


    // Thêm vào Create_database.java

    // Lấy tất cả các mục trong giỏ hàng dựa trên cartId
    public List<CartItem> getAllCartItems(int cartId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn tất cả các mục trong giỏ hàng với cartId nhất định
        Cursor cursor = db.query("cart_items", null, "cart_id = ?", new String[]{String.valueOf(cartId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int cartItemId = cursor.getInt(cursor.getColumnIndex("cart_item_id"));
                @SuppressLint("Range") int foodId = cursor.getInt(cursor.getColumnIndex("food_id"));
                @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("food_name"));
                @SuppressLint("Range") double foodPrice = cursor.getDouble(cursor.getColumnIndex("food_price"));
                @SuppressLint("Range") String foodImage = cursor.getString(cursor.getColumnIndex("food_image"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                // Khởi tạo đối tượng CartItem
                CartItem item = new CartItem(cartItemId, cartId, foodId, foodName, foodPrice, foodImage, quantity);
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return cartItems; // Trả về danh sách các mục trong giỏ hàng
    }


    public void deleteCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART_ITEMS, "cart_item_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
// update khi thay doi sluong item_food_cart
    public void updateCartItemQuantity(int cartItemId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);

        db.update(TABLE_CART_ITEMS, values, "cart_item_id = ?", new String[]{String.valueOf(cartItemId)});
        db.close(); // Closing the database connection
    }


    public long addOrderItem(OrderItem orderItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_code", orderItem.getOrderCode());
        values.put("full_name", orderItem.getFullName());
        values.put("phone_number", orderItem.getPhoneNumber());
        values.put("address", orderItem.getAddress());
        values.put("menu", orderItem.getMenu());
        values.put("order_date", orderItem.getOrderDate());
        values.put("total_amount", orderItem.getTotalAmount());
        values.put("payment_method", orderItem.getPaymentMethod());
        values.put("user_id", orderItem.getUserId());

        // Chèn hàng vào bảng
        long result = db.insert(TABLE_ORDER_ITEMS, null, values);
        db.close();
        return result;
    }



    public List<OrderItem> getAllOrderItems(int user_id) {
        List<OrderItem> orderItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDER_ITEMS + " WHERE user_id = ?", new String[]{String.valueOf(user_id)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") OrderItem orderItem = new OrderItem(
                        cursor.getInt(cursor.getColumnIndex("order_item_id")),  // Lấy order_item_id
                        cursor.getString(cursor.getColumnIndex("order_code")),  // Lấy order_code
                        cursor.getString(cursor.getColumnIndex("full_name")),   // Lấy full_name
                        cursor.getString(cursor.getColumnIndex("phone_number")),// Lấy phone_number
                        cursor.getString(cursor.getColumnIndex("address")),     // Lấy address
                        cursor.getString(cursor.getColumnIndex("menu")),        // Lấy menu
                        cursor.getString(cursor.getColumnIndex("order_date")),  // Lấy order_date
                        cursor.getDouble(cursor.getColumnIndex("total_amount")),// Lấy total_amount
                        cursor.getString(cursor.getColumnIndex("payment_method")),// Lấy payment_method
                        cursor.getInt(cursor.getColumnIndex("isDelivery")) == 1,
                        cursor.getInt(cursor.getColumnIndex("user_id"))
                        // Chuyển đổi isDelivery thành boolean
                );
                orderItemList.add(orderItem);  // Thêm đối tượng vào danh sách
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderItemList;
    }
    public List<OrderItem> getAllOrderItemsAdmin() {
        List<OrderItem> orderItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn lấy tất cả dữ liệu từ bảng order_item
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ORDER_ITEMS, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") OrderItem orderItem = new OrderItem(
                        cursor.getInt(cursor.getColumnIndex("order_item_id")),  // Lấy order_item_id
                        cursor.getString(cursor.getColumnIndex("order_code")),  // Lấy order_code
                        cursor.getString(cursor.getColumnIndex("full_name")),   // Lấy full_name
                        cursor.getString(cursor.getColumnIndex("phone_number")),// Lấy phone_number
                        cursor.getString(cursor.getColumnIndex("address")),     // Lấy address
                        cursor.getString(cursor.getColumnIndex("menu")),        // Lấy menu
                        cursor.getString(cursor.getColumnIndex("order_date")),  // Lấy order_date
                        cursor.getDouble(cursor.getColumnIndex("total_amount")),// Lấy total_amount
                        cursor.getString(cursor.getColumnIndex("payment_method")),// Lấy payment_method
                        cursor.getInt(cursor.getColumnIndex("isDelivery")) == 1, // Chuyển đổi isDelivery thành boolean
                        cursor.getInt(cursor.getColumnIndex("user_id")) // Lấy user_id
                );
                orderItemList.add(orderItem);  // Thêm đối tượng vào danh sách
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderItemList;
    }


    //load theo ngay
 public List<OrderItem> getOrderItemsByDate(String startDate, String endDate) {
     List<OrderItem> orderItemList = new ArrayList<>();
     SQLiteDatabase db = this.getReadableDatabase();

     // Chuyển đổi định dạng ngày từ "dd/MM/yyyy" sang "yyyy-MM-dd"
     String[] startDateParts = startDate.split("/");
     String[] endDateParts = endDate.split("/");

     // Tạo lại định dạng ngày theo chuẩn yyyy-MM-dd
     String formattedStartDate = startDateParts[2] + "-" + startDateParts[1] + "-" + startDateParts[0];
     String formattedEndDate = endDateParts[2] + "-" + endDateParts[1] + "-" + endDateParts[0];

     // Giả sử định dạng ngày là "yyyy-MM-dd", bạn cần định dạng lại để phù hợp với CSDL
     String query = "SELECT * FROM " + TABLE_ORDER_ITEMS + " WHERE order_date BETWEEN ? AND ?";
     Cursor cursor = db.rawQuery(query, new String[]{formattedStartDate, formattedEndDate});

     if (cursor.moveToFirst()) {
         do {
             @SuppressLint("Range") OrderItem orderItem = new OrderItem(
                     cursor.getInt(cursor.getColumnIndex("order_item_id")),
                     cursor.getString(cursor.getColumnIndex("order_code")),
                     cursor.getString(cursor.getColumnIndex("full_name")),
                     cursor.getString(cursor.getColumnIndex("phone_number")),
                     cursor.getString(cursor.getColumnIndex("address")),
                     cursor.getString(cursor.getColumnIndex("menu")),
                     cursor.getString(cursor.getColumnIndex("order_date")),
                     cursor.getDouble(cursor.getColumnIndex("total_amount")),
                     cursor.getString(cursor.getColumnIndex("payment_method")),
                     cursor.getInt(cursor.getColumnIndex("isDelivery")) == 1,
                     cursor.getInt(cursor.getColumnIndex("user_id"))
             );
             orderItemList.add(orderItem);
         } while (cursor.moveToNext());
     }

     cursor.close();
     db.close();
     return orderItemList;
 }


    public void updateOrderDeliveryStatus(int orderId, boolean isDelivery) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isDelivery", isDelivery ? 1 : 0);  // Chuyển boolean thành 1 hoặc 0

        db.update(TABLE_ORDER_ITEMS, values, "order_item_id = ?", new String[]{String.valueOf(orderId)});
        db.close();
    }

    public boolean isAdmin(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT role FROM users WHERE id = ?", new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String role = cursor.getString(cursor.getColumnIndex("role"));
            cursor.close();
            return "admin".equals(role); // Trả về true nếu vai trò là admin
        }

        if (cursor != null) {
            cursor.close();
        }
        return false; // Trả về false nếu không phải admin hoặc không tìm thấy người dùng
    }



    //     Check user exist
    public boolean checkUser(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{"email"}, "email=?", new String[]{email}, null, null, null);
        boolean userExist = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return userExist;
    }

    public boolean updatePasswordByEmail(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);

        int rowAffected = db.update(TABLE_USER, values, "email = ?", new String[]{email});

        return rowAffected > 0;
    }

}







