package com.example.parking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

/**
 * Класс activity экрана журнала нарушений парковки
 * @author Epishov
 */
public class JournalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        ListView listView = (ListView) findViewById(R.id.parkingListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Parking parking = (Parking) listView.getItemAtPosition(position);
                showParkingActivity(parking);
            }
        });

        refreshData();
    }

    void refreshData(){
        Database database = new Database(getApplicationContext());
        List<Parking> results = database.getParkings(new Date(0), new Date());
        ListView listView = (ListView) findViewById(R.id.parkingListView);
        ArrayAdapter<Parking> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);
    }

    void showParkingActivity(Parking parking) {
        Intent intent = new Intent(this, ParkingActivity.class);
        intent.putExtra("recordId", parking.recordId);
        intent.putExtra("latitude", parking.latitude);
        intent.putExtra("longitude", parking.longitude);
        intent.putExtra("carNumber", parking.carNumber);
        intent.putExtra("classify", parking.classification);
        intent.putExtra("date", parking.date.getTime());
        intent.putExtra("photo", parking.photo);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshData();
    }
}