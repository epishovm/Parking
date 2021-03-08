package com.example.parking;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Реализация подсчета статистики нарушений парковки
 * @author Epishov
 */
public class Statistics {

    public int totalParkingCount;
    public HashMap<String, Integer> countByClassification;
    public int nightParkingCount;
    public int dayParkingCount;

    /**
     * Конструктор
     * @param parkings Список нарушений парковки
     */
    public Statistics(List<Parking> parkings) {
        totalParkingCount=parkings.size();
        countByClassification = new HashMap<String, Integer>();
        for (int i = 0; i < parkings.size(); i++) {
            Parking parking = parkings.get(i);
            if (!countByClassification.containsKey(parking.classification)) {
                countByClassification.put(parking.classification, new Integer(1));
            } else {
                countByClassification.put(parking.classification, new Integer((Integer) countByClassification.get(parking.classification) + 1));
            }
            Calendar cldr = Calendar.getInstance();
            cldr.setTimeInMillis(parking.date.getTime());
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            if(hour>=8 && hour<20){
                dayParkingCount++;
            }else{
                nightParkingCount++;
            }
        }
    }

}
