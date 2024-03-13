package fr.iut.projet_mobile_s4_01_powerhome.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementFragment;
import fr.iut.projet_mobile_s4_01_powerhome.app.residence.ResidenceFragment;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getIntExtra("id", 0);
        }

        Log.d("MainActivity", String.valueOf(id));


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {
                    replaceFragment(new MonHabitatFragment(), id);
                    return true;
                } else if (item.getItemId() == R.id.menu_residence) {
                    replaceFragment(new ResidenceFragment(), id);
                    return true;
                }else if (item.getItemId() == R.id.menu_equipement) {
                    replaceFragment(new EquipementFragment(), id);
                    return true;
                }else if (item.getItemId() == R.id.menu_notification) {
                    replaceFragment(new NotificationFragment(), id);
                    return true;
                }else if (item.getItemId() == R.id.menu_preference) {
                    replaceFragment(new PreferenceFragment(), id);
                    return true;
                }

                return false;
            }
        });

        handleIntent(getIntent());

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra("FRAGMENT_TO_LOAD")) {
            String fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD");
            switch (fragmentToLoad) {
                case "MonHabitatFragment":
                    bottomNavigationView.setSelectedItemId(R.id.menu_home);
                    break;
                case "ResidenceFragment":
                    bottomNavigationView.setSelectedItemId(R.id.menu_residence);
                    break;
                case "EquipementFragment":
                    bottomNavigationView.setSelectedItemId(R.id.menu_equipement);
                    break;
                case "NotificationFragment":
                    bottomNavigationView.setSelectedItemId(R.id.menu_notification);
                    break;
                case "PreferenceFragment":
                    bottomNavigationView.setSelectedItemId(R.id.menu_preference);
                    break;
            }
        }
    }

    public void replaceFragment(Fragment fragment, int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Vérifiez si le fragment est déjà dans la pile
        Fragment existingFragment = fragmentManager.findFragmentById(R.id.frame_layout);
        if (existingFragment != null) {
            if (existingFragment.getClass().equals(fragment.getClass())) {
                Log.d("MainActivity", "Existing Fragment: " + existingFragment.getClass().getSimpleName());
                return;
            } else {
                // Supprimez le fragment actuel de la pile
                fragmentTransaction.remove(existingFragment);
            }
        }

        // Ajoutez le nouveau fragment à la pile
        fragment.setArguments(createBundleWithId(id));
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commitAllowingStateLoss();

    }
    public void replaceFragment(Fragment fragment, int id, int itemId) {
        replaceFragment(fragment, id);

        Log.d("MainActivity", String.valueOf(itemId));
        Log.d("MainActivity", String.valueOf(R.id.menu_equipement));

        getSupportFragmentManager().executePendingTransactions();

        bottomNavigationView.setSelectedItemId(itemId);

    }

    private Bundle createBundleWithId(int idToPass) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", idToPass);
        return bundle;
    }
}
