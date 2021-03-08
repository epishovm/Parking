package com.example.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс activity экрана просмотра / редактирования записи о нарушении правил парковки
 * @author Epishov
 */
public class ParkingActivity extends AppCompatActivity implements OnMapReadyCallback {

    int recordId;
    double latitude;
    double longitude;
    byte[] photo;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        // получение данных из intent
        Bundle arguments = getIntent().getExtras();
        recordId = arguments.getInt("recordId", -1);
        latitude = arguments.getDouble("latitude");
        longitude = arguments.getDouble("longitude");
        date = new Date(arguments.getLong("date", new Date().getTime()));
        photo = arguments.getByteArray("photo");
        String classify = arguments.getString("classify");
        String carNumber = arguments.getString("carNumber");

        // отображение данных в полях ввода
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        ((TextView) findViewById(R.id.txbDate)).setText(df.format(date));
        ((TextView) findViewById(R.id.txbCarNumber)).setText(carNumber);
        ((TextView) findViewById(R.id.txbClassification)).setText(classify);
        findViewById(R.id.btnDelete).setVisibility(recordId != -1 ? View.VISIBLE : View.INVISIBLE);

        // отображение фото нарушения в ImageView
        Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageBitmap(bmp);

        // инициализация отображения карты Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Обработчик события готовности к отображению карты Google Maps
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng marker = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(marker));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 16));
    }

    /**
     * Сохранение данных по нажатию кнопки "Сохранить"
     */
    public void btnSaveClick(View view) {
        String carNumber = ((EditText) findViewById(R.id.txbCarNumber)).getText().toString().trim();
        String classify = ((EditText) findViewById(R.id.txbClassification)).getText().toString().trim();
        if (classify.equals("")) {
            findViewById(R.id.txbClassification).requestFocus();
            return;
        }
        if (carNumber.equals("")) {
            findViewById(R.id.txbCarNumber).requestFocus();
            return;
        }
        Database db = new Database(this);
        if (recordId == -1) {
            db.createParking(date, longitude, latitude, carNumber, classify, photo);
        } else {
            db.updateParking(recordId, date, longitude, latitude, carNumber, classify, photo);
        }
        finish();
    }

    /**
     * Удаление записи о нарушении по нажатию кнопки "Удалить"
     */
    public void btnDeleteClick(View view) {
        Database db = new Database(this);
        db.deleteParking(recordId);
        finish();
    }
}