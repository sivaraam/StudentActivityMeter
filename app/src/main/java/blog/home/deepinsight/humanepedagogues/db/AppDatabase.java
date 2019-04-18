/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package blog.home.deepinsight.humanepedagogues.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

@Database(
        entities = {
                Student.class,
                Teacher.class,
                CategoryMap.class,
                CategoryMarks.class,
                Aspirant.class
               /*
                * We're not using the following for now.
                * IT might be needed when there are multiple
                * classes. For now our application assumes
                * there's only one class and a fixed quantity
                * of students.
                */
		// Enrollment.class
	},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract StudentDao studentModel();

    public abstract TeacherDao teacherModel();

    public abstract AspirantsDao aspirantsModel();

    public abstract CategoriesDao categoriesModel();

    public abstract CategoryMarksDao categoryMarksModel();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "student-activity-meter-table")
                    // To simplify the codelab, allow queries on the main thread.
                    // Don't do this on a real app! See PersistenceBasicSample for an example.
                    // FIXME: we would have to fix this.
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    /*
     * FIXME: It is not clear when to correctly close and re-open the database.
     * So, let's leave that for now.
     */
    /*
    public static void closeInstance() {
        INSTANCE.close();
    }
    */

    public static void destroyInstance() {
        INSTANCE = null;
    }
}