package blog.home.deepinsight.humanepedagogues.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;

import java.util.Date;

/*
 * Class representing the table consisting of
 * Enrollment details i.e., which student is enrolled
 * for which class.
 *
 * NOTE: We're not using this class for now as we
 * only have one class. We would need this when there
 * are multiple classes.
 */
@Entity(foreignKeys = {
        @ForeignKey(entity = Student.class,
                parentColumns = "student_id",
                childColumns = "student_id")
        })
public class Enrollment {
    // Fields can be public or private with getters and setters.
    @NonNull
    @ColumnInfo(name="class_id")
    public Integer classroomId;

    @NonNull
    @ColumnInfo(name="student_id")
    public String studentId;

    private String[] primaryKeys = {
            "class_id",
            "student_id"
    };

    public String[] primaryKeys () {
        return primaryKeys;
    }
}