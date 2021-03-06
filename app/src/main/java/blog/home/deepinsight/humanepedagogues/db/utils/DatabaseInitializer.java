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

package blog.home.deepinsight.humanepedagogues.db.utils;

import android.os.AsyncTask;
import androidx.annotation.NonNull;

import blog.home.deepinsight.humanepedagogues.db.AppDatabase;
import blog.home.deepinsight.humanepedagogues.db.Student;
import blog.home.deepinsight.humanepedagogues.db.Teacher;

public class DatabaseInitializer {
    public static void populateAsync(final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static void addTeacher(final AppDatabase db, final String id, final String name, final String subject) {
        Teacher teacher = new Teacher();
        teacher.teacherId = id;
        teacher.teacherName = name;
        teacher.subject = subject;
        db.teacherModel().insertTeacher(teacher);
    }

    private static void addStudent(final AppDatabase db, final Integer id, final String name) {
        Student student = new Student();
        student.studentId = id;
        student.studentName = name;
        db.studentModel().insertStudent(student);
    }

    private static void populateWithTestData(AppDatabase db) {
        db.aspirantsModel().deleteAll();
        db.studentModel().deleteAll();
        db.teacherModel().deleteAll();

        addStudent(db, 1, "Aswin");
        addStudent(db, 2, "Anushya");
        addStudent(db, 3, "Baskar");
        addStudent(db, 4, "Bhuvaneshwari");
        addStudent(db, 5, "Gayathri");
        addStudent(db, 6, "Kanishka");
        addStudent(db, 7, "Manideepan");
        addStudent(db, 8, "Rithik");
        addStudent(db, 9, "Sundar");
        addStudent(db, 10, "Suthi");

        addTeacher(db, "ram_sci", "Ram Praveen", "Science");
        addTeacher(db, "amru_math", "Amrutha", "Maths");
        addTeacher(db, "dhiyana_eng", "Dhiyana Priya", "English");

//
//        try {
//            Date today = getTodayPlusDays(0);
//            Date yesterday = getTodayPlusDays(-1);
//            Date twoDaysAgo = getTodayPlusDays(-2);
//            Date lastWeek = getTodayPlusDays(-7);
//            Date twoWeeksAgo = getTodayPlusDays(-14);
//
//            addLoan(db, "1", user1, book1, twoWeeksAgo, lastWeek);
//
//            addLoan(db, "2", user2, book1, lastWeek, yesterday);
//
//            addLoan(db, "3", user2, book2, lastWeek, today);
//
//            addLoan(db, "4", user2, book3, lastWeek, twoDaysAgo);
//
//            addLoan(db, "5", user2, book4, lastWeek, today);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

//    private static Date getTodayPlusDays(int daysAgo) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, daysAgo);
//        return calendar.getTime();
//    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
