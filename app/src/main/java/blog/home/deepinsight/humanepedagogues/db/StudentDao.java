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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.FAIL;
@Dao
public interface StudentDao {
    @Query("select * from student")
    LiveData<List<Student>> loadAllStudents();

    @Query("select * from student where student_id = :id")
    Student loadStudentById(int id);

    @Insert(onConflict = FAIL)
    void insertStudent(Student user);

    @Delete
    void deleteStudent(Student user);

    @Query("delete from student where studentName like :badName")
    int deleteStudentsByName(String badName);

    @Insert(onConflict = FAIL)
    void insertOrReplaceStudents(Student... users);

    @Delete
    void deleteStudents(Student user1, Student user2);

    @Query("DELETE FROM student")
    void deleteAll();
}