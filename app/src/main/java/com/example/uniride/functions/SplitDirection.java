package com.example.uniride.functions;

import android.util.Log;

public class SplitDirection {

    public SplitDirection() {
    }

    public String getDirection(String allDirection){
        String[] allDirections =  allDirection.split(",");

        return allDirections[0]+", "+allDirections[1];
    }

    public String getPostalCode(String direction){
        return "efe";
    }

    public String getExtraDirection(String direction){
        return "efe";
    }
}
