package blog.home.deepinsight.humanepedagogues;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import blog.home.deepinsight.humanepedagogues.db.AppDatabaseViewModel;
import blog.home.deepinsight.humanepedagogues.db.Aspirant;
import blog.home.deepinsight.humanepedagogues.db.Student;

import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements StudentListFragment.OnListFragmentInteractionListener, AspirantsFragment.OnListFragmentInteractionListener {
    private AppDatabaseViewModel viewModel;

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
         * Get the view model and create and initialise the database
         * with test data.
         */
        viewModel = ViewModelProviders.of(this).get(AppDatabaseViewModel.class);
        viewModel.createDb();

        /*
         * By default load the Student list fragment.
         */
        loadFragment(new StudentListFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public AppDatabaseViewModel getViewModel() { return viewModel; }
    @Override
    public void onListFragmentInteraction(Aspirant aspirer)
    {
        Toast.makeText(MainActivity.this, aspirer.studentId + " " + aspirer.teacherId,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListFragmentInteraction(Student student)
    {
        Toast.makeText(MainActivity.this, student.studentId + " " + student.studentName,
                Toast.LENGTH_SHORT).show();
    }
}
