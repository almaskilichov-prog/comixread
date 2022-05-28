package kz.almas.comixread.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kz.almas.comixread.R;
import kz.almas.comixread.fragments.AboutFragment;
import kz.almas.comixread.fragments.CatalogFragment;
import kz.almas.comixread.fragments.HomeFragment;
import kz.almas.comixread.fragments.MyProfileFragment;
import kz.almas.comixread.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
    // Инициализация переменных
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // #5865d6
        // #3c415e
        // Присваивание переменных
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Добавление пунктом меню (items)
        bottomNavigation.add(new MeowBottomNavigation.Model(0, R.drawable.ic_profile));
        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_catalog));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_search));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_about));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // Инициализация фрагментов
                Fragment fragment = null;
                // Проверка условий
                switch (item.getId()) {
                    case 0:
                        // Инициализация фрагмента страницы профиля
                        fragment = new MyProfileFragment();
                        break;
                    case 1:
                        // Инициализация фрагмента главной страницы
                        fragment = new HomeFragment();
                        break;
                    case 2:
                        // Инициализация фрагмента страницы каталога
                        fragment = new CatalogFragment();
                        break;
                    case 3:
                        // Инициализация фрагмента страницы поиска
                        fragment = new SearchFragment();
                        break;
                    case 4:
                        // Инициализация фрагмена страницы о нас
                        fragment = new AboutFragment();
                        break;
                }
                // Загрузка фрагмента
                loadFragment(fragment);
            }
        });

        bottomNavigation.show(1, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(getApplicationContext(), "You clicked " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
//                Toast.makeText(getApplicationContext(), "You reselected " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}