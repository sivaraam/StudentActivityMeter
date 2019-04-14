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

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.FAIL;

@Dao
public interface TeacherDao {
    @Query("select * from teacher")
    List<Teacher> loadAllTeachers();

    @Query("select * from teacher where teacher_id = :id")
    Teacher loadTeacherById(String id);

    @Insert(onConflict = FAIL)
    void insertTeacher(Teacher user);

    @Delete
    void deleteTeacher(Teacher user);

    @Query("delete from teacher where teacherName like :badName")
    int deleteTeachersByName(String badName);

    @Insert(onConflict = FAIL)
    void insertOrReplaceTeachers(Teacher... users);

    @Delete
    void deleteTeachers(Teacher user1, Teacher user2);

    @Query("DELETE FROM teacher")
    void deleteAll();
}