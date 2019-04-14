package blog.home.deepinsight.humanepedagogues.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/*
 * Class representing the table consisting of
 * Enrollment details i.e., which student is enrolled
 * for which class.
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Teacher.class,
                parentColumns = "teacher_id",
                childColumns = "teacher_id"),

        @ForeignKey(entity = Student.class,
                parentColumns = "student_id",
                childColumns = "student_id")
// FIXME: The category_id should actually be a foreign key
// but we aren't specifying it as one as we get an error
// about the category_id not being in the index:
//
// category_id column references a foreign key but it is not part of an index. This may trigger full table scans whenever parent table is modified so you are highly advised to create an index that covers this column.
//
// We ignore it for now. May be using a class for it
// would help?
//
//        @ForeignKey(entity = CategoryMap.class,
//                parentColumns = "category_id",
//                childColumns = "category_id"),

        },

        primaryKeys = {
            "teacher_id",
            "student_id",
            "category_id",
            "date_mark_given"
        },

        indices = {
            @Index(value = {"student_id", "category_id", "teacher_id"})
        })
@TypeConverters(DateConverter.class)
public class CategoryMarks {
    @NonNull
    @ColumnInfo(name="teacher_id")
    public String teacherId;

    @NonNull
    @ColumnInfo(name="student_id")
    public Integer studentId;

    @NonNull
    @ColumnInfo(name="category_id")
    public String categoryId;

    @NonNull
    public Integer marks;

    @NonNull
    @ColumnInfo(name="date_mark_given")
    public Date dateMarkGiven;
}
