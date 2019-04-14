package blog.home.deepinsight.humanepedagogues.db;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import blog.home.deepinsight.humanepedagogues.db.utils.DatabaseInitializer;

public class AppDatabaseViewModel extends AndroidViewModel {
    private LiveData<List<Student>> students;
    private LiveData<List<Aspirant>> aspirants;
    private AppDatabase mDb;

    public AppDatabaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Student>> getStudents() {
        return students;
    }

    public LiveData<List<Aspirant>> getAspirants() {
        return aspirants;
    }

    public void createDb() {
        mDb = AppDatabase.getInMemoryDatabase(this.getApplication());

        // Populate it with initial data
        DatabaseInitializer.populateAsync(mDb);

        students = mDb.studentModel().loadAllStudents();

        aspirants = mDb.aspirantsModel().loadAllAspirants();
    }
}
