package blog.home.deepinsight.humanepedagogues.db;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import blog.home.deepinsight.humanepedagogues.db.utils.DatabaseInitializer;

public class AppDatabaseViewModel extends AndroidViewModel {
    private LiveData<List<Student>> students;
    private LiveData<List<Teacher>> teachers;
    private AppDatabase mDb;

    public AppDatabaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Student>> getStudents() {
        return students;
    }

    public LiveData<List<Aspirant>> getAspirantsByTeacherId(String teacherId) {
        return mDb.aspirantsModel().loadAllAspirantsFullyByTeacherId(teacherId);
    }

    public LiveData<List<Teacher>> getTeachers() {
        return teachers;
    }

    public LiveData<Teacher> getTeacherById(String teacherId) {
        return mDb.teacherModel().loadTeacherById(teacherId);
    }

    public LiveData<List<CategoryMap>> getCategoriesByTeacherId(String teacherId) {
        return mDb.categoriesModel().loadCategoriesByTeacherId(teacherId);
    }

    public LiveData<List<Aspirant>> getCumulativeMarksByTeacherId(String teacherId) {
        return mDb.categoryMarksModel().loadCumulativeMarksByTeacherId(teacherId);
    }

    public LiveData<List<CategoryMarks>> getCategoryMarks(String teacherId, Integer studentId) {
        return mDb.categoryMarksModel().loadCategoryMarks(teacherId, studentId);
    }

    public void insertCategory(CategoryMap category) {
        mDb.categoriesModel().insertCategory(category);
    }

    public void insertCategoryMark(CategoryMarks categoryMark) {
        mDb.categoryMarksModel().insertCategoryMark(categoryMark);
    }

    public void insertAspirant(Aspirant aspirant) {
        mDb.aspirantsModel().insertAspirants(aspirant);
    }

    public void createDb() {
        mDb = AppDatabase.getInMemoryDatabase(this.getApplication());

        // Populate it with initial data
        DatabaseInitializer.populateAsync(mDb);

        students = mDb.studentModel().loadAllStudents();

        teachers = mDb.teacherModel().loadAllTeachers();
    }
}
