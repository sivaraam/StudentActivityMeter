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
public interface CategoriesDao {
    @Query("select * from categorymap")
    LiveData<List<CategoryMap>> loadAllCategories();

    @Query("select * from categorymap where teacher_id = :id")
    LiveData<List<CategoryMap>> loadCategoriesByTeacherId(String id);

    @Insert(onConflict = FAIL)
    void insertCategory(CategoryMap category);

    @Delete
    void deleteCategories(CategoryMap category);

    @Insert(onConflict = FAIL)
    void insertOrReplaceCategories(CategoryMap... categories);

    @Delete
    void deleteCategories(CategoryMap category1, CategoryMap category2);

    @Query("DELETE FROM categorymap")
    void deleteAll();
}