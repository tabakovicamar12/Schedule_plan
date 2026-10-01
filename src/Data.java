public class Data {
    private String tripId;
    private String routeShortName;
    private Integer routeId;
    private Integer stationId;
    private String arrivalTime;
    private int minutesDiff;

    public Data(String tripId, String routeShortName, Integer routeId, Integer stationId, String arrivalTime, int minutesDiff) {
        this.tripId = tripId;
        this.routeShortName = routeShortName;
        this.routeId = routeId;
        this.stationId = stationId;
        this.arrivalTime = arrivalTime;
        this.minutesDiff = minutesDiff;
    }

    public String getTripId() { return tripId; }
    public String getRouteShortName() { return routeShortName; }
    public Integer getRouteId() { return routeId; }
    public Integer getStationId() { return stationId; }
    public String getArrivalTime() { return arrivalTime; }
    public int getMinutesDiff() { return minutesDiff; }
}
