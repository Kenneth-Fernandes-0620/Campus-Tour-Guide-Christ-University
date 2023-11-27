package org.pytorch.demo.objectdetection.sensors;

import android.content.Context;
import android.content.ContextWrapper;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import org.pytorch.demo.objectdetection.Blocks;
import org.pytorch.demo.objectdetection.util.LocationCoordinatesPair;

import java.util.ArrayList;
import java.util.Arrays;

public class SensorHelper extends ContextWrapper {

    public Sensor accelerometer;
    public Sensor magnetometer;

    public float[] accelerometerData = new float[3];
    public float[] magnetometerData = new float[3];

    public double currentLatitude;
    public double currentLongitude;

    public SensorManager sensorManager;


    private final ArrayList<LocationCoordinatesPair> locations = new ArrayList<>(Arrays.asList(
            new LocationCoordinatesPair(12.934687053830347, 77.6060818229854, 12.934189109317886, 77.60594357746945, Blocks.BLOCK_CENTRAL),
            new LocationCoordinatesPair(12.934358353217826, 77.60667592334495, 12.933717955414886, 77.60665979895988, Blocks.BLOCK_1),
            new LocationCoordinatesPair(12.933107469673649, 77.60633481013775, 12.932477733408614, 77.60633150708198, Blocks.BLOCK_2),
            new LocationCoordinatesPair(12.931818275702613, 77.60630638425552, 12.931818275702613, 77.60668168739105, Blocks.BLOCK_3),
            new LocationCoordinatesPair(12.932131779911844, 77.60609483876607, 12.931696471683725, 77.60605042188594, Blocks.BLOCK_4),
            new LocationCoordinatesPair(12.931451146279457, 77.60608561770111, 12.931097464320729, 77.60608975707392, Blocks.BLOCK_RND)
    ));


    public SensorHelper(Context base) {
        super(base);
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (this.accelerometer == null || this.magnetometer == null)
            Log.d("Sample Tag", "SensorHelper: A sensor is NULL");
    }

    public static boolean checkIntersection(double[] userPosition, double heading, double[][] lineCoordinates) {
        double x = userPosition[0];
        double y = userPosition[1];
        double theta = Math.toRadians(heading);
        double m1 = Math.tan(theta);
        double b1 = y - m1 * x;

        double x1 = lineCoordinates[0][0];
        double y1 = lineCoordinates[0][1];
        double x2 = lineCoordinates[1][0];
        double y2 = lineCoordinates[1][1];
        double m2 = (y2 - y1) / (x2 - x1);
        double b2 = y1 - m2 * x1;

        // Calculate intersection point
        if (m1 == m2) {
            return false; // Parallel lines, no intersection
        }

        double intersectionX = (b2 - b1) / (m1 - m2);
        double intersectionY = m1 * intersectionX + b1;

        // Check if the intersection point lies within the line segment
        return Math.min(x1, x2) <= intersectionX && intersectionX <= Math.max(x1, x2) && Math.min(y1, y2) <= intersectionY && intersectionY <= Math.max(y1, y2);
    }

    public static ArrayList<Blocks> getIntersectionCode(double[] userPosition, double heading, ArrayList<LocationCoordinatesPair> locations) {
        ArrayList<Blocks> blocks = new ArrayList<>();
        for (LocationCoordinatesPair location : locations) {
            if (checkIntersection(userPosition, heading, new double[][]{{location.latitudeStart, location.longitudeStart}, {location.latitudeEnd, location.longitudeEnd}}))
                blocks.add(location.blocks);
        }
        if (blocks.size() == 0)
            blocks.add(Blocks.BLOCK_UNKNOWN);
        return blocks;
    }

    public void calculateSensorReadings() {
        // Calculate device orientation using accelerometer and magnetometer data
        float[] rotationMatrix = new float[9];
        float[] orientationValues = new float[3];

        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerData, magnetometerData)) {

            SensorManager.getOrientation(rotationMatrix, orientationValues);

            float azimuth = (float) Math.toDegrees(orientationValues[0]);
            float pitch = (float) Math.toDegrees(orientationValues[1]);
            float roll = (float) Math.toDegrees(orientationValues[2]);

            StringBuilder Block = new StringBuilder();
//            Log.d(TAG, "Block Pointing to: " + getIntersectionCode(new double[]{currentLatitude, currentLongitude}, azimuth));
            for (Blocks blocks : getIntersectionCode(new double[]{currentLatitude, currentLongitude}, azimuth, locations))
                Block.append(String.valueOf(blocks)).append(", ");

//            heading.setText(getString(R.string.heading, getCardinalDirection(azimuth)));
//            Log.d(TAG, "Intersects ? : " + checkIntersection(new double[]{12.934377813069952, 77.60618351913456}, azimuth, new double[][]{{12.934687053830347, 77.6060818229854}, {12.934189109317886, 77.60594357746945}}));

//            Log.d(TAG, "onSensorChanged: Heading: " + azimuth + ", Towards: " + getCardinalDirection(azimuth));

//            Log.d(TAG, "onSensorChanged, aizmuth: " + azimuth);
//            Log.d(TAG, "onSensorChanged, pitch: " + pitch);
//            Log.d(TAG, "onSensorChanged, roll: " + roll);


        }
    }

    private String getCardinalDirection(float heading) {
        String[] cardinalDirections = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};
        int index = Math.round((heading % 360) / 45);
        if (index < 0) index += 8;
        return cardinalDirections[index];
    }

}
