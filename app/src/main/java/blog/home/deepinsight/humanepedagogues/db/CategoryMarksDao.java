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
public interface CategoryMarksDao {
    @Query("select * from categorymarks")
    LiveData<List<CategoryMarks>> loadAllCategoryMarks();

    //            "INNER JOIN student ON categorymarks.student_id = student.student_id" +

    @Query("select student_id, categorymap.teacher_id, categorymarks.category_id, categorymap.categoryName as categoryName, marks, date_mark_given from categorymarks" +
            " INNER JOIN categorymap ON categorymarks.category_id = categorymap.category_id" +
            " AND categorymarks.student_id = :studentId" +
            " AND categorymarks.teacher_id = :teacherId")
    LiveData<List<CategoryMarks>> loadCategoryMarks(String teacherId, Integer studentId);

    @Query("select student_id as student_id , teacher_id as teacher_id, SUM(marks) as studentScore from categorymarks" +
            " WHERE teacher_id = :teacherId" +
            " GROUP BY student_id")
    LiveData<List<Aspirant>> loadCumulativeMarksByTeacherId(String teacherId);

    @Query("select SUM(marks) from categorymarks where student_id = :id")
    Integer findTotalMarksByStudentId(Integer id);

    @Insert(onConflict = FAIL)
    void insertCategoryMark(CategoryMarks categoryMark);

    @Delete
    void deleteCategoryMark(CategoryMarks categoryMark);

    @Insert(onConflict = FAIL)
    void insertOrReplaceCategoryMarks(CategoryMarks... categoryMarks);

    @Delete
    void deleteCategoryMarks(CategoryMarks categoryMark1, CategoryMarks categoryMark2);

    @Query("DELETE FROM categorymap")
    void deleteAll();
}