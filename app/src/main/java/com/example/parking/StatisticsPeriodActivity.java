package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Класс activity для выбора отчётного периода для анализа статистики нарушений
 * @author Epishov
 */
public class StatisticsPeriodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_period);

        Calendar now = Calendar.getInstance();
        ((DatePicker) findViewById(R.id.dateEnd)).init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), null);
        now.add(Calendar.MONTH, -1);
        ((DatePicker) findViewById(R.id.dateStart)).init(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), null);
    }

    /**
     * Отображение activity вывода статистики по нажатию кнопки "Показать статистику"
     */
    public void btnShowStatisticsClick(View view) {
        DatePicker dtPickerStart = ((DatePicker) findViewById(R.id.dateStart));
        DatePicker dtPickerEnd = ((DatePicker) findViewById(R.id.dateEnd));

        Calendar calendar = Calendar.getInstance();
        calendar.set(dtPickerStart.getYear(), dtPickerStart.getMonth(), dtPickerEnd.getDayOfMonth(), 0, 0, 0);
        long timeStart = calendar.getTimeInMillis();
        calendar.set(dtPickerEnd.getYear(), dtPickerEnd.getMonth(), dtPickerEnd.getDayOfMonth(), 23, 59, 59);
        long timeEnd = calendar.getTimeInMillis();

        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("timeStart", timeStart);
        intent.putExtra("timeEnd", timeEnd);
        startActivity(intent);
    }
}