package com.example.parking;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Структура данных о нарушении правил парковки
 * @author Epishov
 */
public class Parking {

    /**
     * Уникальный ID записи
     */
    public int recordId;

    /**
     * Дата нарушения
     */
    public Date date;

    /**
     * GPS-координата (долгота)
     */
    public double latitude;

    /**
     * GPS-координата (широта)
     */
    public double longitude;

    /**
     * Номер транспортного средства
     */
    public String carNumber;

    /**
     * Номер статьи КОАП
     */
    public String classification;

    /**
     * Фото нарушения в бинарном виде
     */
    public byte[] photo;

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return df.format(date) + "\r\nСтатья КОАП: " + classification + " | Номер ТС: " + carNumber;
    }
}
