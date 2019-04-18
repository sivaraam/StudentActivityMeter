package blog.home.deepinsight.humanepedagogues;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import blog.home.deepinsight.humanepedagogues.db.AppDatabaseViewModel;
import blog.home.deepinsight.humanepedagogues.db.Aspirant;
import blog.home.deepinsight.humanepedagogues.db.CategoryMap;
import blog.home.deepinsight.humanepedagogues.db.CategoryMarks;
import blog.home.deepinsight.humanepedagogues.db.Student;
import blog.home.deepinsight.humanepedagogues.db.Teacher;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StudentListFragment.OnListFragmentInteractionListener, AspirantsFragment.OnListFragmentInteractionListener {
    private AppDatabaseViewModel viewModel;
    private List<Teacher> teachersList;
    private List<CategoryMap> categoryList;
    Teacher currentTeacher;
    String currentTeacherId;
    private Menu menu;

    private final String defaultTeacherId = "ram_sci";
    private final Integer minAspirantScore = 50;

    static MainActivity instance;

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

    private void updateTeacher(String teacherId) {
        /*
         * Remove observer for old teacher
         */
        viewModel.getCategoriesByTeacherId(currentTeacherId).removeObservers(
                MainActivity.instance
        );

        currentTeacherId = teacherId;

        /*
         * Observe for categories for the new teacher
         */
        viewModel.getTeacherById(currentTeacherId).observe(
                MainActivity.instance,
                MainActivity.instance::setTeacher
        );

        viewModel.getCategoriesByTeacherId(currentTeacherId).observe(
                MainActivity.instance,
                MainActivity.instance::setCategories
        );

        viewModel.getCumulativeMarksByTeacherId(currentTeacherId).observe(
                MainActivity.instance,
                MainActivity.instance::updateAspirants
        );
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            final int columnCount = 1;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = StudentListFragment.newInstance(columnCount);
                    break;
                case R.id.navigation_aspirants:
                    fragment = AspirantsFragment.newInstance(columnCount, currentTeacherId);
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        /*
         * Get the view model and create and initialise the database
         * with test data.
         */
        viewModel = ViewModelProviders.of(this).get(AppDatabaseViewModel.class);
        viewModel.createDb();
        viewModel.getTeachers().observe(this, this::setTeachers);

        updateTeacher(defaultTeacherId);

        /*
         * By default load the Student list fragment.
         */
        loadFragment(new StudentListFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public AppDatabaseViewModel getViewModel() { return viewModel; }

    void checkAndAddCategory(String categoryName) {
        String categoryNameEntered = categoryName.trim();
        String categoryId = categoryNameEntered.toLowerCase();
        CategoryMap newCategory = new CategoryMap();
        boolean found_category = false;

        for (CategoryMap category : categoryList) {
            if (categoryId.equals(category.categoryId)) {
                Toast.makeText(MainActivity.this, R.string.category_exists,
                        Toast.LENGTH_SHORT).show();
                found_category = true;
                showCategoryAddDialog();
            }
        }

        if (!found_category) {
            if (categoryName.isEmpty()) {
                Toast.makeText(MainActivity.this, "Bad category name",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                newCategory.teacherId = currentTeacherId;
                newCategory.categoryId = categoryId;
                newCategory.categoryName = categoryNameEntered;

                viewModel.insertCategory(newCategory);

                Toast.makeText(MainActivity.this, "Successfully added category: " + newCategory.categoryId,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    void showCategoryAddDialog() {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        View addCategoryView = inflater.inflate(R.layout.dialog_add_category, null);
        final EditText categoryName = addCategoryView.findViewById(R.id.add_category);

        // 2. Set the layout to a custom layout
        builder.setView(addCategoryView);

        builder.setTitle(R.string.enter_category_name);

        // 3. Set callbacks for buttons
        builder.setPositiveButton(R.string.insert_add_another, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                checkAndAddCategory(categoryName.getText().toString());
                showCategoryAddDialog();
            }
        });

        builder.setNeutralButton(R.string.finish_category_add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                checkAndAddCategory(categoryName.getText().toString());
            }
        });

        // 4. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /*
     * Function to invoke a dialog to add categories if there are no
     * categories for the current teacher.
     */
    public void checkCategoriesAndRequestAddition() {
        if (categoryList != null) {
            if (categoryList.size() == 0) {
                /*
                 * This teacher hasn't added any categories yet. So request
                 * them to add some.
                 */
                showCategoryAddDialog();
            }
        }
    }

    /*
     * Callbacks for database observer changes
     */
    public void setTeachers(List<Teacher> teachers) {
        teachersList = teachers;
    }

    public void setTeacher(Teacher teacher) {
        if (teacher != null) {
            currentTeacher = teacher;
        }

        if (MainActivity.instance.menu != null) {
            MenuItem teacherName = MainActivity.instance.menu.findItem(R.id.teacher_name);
            if (currentTeacher != null)
                teacherName.setTitle(currentTeacher.teacherName);
        }
    }

    public void setCategories(List<CategoryMap> categories) {
        categoryList = categories;
        checkCategoriesAndRequestAddition();
    }

    public void updateAspirants(List<Aspirant> typicalAspirants) {
        for (Aspirant aspirant : typicalAspirants) {
            if (aspirant.studentScore == null) {
                continue;
            }

            if (aspirant.studentScore >= minAspirantScore) {
                viewModel.insertAspirant(aspirant);
            }
        }
    }

    /*
     * Menu item callbacks
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem teacherName = menu.findItem(R.id.teacher_name);

        if (currentTeacher != null)
            teacherName.setTitle(currentTeacher.teacherName);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.teacher_id:
                // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();
                View teacherIdView = inflater.inflate(R.layout.dialog_teacher_id, null);
                final EditText teacherID = teacherIdView.findViewById(R.id.teacher_userid);

                // 2. Set the layout to a custom layout
                builder.setView(teacherIdView);

                builder.setTitle(R.string.enter_teacher_id);

                // 3. Set callbacks for buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String teacherIdEntered = teacherID.getText().toString().trim();

                        if (teachersList == null) {
                            Toast.makeText(MainActivity.this, R.string.teacher_list_not_loaded,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (Teacher teacher : teachersList) {
                                if (teacherIdEntered.equals(teacher.teacherId)) {
                                    updateTeacher(teacherIdEntered);

                                    String idChangedMsg = getApplicationContext().getString(R.string.teacher_id_changed, currentTeacherId);
                                    Toast.makeText(MainActivity.this, idChangedMsg,
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            Toast.makeText(MainActivity.this, R.string.invalid_teacher_id,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*
                         * Nothing to do here
                         */
                        dialog.dismiss();
                    }
                });

                // 4. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
                AlertDialog dialog = builder.create();

                dialog.show();
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
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(
                    "Student ID: " +
                    aspirer.studentId +
                    "\nTotal Marks: " +
                     aspirer.finalScore
                )
                .setTitle(aspirer.studentName);

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onListFragmentInteraction(Student student)
    {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(
                "Student ID: " +
                student.studentId
                )
                .setTitle(student.studentName);

        // 3. Set callbacks for buttons
        builder.setPositiveButton(R.string.give_marks, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog.Builder categoryDialog = new AlertDialog.Builder(MainActivity.instance);
                categoryDialog.setTitle("Select Category to Add Mark");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.instance, android.R.layout.select_dialog_item);
                for (CategoryMap category : categoryList) {
                    arrayAdapter.add(category.categoryName);
                }

                categoryDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                categoryDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String categoryName = arrayAdapter.getItem(which);

                        LayoutInflater inflater = MainActivity.instance.getLayoutInflater();
                        AlertDialog.Builder marksDialog = new AlertDialog.Builder(MainActivity.instance);
                        View addCategoryView = inflater.inflate(R.layout.dialog_add_marks, null);
                        final EditText marksField = addCategoryView.findViewById(R.id.add_marks);

                        marksDialog.setView(addCategoryView);
                        marksDialog.setTitle("Enter marks for category " + categoryName);

                        marksDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*
                                 * We know the ID is lower case of the name.
                                 * So, we use it directly.
                                 */
                                String categoryId = categoryName.toLowerCase();

                                CategoryMarks mark = new CategoryMarks();
                                mark.teacherId = currentTeacherId;
                                mark.studentId = student.studentId;
                                mark.categoryId = categoryId;
                                mark.marks = Integer.parseInt(marksField.getText().toString());
                                mark.dateMarkGiven = new Date(System.currentTimeMillis());

                                viewModel.insertCategoryMark(mark);

                                Toast.makeText(MainActivity.this, "Successfully awarded " + mark.marks + " marks",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        marksDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });

                        marksDialog.show();
                    }
                });

                categoryDialog.show();
            }
        });

        builder.setNeutralButton(R.string.get_report, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Bundle studentReportBundle = new Bundle();
                studentReportBundle.putString("teacher-id", currentTeacherId);
                studentReportBundle.putInt("student-id", student.studentId);

                Intent i = new Intent(MainActivity.instance, StudentReportActivity.class);
                i.putExtras(studentReportBundle);
                startActivity(i);
            }
        });

        // 4. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
