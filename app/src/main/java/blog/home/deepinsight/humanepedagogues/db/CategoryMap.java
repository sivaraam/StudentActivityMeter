package blog.home.deepinsight.humanepedagogues.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

/*
 * Class representing the table consisting of a list of
 * categories created by each teacher. The list of
 * categories are the basis using which each teacher rates
 * students.
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Teacher.class,
                parentColumns = "teacher_id",
                childColumns = "teacher_id")
        },
        primaryKeys = {
                "teacher_id",
                "category_id"
        },
        indices = {
            @Index(value = {"category_id", "teacher_id"})
        })
public class CategoryMap {
    @NonNull
    @ColumnInfo(name="teacher_id")
    public String teacherId;

    /*
     * The string id created by normalizing the
     * category name. For now we just lower case
     * it.
     */
    @NonNull
    @ColumnInfo(name="category_id")
    public String categoryId;

    /*
     * The name of the category as entered by the
     * user (teacher).
     */
    @NonNull
    public String categoryName;
}
