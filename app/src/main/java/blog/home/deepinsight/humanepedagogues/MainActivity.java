package blog.home.deepinsight.humanepedagogues;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import blog.home.deepinsight.humanepedagogues.dummy.DummyContent;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements StudentListFragment.OnListFragmentInteractionListener, AspirantsFragment.OnListFragmentInteractionListener {

    private TextView mTextMessage;

    /*
     * Taken from a blog titled: Bottom Navigation Android Example using Fragments
     *
     * https://www.simplifiedcoding.net/bottom-navigation-android-example/
     *
     */
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new StudentListFragment();
                    break;
                case R.id.navigation_aspirants:
                    fragment = new AspirantsFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * By default load the Student list fragment.
         */
        loadFragment(new StudentListFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item)
    {
        Toast.makeText(MainActivity.this, (String)item.details,
                Toast.LENGTH_SHORT).show();
    }

}
