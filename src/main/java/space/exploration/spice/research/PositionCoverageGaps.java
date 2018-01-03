package space.exploration.spice.research;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.exploration.communications.protocol.spice.MSLRelativePositions;
import space.exploration.spice.utilities.PositionUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class PositionCoverageGaps {
    public static final String DATE_FORMAT = "yyyy-MM-dd~HH:mm:ss";

    private DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(DATE_FORMAT);
    private PositionUtils     positionUtils  = null;
    private DateTime          startTime      = null;
    private DateTime          endTime        = null;

    private FutureTask<Map<Long, MSLRelativePositions.MSLRelPositionsPacket>> coverageGapTask = null;
    private Map<Long, MSLRelativePositions.MSLRelPositionsPacket>             coverageGap     = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(PositionCoverageGaps.class);

    public PositionCoverageGaps(String startTimeString, String endTimeString) throws IllegalArgumentException {
        this.positionUtils = new PositionUtils();
        this.startTime = dateTimeFormat.parseDateTime(startTimeString);
        this.endTime = dateTimeFormat.parseDateTime(endTimeString);
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time can not be before startTime");
        }
        coverageGapTask = new FutureTask<>(new Callable<Map<Long, MSLRelativePositions.MSLRelPositionsPacket>>() {
            @Override
            public Map<Long, MSLRelativePositions.MSLRelPositionsPacket> call() throws Exception {
                return computeCoverageGaps();
            }
        });
    }

    private Map<Long, MSLRelativePositions.MSLRelPositionsPacket> computeCoverageGaps() {
        DateTime                                   current             = new DateTime(startTime);
        MSLRelativePositions.MSLRelPositionsPacket positionsPacket     = null;
        double                                     lastKnownLightDelay = 0.0d;
        while (current.isBefore(endTime)) {
            positionUtils.setUtcTime(dateTimeFormat.print(current));
            try {
                logger.info("OneWayLightTime MSL Earth = " + Double.toString(positionUtils.getPositionPacket()
                                                                                     .getOwltMSLEarth()));
                positionsPacket = positionUtils.getPositionPacket();
            } catch (Exception positionUtilsException) {
                positionUtilsException.printStackTrace();
                coverageGap.put(current.getMillis(), positionsPacket);
            }
            current = new DateTime(current.getMillis() + TimeUnit.SECONDS.toMillis(1l));
        }
        return coverageGap;
    }

    public FutureTask<Map<Long, MSLRelativePositions.MSLRelPositionsPacket>> getCoverageGapTask() {
        return coverageGapTask;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }
}
