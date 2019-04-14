package blog.home.deepinsight.humanepedagogues.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/*
 * Class representing the table consisting of a list of
 * Teacher entries.
 */
@Entity(indices = {
            @Index(value = {"teacher_id"})
        })
public class Teacher {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="teacher_id")
    public String teacherId;

    @NonNull
    public String teacherName;

    @NonNull
    public String subject;

    /*
     * Note: To be used when there are multiple classes.
     * We currently only have one class. So, we're not
     * using this.
     */
    @Ignore
    public Integer classroomId;
}
