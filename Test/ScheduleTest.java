import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {

    List testList = List.of(
            new Data("TRIP_101", "1A", 1, 501, "14:15", 5),
            new Data("TRIP_102", "1A", 1, 501, "14:30", 20),
            new Data("TRIP_103", "1A", 1, 501, "14:45", 35),
            new Data("TRIP_201", "4", 2, 501, "14:12", 2),
            new Data("TRIP_202", "4", 2, 501, "14:27", 17),
            new Data("TRIP_301", "100X", 3, 501, "14:50", 40)
    );

    @Test
    @DisplayName("Unit Test - LimitArrivals returns at most N buses per line")
    public void testLimitArrivals() {
        List testData = new ArrayList<>();

        testData.add(new Data("trip1", "Line 10", 10, 100, "12:00", 10));
        testData.add(new Data("trip2", "Line 10", 10, 100, "12:15", 25));
        testData.add(new Data("trip3", "Line 10", 10, 100, "12:30", 40));

        testData.add(new Data("trip4", "Line 20", 20, 100, "12:05", 15));

        List result = Main.limitArrivals(testData, 2);

        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Unit Test - CheckTime returns null for time outside 120 minutes window")
    public void testCheckTimeOutsideWindow() {
        LocalTime now = LocalTime.now();
        LocalTime farFuture = now.plusMinutes(150);
        String arrivalStr = String.format("%02d:%02d:00", farFuture.getHour(), farFuture.getMinute());

        int[] minutes = new int[1];
        String result = Main.checkTime(arrivalStr, "absolute", minutes);

        assertNull(result);
    }

    @Test
    @DisplayName("Unit Test - GroupAndSort handles empty list safely")
    public void testGroupAndSortEmptyList() {
        List emptyList = new ArrayList<>();
        List result = Main.groupAndSort(emptyList, 2);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Integration Test - Loading station data from GTFS files")
    public void testGetStopsStationIntegration() {
        int testStopId = 1;
        int limit = 2;

        List arrivals = Main.getStopsStation(testStopId, limit, "absolute");

        assertNotNull(arrivals, "The list should not be null after reading files.");
    }
}