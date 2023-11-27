package org.pytorch.demo.objectdetection.util;

import org.pytorch.demo.objectdetection.Blocks;

public class LocationCoordinatesPair {
    public double latitudeStart;
    public double longitudeStart;
    public double latitudeEnd;
    public double longitudeEnd;
    public Blocks blocks;

    public LocationCoordinatesPair(double latitudeStart, double longitudeStart, double latitudeEnd, double longitudeEnd, Blocks block) {
        this.latitudeStart = latitudeStart;
        this.longitudeStart = longitudeStart;
        this.latitudeEnd = latitudeEnd;
        this.longitudeEnd = longitudeEnd;
        this.blocks = block;
    }
}