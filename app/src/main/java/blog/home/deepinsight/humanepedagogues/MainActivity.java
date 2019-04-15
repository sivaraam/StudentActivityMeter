package blog.home.deepinsight.humanepedagogues;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import blog.home.deepinsight.humanepedagogues.db.AppDatabaseViewModel;
import blog.home.deepinsight.humanepedagogues.db.Aspirant;
import blog.home.deepinsight.humanepedagogues.db.Student;

import android.view.Menu;
import android.view.MenuInflater;
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

    /*
     * Menu item callbacks
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.teacher_id:
                Toast.makeText(
                        MainActivity.this,
                        "Hello world from menu!",
                         Toast.LENGTH_SHORT
                    ).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Fragment items interaction callbacks
     */
    @Override
    public void onListFragmentInteraction(Aspirant aspirer)
    {
//        Toast.makeText(MainActivity.this, aspirer.studentId + " " + aspirer.teacherId,
//                Toast.LENGTH_SHORT).show();

        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Student ID: " + aspirer.studentId + "\nTeacher ID: " + aspirer.teacherId)
                .setTitle(aspirer.studentName);

        // Set callbacks for buttons
        builder.setPositiveButton(R.string.give_marks, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, aspirer.studentId + " " + aspirer.teacherId + " Give marks",
                                Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton(R.string.get_report, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, aspirer.studentId + " " + aspirer.teacherId + " Get Report",
                                Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onListFragmentInteraction(Student student)
    {
        Toast.makeText(MainActivity.this, student.studentId + " " + student.studentName,
                Toast.LENGTH_SHORT).show();
    }
}
