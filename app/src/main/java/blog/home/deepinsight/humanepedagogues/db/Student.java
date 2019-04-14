package blog.home.deepinsight.humanepedagogues.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/*
 * Class representing the table consisting of a list of
 * Student entries.
 */
@Entity(indices = {
            @Index(value = {"student_id"})
        })
public class Student {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="student_id")
    public Integer studentId;

    @NonNull
    public String studentName;
}
