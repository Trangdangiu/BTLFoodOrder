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
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT UNIQUE, password TEXT, role TEXT)";
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
                "FOREIGN KEY (user_id) REFERENCES " + TABLE_USER + "(id))";
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
                "FOREIGN KEY (user_id) REFERENCES " + TABLE_USER + "(id))";
        db.execSQL(CREATE_ORDERS_TABLE);

        // Tạo bảng chi tiết đơn hàng
        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + TABLE_ORDER_ITEMS +
                "(order_item_id INTEGER PRIMARY KEY AUTOINCREMENT, order_id INTEGER, food_id INTEGER, quantity INTEGER, " +
                "FOREIGN KEY (order_id) REFERENCES " + TABLE_ORDERS + "(order_id), " +
                "FOREIGN KEY (food_id) REFERENCES " + TABLE_FOOD + "(id))";
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

        // Sử dụng đúng tên bảng
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1; // Trả về true nếu thêm thành công
    }

    // Đăng nhập người dùng
    public String loginUser(String email, String password) {
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

        // Thêm vào cơ sở dữ liệu
        db.insert("cart_items", null, values);
        db.close();
        return 0;
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

    // Thêm vào Create_database.java

    public List<CartItem> getAllCartItems(int cartId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("cart_items", null, "cart_id = ?", new String[]{String.valueOf(cartId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int cartItemId = cursor.getInt(cursor.getColumnIndex("cart_item_id"));
                @SuppressLint("Range") int foodId = cursor.getInt(cursor.getColumnIndex("food_id"));
                @SuppressLint("Range") String foodName = cursor.getString(cursor.getColumnIndex("food_name"));
                @SuppressLint("Range") double foodPrice = cursor.getDouble(cursor.getColumnIndex("food_price"));
                @SuppressLint("Range") String foodImage = cursor.getString(cursor.getColumnIndex("food_image"));
                @SuppressLint("Range") int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                CartItem item = new CartItem(cartItemId, cartId, foodId, foodName, foodPrice, foodImage, quantity);
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return cartItems;
    }





}







