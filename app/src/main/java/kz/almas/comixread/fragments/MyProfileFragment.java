package kz.almas.comixread.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kz.almas.comixread.R;


public class MyProfileFragment extends Fragment {

    private FirebaseDatabase DB;
    private DatabaseReference users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // присваивание бд (Firebase) к переменной
        DB = FirebaseDatabase.getInstance();
        users = DB.getReference().child("users");

        // обработка нажатия на аватар
        CardView changeAvatar = view.findViewById(R.id.defaultAvatar_cardView);
        changeAvatar.setOnClickListener(new View.OnClickListener() {
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
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                Fragment editProfileFragment = new EditProfileFragment();
                loadFragment(editProfileFragment);
            }
        });

        return view;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(getContext(), "Работает", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}