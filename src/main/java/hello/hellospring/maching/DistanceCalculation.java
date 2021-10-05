package hello.hellospring.maching;


import hello.hellospring.members.Member;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Component;

import javax.mail.search.SearchTerm;


/* 거리계산 class*/
@Component
public class DistanceCalculation {
    public static final double R = 6372.8; // In kilometers


    public String DistanceCalculation(double lat1, double lon1, double lat2, double lon2, String unit, MsMembers data, MsMembers members) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "k") {
            dist = dist * 1.609344;
        } else if(unit == "m"){
            dist = dist * 1609.344;
        }

        int km = (int) (dist / 1000);
        int m = (int) (dist % 1000);

        String streetData = null;
        if (km <= Integer.parseInt(data.getMyWantStreet())) {
            if (km != 0) {
                streetData = km + "." + m + "km";
                System.out.println("ID : " + members.getMyId() + "\n"
                        + km + "."
                        + m + "km");
            } else {
                streetData = m + "m";
                System.out.println("ID : " + members.getMyId() + "\n"
                        + m + "m");
            }
        }
        return streetData;

    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
