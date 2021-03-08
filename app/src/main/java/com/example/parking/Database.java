package com.example.parking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Реализация операций сохранения и чтения журнала нарушений парковки
 * @author Epishov
 */
public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "database", null, 1);
    }

    /**
     * Обработчик события создания новой базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS PARKING (RecordId INTEGER PRIMARY KEY AUTOINCREMENT, ParkingDate LONG, Longitude DOUBLE, Latitude DOUBLE, CarNumber TEXT, Classification TEXT, Photo BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  }

    /**
     * Сохранение новой записи о нарушении парковки в БД
     * @param date Дата нарушения
     * @param longitude GPS-координата (долгота)
     * @param latitude GPS-координата (широта)
     * @param carNumber Номер транспортного средства
     * @param classification Номер статьи КОАП
     * @param photo Фото нарушения
     */
    public void createParking(Date date, double longitude, double latitude, String carNumber, String classification, byte[] photo) {
        ContentValues values = new ContentValues();
        values.put("ParkingDate", date.getTime());
        values.put("Longitude", longitude);
        values.put("Latitude", latitude);
        values.put("CarNumber", carNumber);
        values.put("Classification", classification);
        values.put("Photo", photo);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("PARKING", null, values);
    }

    /**
     * Изменение существующей записи о нарушении парковки из БД
     * @param recordId ID изменяемой записи в базе данных
     * @param date Дата нарушения
     * @param longitude GPS-координата (долгота)
     * @param latitude GPS-координата (широта)
     * @param carNumber Номер транспортного средства
     * @param classification Номер статьи КОАП
     * @param photo Фото нарушения
     */
    public void updateParking(int recordId, Date date, double longitude, double latitude, String carNumber, String classification, byte[] photo) {
        ContentValues values = new ContentValues();
        values.put("ParkingDate", date.getTime());
        values.put("Longitude", longitude);
        values.put("Latitude", latitude);
        values.put("CarNumber", carNumber);
        values.put("Classification", classification);
        values.put("Photo", photo);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update("PARKING", values, "RecordId=?", new String[]{String.valueOf(recordId)});
    }

    /**
     * Удаление записи о нарушении парковки из БД
     * @param recordId ID записи в базе данных
     */
    public void deleteParking(int recordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("PARKING", "RecordId=?", new String[]{String.valueOf(recordId)});
    }

    /**
     * Чтение из базы данных журнала записей о нарушении парковки
     * @param startDate Дата начала периода
     * @param endDate Дата окончания периода
     * @return Список записей о нарушении парковки
     */
    public ArrayList<Parking> getParkings(Date startDate, Date endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select RecordId, ParkingDate, Longitude, Latitude, CarNumber, Classification, Photo " +
                "from PARKING " +
                "where ParkingDate>=? and ParkingDate<=? " +
                "order by ParkingDate",
                new String[]{
                        String.valueOf(startDate.getTime()),
                        String.valueOf(endDate.getTime())
                });
        ArrayList<Parking> result = new ArrayList<Parking>();
        while (cursor.moveToNext()) {
            Parking item = new Parking();
            item.recordId = cursor.getInt(0);
            item.date = new Date(cursor.getLong(1));
            item.longitude = cursor.getInt(2);
            item.latitude = cursor.getInt(3);
            item.carNumber = cursor.getString(4);
            item.classification = cursor.getString(5);
            item.photo = cursor.getBlob(6);
            result.add(item);
        }
        cursor.close();
        return result;
    }
}
