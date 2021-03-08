package com.example.parking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс activity главного экрана
 * @author Epishov
 */
public class MainActivity extends AppCompatActivity implements LocationListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PERMISSIONS = 2;
    String currentPhotoPath;
    byte[] currentPhotoData;
    LocationManager locationManager;
    Location currentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        requestLocationUpdate();
    }

    /**
     * Обработчик события изменения текущих GPS-координат
     * @param location GPS-координаты
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = location;
    }

    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = imageFile.getAbsolutePath();
        currentPhotoData = null;
        return imageFile;
    }

    /**
     * Обработчик нажатия кнопки "Добавить нарушение парковки"
     */
    public void btnCreateNewClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File photoFile = createImageFile();
            Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (IOException ex) {
        }
    }

    /**
     * Обработчик получения результата из запущенной activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == -1) {

            // результат получения фото с камеры
            File file = new File(currentPhotoPath);
            int size = (int) file.length();
            byte[] imageBytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(imageBytes, 0, imageBytes.length);
                buf.close();
                file.delete();
                currentPhotoData = imageBytes;
                showParkingActivity();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_PERMISSIONS) {

            // результат запроса разрешения на определение местоположения
            requestLocationUpdate();
        }
    }

    /**
     * Инициирование запроса на получение текущего местоположения
     */
    void requestLocationUpdate(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    /**
     * Запуск activity экрана ввода данных о нарушении правил парковки
     */
    void showParkingActivity() {
        Intent intent = new Intent(this, ParkingActivity.class);
        intent.putExtra("photo", currentPhotoData);
        if (currentLocation != null) {
            intent.putExtra("latitude", currentLocation.getLatitude());
            intent.putExtra("longitude", currentLocation.getLongitude());
        } else {
            intent.putExtra("latitude", 0.0);
            intent.putExtra("longitude", 0.0);
        }
        startActivity(intent);
    }

    /**
     * Обработчик нажатия кнопки "Журнал нарушений"
     */
    public void btnShowJournalClick(View view) {
        Intent intent = new Intent(this, JournalActivity.class);
        startActivity(intent);
    }

    /**
     * Обработчик нажатия кнопки "Статистика нарушений"
     */
    public void btnShowStatisticsClick(View view) {
        Intent intent = new Intent(this, StatisticsPeriodActivity.class);
        startActivity(intent);
    }
}