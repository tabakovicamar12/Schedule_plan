import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    //Uporaba AI pri izpisih ter pri optimizaciji kode predvsem je mišljeno na shranjevanje podatkov iz datotek
    //ter pravilna uporaba naprej.

    //Dva takoimenovana slovarja uporaba za shranjevanje vrednosti prebranih iz datotek predvsem iz vidika
    //performans tj. datoteke ne odpiramo vedno vendar spodaj v Main klicemo takoj odpiranje in shranjevanje podatkov
    //v Map
    public static Map tripsMap = new HashMap<>();
    public static Map routeMap = new HashMap<>();

    //Razred Route
    public static class Route {
        public Integer route_id;
        public String short_name;

        public Route() {
        }

        public Route(Integer route_id, String short_name) {
            this.route_id = route_id;
            this.short_name = short_name;
        }
    }

    //file_types
    //0 - trips
    //1 - routes
    public static void loadInMemory(String fileName, Integer file_type) {
        try (BufferedReader br = new BufferedReader(new FileReader("Data/" + fileName))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.replace("\"", "").split(",");
                if (row.length > 2) {
                    if(file_type.equals(0)) {
                        tripsMap.put(row[2].trim(), row[0].trim());
                    } else if (file_type.equals(1)) {
                        routeMap.put(row[0].trim(), row[2].trim());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error with loading .txt file: " + e.getMessage());
        }
    }

    //Obdelava časovnih vrednosti
    static String checkTime(String arrival_time, String timeMode, int[] outMinutes) {
        try {
            String[] parts = arrival_time.trim().split(":");
            int arrivalHour = Integer.parseInt(parts[0]);
            int arrivalMinute = Integer.parseInt(parts[1]);

            if (arrivalHour >= 24) {
                arrivalHour -= 24;
            }

            LocalTime now = LocalTime.now();
            LocalTime arrival = LocalTime.of(arrivalHour, arrivalMinute);

            long diff = java.time.temporal.ChronoUnit.MINUTES.between(now, arrival);

            if (diff < 0 && now.getHour() >= 22 && arrivalHour < 3) {
                diff += 1440;
            }

            if (diff >= 0 && diff <= 120) {
                outMinutes[0] = (int) diff;

                if ("relative".equalsIgnoreCase(timeMode)) {
                    return diff + "min";
                }
                return arrival_time.trim();
            }

        } catch (Exception e) {
        }

        return null;
    }

    public static List<Data> groupAndSort(List<Data> list, Integer limit) {
        if (list != null && !list.isEmpty()) {
            return limitArrivals(list.stream()
                    .sorted(Comparator.comparing(Data::getRouteId)
                            .thenComparing(Data::getMinutesDiff))
                    .collect(Collectors.toList()), limit);
        }

        return Collections.emptyList();
    }

    static List<Data> limitArrivals(List<Data> list, Integer limit) {
        List<Data> filter_list = new ArrayList<>();
        List<Integer> processedRoutes = new ArrayList<>();
        var counter = 0;

        for (int i = 0; i < list.size(); i++) {
            counter = 0;
            var line = list.get(i);

            if (!processedRoutes.contains(line.getRouteId())) {
                for (int j = 0; j < list.size(); j++) {
                    if (Objects.equals(line.getRouteId(), list.get(j).getRouteId())) {
                        if (counter < limit) {
                            filter_list.add(list.get(j));
                            counter++;
                        }
                    }
                }

                processedRoutes.add(line.getRouteId());
            }
        }

        return filter_list;
    }

    private static Route getRoute(String route_id) {
        try {
            if(routeMap.containsKey(route_id)) {
                var data = routeMap.get(route_id);
                return new Route(Integer.parseInt(route_id), data.toString());
            }

            return new Route(0, "Unknown");
        } catch (Exception e) {
            return new Route(0, "Unknown");
        }
    }

    private static Route getTrip(String trip_id) {
        if (trip_id == null || trip_id.trim().isEmpty()) {
            System.err.println("[ERROR] getTrip: Parameter 'trip_id' is null or empty.");
            return new Route(0, "Unknown");
        }

        if (tripsMap == null || tripsMap.isEmpty()) {
            System.err.println("[WARNING] getTrip: Map 'tripsMap' is empty after calling loadInMemory().");
            return new Route(0, "Unknown");
        }

        String trimmedTripId = trip_id.trim();

        if (!tripsMap.containsKey(trimmedTripId)) {
            System.err.println("[WARNING] getTrip: Trip with ID '" + trimmedTripId + "' does not exist in the system.");
            return new Route(0, "Unknown");
        }

        String routeId = tripsMap.get(trimmedTripId).toString();

        return getRoute(routeId);
    }

    static List<Data> getStopsStation(Integer stop_id, Integer limit, String timeMode) {
        List<Data> list = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader("Data/stop_times.txt"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                var parts = line.split(",");
                var stops_times_per_station = Integer.parseInt(parts[3].trim());

                if(stops_times_per_station == stop_id) {
                    var trip_id = parts[0].trim();
                    var arrival_time = parts[1].trim();

                    int[] minutesHolder = new int[1];
                    var validationTime = checkTime(arrival_time, timeMode, minutesHolder);

                    if(validationTime != null) {
                        var trip = getTrip(trip_id);
                        list.add(new Data(
                                trip_id,
                                trip.short_name,
                                trip.route_id,
                                stops_times_per_station,
                                validationTime,
                                minutesHolder[0]
                        ));
                    }
                }
            }
            return groupAndSort(list, limit);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void getStop(Integer stop_id, Integer limit, String timeMode) {
        try(BufferedReader reader = new BufferedReader(new FileReader("Data/stops.txt"))) {
            String line;
            reader.readLine();
            String stationName = "";

            while ((line = reader.readLine()) != null) {
                var stop_data = line.split(",");

                Integer data_stop_id = Integer.parseInt(stop_data[0].trim());
                if(data_stop_id.equals(stop_id)) {
                    stationName = stop_data[2].trim();
                    System.out.println("Station: " + stationName + " (ID: " + data_stop_id + ")");
                    break;
                }
            }

            List<Data> stations = getStopsStation(stop_id, limit, timeMode);

            if (stations != null && !stations.isEmpty()) {
                System.out.println("=== ARRIVAL SCHEDULE ===");
                for (Data d : stations) {
                    System.out.println("🕒 " + d.getArrivalTime() + " | Route: " + d.getRouteShortName() + " (Trip: " + d.getTripId() + ")");
                }
            } else {
                System.out.println("No arrivals found.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        int stopId;
        int limit;
        String timeMode;

        if (args.length >= 3) {
            stopId = Integer.parseInt(args[0]);
            limit = Integer.parseInt(args[1]);
            timeMode = args[2];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Input station id:");
            stopId = scanner.nextInt();
            System.out.print("Input max number per line:");
            limit = scanner.nextInt();
            System.out.print("Input time mode (absolute/relative):");
            timeMode = scanner.next();
        }

        loadInMemory("trips.txt", 0);
        loadInMemory("routes.txt", 1);
        getStop(stopId, limit, timeMode);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(() -> {
            try {
                LocalTime now = LocalTime.now();
                LocalTime twoHoursLater = now.plusHours(2);

                System.out.println("[" + now + "] Refresh data: "
                        + now + " - " + twoHoursLater);

                getStop(stopId, limit, timeMode);

            } catch (Exception e) {
                System.err.println("Problem with refreshing data: " + e.getMessage());
            }
        }, 2, 2, TimeUnit.HOURS);
    }
}
