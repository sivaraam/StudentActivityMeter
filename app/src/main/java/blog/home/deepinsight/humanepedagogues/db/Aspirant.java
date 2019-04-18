package blog.home.deepinsight.humanepedagogues.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

/*
 * Class representing the table consisting of a list of
 * categories created by each teacher. The list of
 * categories are the basis using which each teacher rates
 * students.
 */
@Entity(
//        foreignKeys = {
//        @ForeignKey(entity = Teacher.class,
//                parentColumns = "teacher_id",
//                childColumns = "teacher_id"),
//
//        @ForeignKey(entity = Student.class,
//                parentColumns = "student_id",
//                childColumns = "student_id")
//        },

        primaryKeys = {
                "teacher_id",
                "student_id"
        },

        indices = {
                @Index(value = {"student_id", "teacher_id"})
        })
public class Aspirant {
    @NonNull
    @ColumnInfo(name="teacher_id")
    public String teacherId;

    @NonNull
    @ColumnInfo(name="student_id")
    public Integer studentId;

    public String studentName;

    public Integer studentScore;

    @Ignore
    public Integer finalScore;
}
