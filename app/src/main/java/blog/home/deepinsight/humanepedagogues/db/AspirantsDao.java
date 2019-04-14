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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.FAIL;

@Dao
public interface AspirantsDao {
    @Query("select * from aspirant")
    LiveData<List<Aspirant>> loadAllAspirants();

    @Query("select aspirant.student_id, aspirant.teacher_id, student.studentName as studentName From aspirant " +
            "INNER JOIN student ON aspirant.student_id = student.student_id")
    LiveData<List<Aspirant>> loadAllAspirantsWithName();

    @Query("select * from Aspirant where teacher_id = :id")
    LiveData<List<Aspirant>> loadAspirantsByTeacherId(String id);

    @Insert(onConflict = FAIL)
    void insertAspirants(Aspirant user);

    @Delete
    void deleteAspirants(Aspirant user);

    @Insert(onConflict = FAIL)
    void insertOrReplaceAspirants(Aspirant... users);

    @Delete
    void deleteAspirants(Aspirant user1, Aspirant user2);

    @Query("DELETE FROM Aspirant")
    void deleteAll();
}