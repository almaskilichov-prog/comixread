package kz.almas.comixread.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import kz.almas.comixread.R;
import kz.almas.comixread.activities.EditProfileActivity;
import kz.almas.comixread.classes.Auth;
import kz.almas.comixread.classes.DataConverter;
import kz.almas.comixread.classes.User;
import kz.almas.comixread.database.App;
import kz.almas.comixread.database.AppDatabase;
import kz.almas.comixread.database.UserInfo;
import kz.almas.comixread.database.UserInfoDao;


public class MyProfileFragment extends Fragment {

    // Firebase
    private FirebaseDatabase DB;
    private DatabaseReference users;

    // Юзернейм
    TextView usernameProfile;
    String username;

    // База данных
    AppDatabase db = App.getInstance().getDatabase();

    // Юзер
    User user;

    // Аватарка
    ImageView avatar;
    String avatarBase64;

    AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        usernameProfile = view.findViewById(R.id.nicknameProfile);
        avatar = view.findViewById(R.id.avatar);

        startLoadingDialog();


        // присваивание бд (Firebase) к переменной
        DB = FirebaseDatabase.getInstance();
        users = DB.getReference().child("users");


        // обработка нажатия на аватар
        avatar = view.findViewById(R.id.avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = new String[]{"Сделать фото", "Выбрать из галереи", "Отмена"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Выберите ваш аватар");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 0);
                        } else if (i == 1) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");

                            startActivityForResult(intent, 1);
                        }
                    }
                });

                AlertDialog alertDialog = builder.show();
                alertDialog.getWindow().setGravity(Gravity.CENTER);
            }


        });

        // обработка на нажатие кнопки "редактировать профиль"
        CardView editProfile_cardView = view.findViewById(R.id.editProfile_cardView);
        editProfile_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        updateMyProfileInfo();

//        if (db.userInfoDao().getById(1) == null) {
//            startLoadingDialog();
//            updateMyProfileInfo();
//        } else {
//            usernameProfile.setText(db.userInfoDao().getById(1).username);
//            user.setId(db.userInfoDao().getById(1).key);
//            user.setPassword(db.userInfoDao().getById(1).password);
//            if (db.userInfoDao().getById(1).avatarBase64 != null) {
//                user.setAvatarBase64(db.userInfoDao().getById(1).avatarBase64);
//                getAvatar();
//            }
//
//        }

        return view;





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 0) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                saveAvatarToFB(bitmap);
                avatar.setImageBitmap(bitmap);
            } else if (requestCode == 1) {
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    saveAvatarToFB(bitmap);
                    avatar.setImageBitmap(bitmap);
                } catch (Exception e) {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void updateMyProfileInfo() {
        Auth auth = new Auth(getActivity());
        Query query = users.orderByChild("username").equalTo(auth.getUsername());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    user = data.getValue(User.class);

//                    UserInfo userInfo = new UserInfo();

                    // проверка на аватарку
                    if (user.getAvatarBase64() != null) {
                        getAvatar();
//                        userInfo.avatarBase64 = user.getAvatarBase64();
                    }

                    usernameProfile.setText(user.getUsername());
                    dialog.dismiss();
//                    userInfo.id = 1L;
//                    userInfo.key = user.getId();
//                    userInfo.username = user.getUsername();
//                    userInfo.password = user.getPassword();


//                    db.userInfoDao().insert(userInfo);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveAvatarToFB(Bitmap bitmap) {
        byte[] byteArray = DataConverter.convertImageToByteArray(bitmap);
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        user.setAvatarBase64(encoded);
        users.child(user.getId()).child("avatarBase64").setValue(encoded);
//        db.userInfoDao().updateAvatarBase64ByIdList(Arrays.asList(1L), byteArray);
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void getAvatar() {
        byte[] decodedString = Base64.decode(user.getAvatarBase64(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        avatar.setImageBitmap(bitmap);
    }
}