package kz.almas.comixread.classes;

import android.content.Context;
import android.content.SharedPreferences;

public class Auth {

    private Context context;
    private static String username = null;
    private static User user = null;

    public Auth(Context context) {
        this.context = context;
    }

    public String getUsername() {
        if (username == null) {
            SharedPreferences sp = context.getSharedPreferences("data", context.MODE_PRIVATE);
            username = sp.getString("username", null);
        }
        return username;
    }

    public void saveUsername(String username) {
        Auth.username = username;
        SharedPreferences sp = context.getSharedPreferences("data",
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("username", username);
        edit.apply();
    }

    public void setCurrentUser(User p) {
        user = p;
    }

    public User getCurrentUser() {
        return user;
    }

}
