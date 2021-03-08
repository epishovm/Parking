package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс activity экрана вывода статистики нарушений правил парковки
 * @author Epishov
 */
public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // получение даты начала и окончания периода из intent
        Bundle arguments = getIntent().getExtras();
        Date dateStart = new Date(arguments.getLong("timeStart"));
        Date dateEnd = new Date(arguments.getLong("timeEnd"));

        // получение списка нарушений из базы данных за указанный период
        Database database = new Database(getApplicationContext());
        List<Parking> parkings = database.getParkings(new Date(0), new Date());

        // выполнение статистического анализа данных
        Statistics stats = new Statistics(parkings);

        // отображение статистики в текстовом поле
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String text = "Статистика нарушений парковки\r\n"
                + "за период с " + df.format(dateStart) + " по " + df.format(dateEnd) + "\r\n\r\n"
                + "1) По статьям КОАП:\r\n\r\n";
        for (Map.Entry<String, Integer> entry : stats.countByClassification.entrySet()) {
            text += "  - статья " + entry.getKey() + ": " + entry.getValue().toString() + " нарушений\r\n";
        }
        text += "\r\n2) По времени суток:\r\n\r\n";
        text += "  - день (8:00-20:00): " + String.valueOf(stats.dayParkingCount) + " нарушений\r\n";
        text += "  - ночь (20:00-8:00): " + String.valueOf(stats.nightParkingCount) + " нарушений\r\n";
        text += "\r\nВсего нарушений: " + String.valueOf(stats.totalParkingCount);
        ((TextView) findViewById(R.id.textView)).setText(text);
    }
}