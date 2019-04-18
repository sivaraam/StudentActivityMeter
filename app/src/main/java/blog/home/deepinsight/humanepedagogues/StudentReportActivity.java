package blog.home.deepinsight.humanepedagogues;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import blog.home.deepinsight.humanepedagogues.db.AppDatabaseViewModel;
import blog.home.deepinsight.humanepedagogues.db.CategoryMarks;

public class StudentReportActivity extends AppCompatActivity {
    private String teacherId;
    private Integer studentId;
    AppDatabaseViewModel viewModel;
    private List<CategoryMarks> studentMarks;

    public void initTable(){
        TableLayout markTable = findViewById(R.id.student_report_table);
        int currentRow = 0;

        TableRow header_row = new TableRow(this);
        TableRow.LayoutParams headerLp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        headerLp.setMargins(30, 20, 30, 0);

        TextView headerCategoryName = new TextView(this);
        TextView headerCategoryMark = new TextView(this);
        TextView headerDate = new TextView(this);

        headerCategoryName.setText("Category");
        headerCategoryMark.setText("Mark");
        headerDate.setText("Date");

        header_row.addView(headerCategoryName);
        header_row.addView(headerCategoryMark);
        header_row.addView(headerDate);

        markTable.addView(header_row, currentRow++);

        for (CategoryMarks studentMark : studentMarks) {
            SimpleDateFormat spf = new SimpleDateFormat("dd MMM yyyy");
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(30, 20, 50, 0);
            row.setLayoutParams(lp);

            TextView categoryName = new TextView(this);
            TextView categoryMark = new TextView(this);
            TextView date = new TextView(this);

            categoryName.setText(studentMark.categoryName);
            categoryMark.setText(studentMark.marks.toString());

            date.setText(spf.format(studentMark.dateMarkGiven));

            row.addView(categoryName);
            row.addView(categoryMark);
            row.addView(date);

            markTable.addView(row, currentRow++);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Bundle receiveBundle = this.getIntent().getExtras();

        teacherId = receiveBundle.getString("teacher-id");
        studentId = receiveBundle.getInt("student-id");

        viewModel = MainActivity.instance.getViewModel();
        viewModel.getCategoryMarks(teacherId, studentId).observe(
                this,
                this::setCategoryMarks
        );
    }

    public void setCategoryMarks(List<CategoryMarks> marks) {
        this.studentMarks = marks;
        initTable();
    }
}
