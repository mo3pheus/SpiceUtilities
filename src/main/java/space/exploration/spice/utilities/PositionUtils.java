package space.exploration.spice.utilities;

import space.exploration.communications.protocol.spice.MSLRelativePositions;

import java.io.*;

public class PositionUtils {
    private File positionsCalcFile = ExecUtils.getExecutionFile("/POSITIONS/finalPositionCalc");
    private String utcTime = "";

    public PositionUtils(String utcTime){
        this.utcTime = utcTime;
    }

    public String[] getPositionData(){
        return ExecUtils.getExecutionOutput(positionsCalcFile,utcTime);
    }

    public MSLRelativePositions.MSLRelPositionsPacket getPositionPacket(){
        /*et, stC0, stC1, stC2, stC3, stC4, stC5, ltCE, stE0, stE1, stE2, stE3, stE4, stE5, ltEC, posEC0, posEC1,
        posEC2, ltEC, angularSeparation*/
        MSLRelativePositions.MSLRelPositionsPacket.Builder mBuilder = MSLRelativePositions.MSLRelPositionsPacket.newBuilder();

        String[] positionsData = ExecUtils.getExecutionOutput(positionsCalcFile,utcTime);

        // Ephemeris Time
        mBuilder.setEphemerisTime(Double.parseDouble(positionsData[0]));

        // StateCuriosity
        mBuilder.addStateCuriosity(Double.parseDouble(positionsData[1]));
        mBuilder.addStateCuriosity(Double.parseDouble(positionsData[2]));
        mBuilder.addStateCuriosity(Double.parseDouble(positionsData[3]));
        mBuilder.addStateCuriosity(Double.parseDouble(positionsData[4]));
        mBuilder.addStateCuriosity(Double.parseDouble(positionsData[5]));
        mBuilder.addStateCuriosity(Double.parseDouble(positionsData[6]));
        mBuilder.setOwltMSLEarth(Double.parseDouble(positionsData[7]));

        // StateEarth
        mBuilder.addStateEarth(Double.parseDouble(positionsData[8]));
        mBuilder.addStateEarth(Double.parseDouble(positionsData[9]));
        mBuilder.addStateEarth(Double.parseDouble(positionsData[10]));
        mBuilder.addStateEarth(Double.parseDouble(positionsData[11]));
        mBuilder.addStateEarth(Double.parseDouble(positionsData[12]));
        mBuilder.addStateEarth(Double.parseDouble(positionsData[13]));
        mBuilder.setOwltEarthMSL(Double.parseDouble(positionsData[14]));

        //Position Earth w.r.t Curiosity
        mBuilder.addPositionEarthWRTCuriosity(Double.parseDouble(positionsData[15]));
        mBuilder.addPositionEarthWRTCuriosity(Double.parseDouble(positionsData[16]));
        mBuilder.addPositionEarthWRTCuriosity(Double.parseDouble(positionsData[17]));
        mBuilder.setOwltEarthMSL2(Double.parseDouble(positionsData[18]));

        //Angular separation Earth vs MSL HGA
        mBuilder.setAngSepHGAEarth(Double.parseDouble(positionsData[19]));

        return mBuilder.build();
    }
}
