package com.example.parking;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Модульные тесты класса Statistics
 * @author Epishov
 */
public class StatisticsUnitTest {

    /**
     * Входные данные: пустой список нарушений
     * Ожидаемый результат: все статистические показатели равны 0
     */
    @Test
    public void emptyParkingList() {
        List<Parking> parkings = new ArrayList<Parking>();
        Statistics stats =new Statistics(parkings);
        assertEquals(0, stats.dayParkingCount);
        assertEquals(0, stats.nightParkingCount);
        assertEquals(0, stats.totalParkingCount);
        assertEquals(0, stats.countByClassification.size());
    }

    Parking createParkingForTest(int hour, int minute, String classification) {
        Parking parking = new Parking();
        Calendar cldr = Calendar.getInstance();
        cldr.set(2021, 03, 01, hour, minute);
        parking.date = new Date(cldr.getTimeInMillis());
        parking.classification = classification;
        return parking;
    }

    /**
     * Входные данные: список нарушений с разным временем суток
     * Ожидаемый результат: количество нарушений днём = 2, ночью = 3, всего = 3
     */
    @Test
    public void parkingsByTimeOfDay() {
        List<Parking> parkings = new ArrayList<Parking>();
        parkings.add(createParkingForTest(8, 0, "12.19.1"));
        parkings.add(createParkingForTest(9, 0, "12.19.1"));
        parkings.add(createParkingForTest(20, 0, "12.19.1"));
        Statistics stats =new Statistics(parkings);
        assertEquals(2, stats.dayParkingCount);
        assertEquals(1, stats.nightParkingCount);
        assertEquals(3, stats.totalParkingCount);
    }

    /**
     * Входные данные: список нарушений с разными статьями КОАП
     * Ожидаемый результат: количество нарушений по статье 12.19.1 = 2, по статье 12.19.2 = 1, по 12.19.3 = 1
     */
    @Test
    public void parkingsByClassifcation() {
        List<Parking> parkings = new ArrayList<Parking>();
        parkings.add(createParkingForTest(8, 0, "12.19.1"));
        parkings.add(createParkingForTest(9, 0, "12.19.1"));
        parkings.add(createParkingForTest(10, 0, "12.19.2"));
        parkings.add(createParkingForTest(12, 0, "12.19.3"));
        Statistics stats = new Statistics(parkings);
        assertEquals(3, stats.countByClassification.size());
        assertEquals((Integer)2, (Integer)stats.countByClassification.get("12.19.1"));
        assertEquals((Integer)1, (Integer)stats.countByClassification.get("12.19.2"));
        assertEquals((Integer)1, (Integer)stats.countByClassification.get("12.19.3"));
    }
}