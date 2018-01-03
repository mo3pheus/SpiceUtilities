package space.exploration.spice.research;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space.exploration.communications.protocol.spice.MSLRelativePositions;
import space.exploration.spice.utilities.PositionUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CoverageGapAnalyzer {
    public static final Logger logger = LoggerFactory.getLogger(CoverageGapAnalyzer.class);

    public static void maina(String[] args) throws InterruptedException {
        String sourceFile = "/home/sanket/Documents/workspace/SpiceUtilities/src/main/resources/POSITIONS" +
                "/finalPositionCalc";
        configureLogging(Boolean.parseBoolean(args[1]));
        final String      DATE_FORMAT    = "yyyy-MM-dd~HH:mm:ss";
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(DATE_FORMAT);

        String startDateStr = "2016-01-01~00:00:00";
        String endDateStr   = "2017-11-30~00:00:00";
        DateTime startDate = dateTimeFormat.parseDateTime
                (startDateStr);
        DateTime endDate = dateTimeFormat.parseDateTime
                (endDateStr);
        int                                                   numThreads  = Integer.parseInt(args[0]);
        Map<Long, MSLRelativePositions.MSLRelPositionsPacket> finalResult = new HashMap<>();


        ExecutorService executorService = Executors.newFixedThreadPool
                (numThreads);

        double timeInterval = endDate.getMillis() - startDate.getMillis();
        timeInterval = timeInterval / ((double) numThreads);

        // get the individual tasks ready
        PositionCoverageGaps[] coverageGaps    = new PositionCoverageGaps[numThreads];
        ExecFileHelper[]       execFileHelpers = new ExecFileHelper[numThreads];
        DateTime               tmpStartDate    = new DateTime(startDate);
        for (int i = 0; i < numThreads; i++) {
            DateTime tmpEndDate = new DateTime(tmpStartDate.getMillis() + ((long) timeInterval));

            String stDate, enDate;
            stDate = dateTimeFormat.print(tmpStartDate);
            enDate = dateTimeFormat.print(tmpEndDate);
            coverageGaps[i] = new PositionCoverageGaps(stDate, enDate);
            execFileHelpers[i] = new ExecFileHelper(sourceFile, new String(sourceFile + Integer.toString(i)));
            execFileHelpers[i].makeFiles();
            tmpStartDate = new DateTime(tmpEndDate.getMillis() + 1);
        }

        Thread.sleep(1000l);

        // submit the tasks
        for (PositionCoverageGaps positionCoverageGaps : coverageGaps) {
            executorService.execute(positionCoverageGaps.getCoverageGapTask());
            System.out.println("Now running coverage analysis for startTime = " + dateTimeFormat.print
                    (positionCoverageGaps.getStartTime())
                                       + " endTime = " + dateTimeFormat.print(positionCoverageGaps.getEndTime()));
        }

        Thread.sleep(1000l);

        while (true) {
            for (int i = 0; i < coverageGaps.length; i++) {
                if (coverageGaps[i].getCoverageGapTask().isDone()) {
                    try {
                        Map<Long, MSLRelativePositions.MSLRelPositionsPacket> payLoad = coverageGaps[i]
                                .getCoverageGapTask().get();
                        for (Long timeStamp : payLoad.keySet()) {
                            System.out.println(" Timestamp = " + timeStamp + " positionsPacket = " + payLoad.get
                                    (timeStamp).toString());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            Thread.sleep(1000l);
        }
    }

    public static void main(String[] args) throws IOException {
        configureLogging(Boolean.parseBoolean(args[0]));
        final String      DATE_FORMAT    = "yyyy-MM-dd~HH:mm:ss";
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(DATE_FORMAT);

        String startDateStr = "2016-01-05~08:03:00";
        String endDateStr   = "2017-11-30~00:00:00";
        DateTime startDate = dateTimeFormat.parseDateTime
                (startDateStr);
        DateTime endDate = dateTimeFormat.parseDateTime
                (endDateStr);

        DateTime      tmpStDate     = new DateTime(startDate);
        PositionUtils positionUtils = new PositionUtils();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("coverageGaps.txt", true));
        String         op;
        while (tmpStDate.isBefore(endDate)) {
            positionUtils.setUtcTime(dateTimeFormat.print(tmpStDate));
            try {
                MSLRelativePositions.MSLRelPositionsPacket positionsPacket = positionUtils.getPositionPacket();
                op = Long.toString(tmpStDate.getMillis()) + ",";
                op += positionsPacket.getUtcTime() + "," + positionsPacket.getSclkValue() + "," + positionsPacket
                        .getEphemerisTime()
                        + "," + positionsPacket.getOwltMSLEarth() + "," + positionsPacket.getOwltEarthMSL();
                for (Double position : positionsPacket.getStateCuriosityList()) {
                    op += "," + Double.toString(position);
                }
                bufferedWriter.write(op);
                bufferedWriter.newLine();
            } catch (Exception coverageGap) {
                logger.info(" TimeStamp = " + tmpStDate.getMillis() + " positionsPacket = coverageGap ");
                coverageGap.printStackTrace();
                bufferedWriter.write(Long.toString(tmpStDate.getMillis()) + ", coverageGap");
                bufferedWriter.newLine();
            }
            tmpStDate = new DateTime(tmpStDate.getMillis() + TimeUnit.MINUTES.toMillis(1l));
        }
        bufferedWriter.close();
    }

    public static String configureLogging(boolean debug) {
        FileAppender fa = new FileAppender();

        if (!debug) {
            fa.setThreshold(Level.toLevel(Priority.INFO_INT));
            fa.setFile("positionUtilsReports/positionUtilsCoverageGap_" + Long.toString(System.currentTimeMillis()) +
                               ".log");
        } else {
            fa.setThreshold(Level.toLevel(Priority.DEBUG_INT));
            fa.setFile("positionUtilsDebugReports/positionUtilsCoverageGap_" + Long.toString(System.currentTimeMillis
                    ()) + ".log");
        }

        fa.setLayout(new PatternLayout("%d [%t] %p %c %x - %m%n"));

        fa.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(fa);
        return fa.getFile();
    }
}
