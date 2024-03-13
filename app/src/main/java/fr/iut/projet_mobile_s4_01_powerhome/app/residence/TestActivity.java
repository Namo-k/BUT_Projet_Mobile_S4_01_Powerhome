package fr.iut.projet_mobile_s4_01_powerhome.app.residence;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import fr.iut.projet_mobile_s4_01_powerhome.R;
import fr.iut.projet_mobile_s4_01_powerhome.app.CreneauFragment;
import fr.iut.projet_mobile_s4_01_powerhome.app.MonHabitatFragment;
import fr.iut.projet_mobile_s4_01_powerhome.app.NotificationFragment;
import fr.iut.projet_mobile_s4_01_powerhome.app.PreferenceFragment;
import fr.iut.projet_mobile_s4_01_powerhome.app.equipement.EquipementFragment;

public class TestActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ActionBarDrawerToggle toggle;
    FragmentManager fm;
    FrameLayout frameLayout;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = findViewById(R.id.frame_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.close, R.string.open );
        drawerLayout.addDrawerListener(toggle);

        fm = getSupportFragmentManager();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    fm.beginTransaction()
                            .replace(R.id.frame_layout, new MonHabitatFragment())
                            .commit();
                } else if (itemId == R.id.menu_residence) {
                    fm.beginTransaction()
                            .replace(R.id.frame_layout, new ResidenceFragment())
                            .commit();
                }
                else if (itemId == R.id.menu_equipement) {
                    fm.beginTransaction()
                            .replace(R.id.frame_layout, new EquipementFragment())
                            .commit();
                }
                else if (itemId == R.id.menu_creneau) {
                    fm.beginTransaction()
                            .replace(R.id.frame_layout, new CreneauFragment())
                            .commit();
                }
                else if (itemId == R.id.menu_notification) {
                    fm.beginTransaction()
                            .replace(R.id.frame_layout, new NotificationFragment())
                            .commit();
                }
                else if (itemId == R.id.menu_preference) {
                    fm.beginTransaction()
                            .replace(R.id.frame_layout, new PreferenceFragment())
                            .commit();
                }
                else if (itemId == R.id.menu_profile) {
                    fm.beginTransaction()
                            .replace(R.id.frame_layout, new ResidenceFragment())
                            .commit();
                }else if (itemId == R.id.menu_deconnexion) {
                    fm.beginTransaction()
                            .replace(R.id.frame_layout, new ResidenceFragment())
                            .commit();
                }


                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.getMenu().performIdentifierAction(R.id.menu_preference, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

}
