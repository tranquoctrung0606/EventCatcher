package com.linh.wiinav.ui.report;

import com.linh.wiinav.R;

import java.util.Arrays;
import java.util.List;

public class MajorReportDataFactory
{
    public static List<MajorReport> makeReports() {
        return Arrays.asList(makeReportTraffic(),
                             makeReportPolice(),
                             makeReportCrash(),
                             makeReportHazard(),
                             makeReportMapIssue(),
                             makeReportCamera(),
                             makeReportRoadsideHelp());
    }

    private static MajorReport makeReportMapIssue()
    {
        return new MajorReport("Map Issue", makeMinorReportMapIssue(), R.drawable.ic_report_mapissue);
    }

    private static List<MinorReport> makeMinorReportMapIssue()
    {
        MinorReport missingroad = new MinorReport("Missing Road", R.mipmap.ic_launcher);
        MinorReport turnnotallowed = new MinorReport("Turn not Allowed", R.mipmap.ic_launcher);
        MinorReport speedlimit = new MinorReport("Speed limit", R.mipmap.ic_launcher);
        MinorReport wrongdirection = new MinorReport("Wrong Address", R.mipmap.ic_launcher);
        MinorReport missingexit = new MinorReport("Missing Exit", R.mipmap.ic_launcher);
        MinorReport oneway = new MinorReport("One Way", R.mipmap.ic_launcher);
        MinorReport closure = new MinorReport("Closure", R.mipmap.ic_launcher);

        return Arrays.asList(missingroad, turnnotallowed, speedlimit, wrongdirection, missingexit, oneway, closure);
    }

    private static MajorReport makeReportCamera()
    {
        return new MajorReport("Camera", makeMinorReportCamera(), R.drawable.ic_report_camera);
    }

    private static List<MinorReport> makeMinorReportCamera()
    {
        MinorReport redlight = new MinorReport("Speed", R.mipmap.ic_launcher);
        MinorReport speed = new MinorReport("Redlight", R.mipmap.ic_launcher);
        MinorReport fake = new MinorReport("Fake", R.mipmap.ic_launcher);

        return Arrays.asList(redlight, speed, fake);
    }

    private static MajorReport makeReportHazard()
    {
        return new MajorReport("Hazard", makeMinorReportHazard(), R.drawable.ic_report_hazard);
    }

    private static List<MinorReport> makeMinorReportHazard()
    {
        MinorReport flood = new MinorReport("Flood", R.mipmap.ic_launcher);
        MinorReport pothole = new MinorReport("Pothole", R.mipmap.ic_launcher);
        MinorReport construction = new MinorReport("Construction", R.mipmap.ic_launcher);
        MinorReport missingsign = new MinorReport("Missing Sign", R.mipmap.ic_launcher);
        MinorReport objectonroad = new MinorReport("Object on Road", R.mipmap.ic_launcher);
        MinorReport brokentrafficlight = new MinorReport("Broken Traffic Light", R.mipmap.ic_launcher);
        MinorReport stoppedvehicle = new MinorReport("Stopped Vehicle", R.mipmap.ic_launcher);
        MinorReport animal = new MinorReport("Animal", R.mipmap.ic_launcher);

        return Arrays.asList(flood, pothole, construction, missingsign, objectonroad, brokentrafficlight, stoppedvehicle, animal);
    }

    private static MajorReport makeReportRoadsideHelp()
    {
        return new MajorReport("Roadside Help", makeMinorReportRoadsideHelp(), R.drawable.ic_report_roadsidehelp);
    }

    private static List<MinorReport> makeMinorReportRoadsideHelp()
    {
        MinorReport sendHelp = new MinorReport("Send Help", R.mipmap.ic_launcher);

        return Arrays.asList(sendHelp);
    }

    private static MajorReport makeReportCrash()
    {
        return new MajorReport("Crash", makeMinorReportCrash(), R.drawable.ic_report_crash);
    }

    private static List<MinorReport> makeMinorReportCrash()
    {
        MinorReport major = new MinorReport("Major", R.mipmap.ic_launcher);
        MinorReport minor = new MinorReport("Minor", R.mipmap.ic_launcher);

        return Arrays.asList(major, minor);
    }

    private static MajorReport makeReportPolice()
    {
        return new MajorReport("Police", makeMinorReportPolice(), R.drawable.ic_report_police);
    }

    private static List<MinorReport> makeMinorReportPolice()
    {
        MinorReport onRoad = new MinorReport("On Road", R.mipmap.ic_launcher);
        MinorReport hidden = new MinorReport("Hidden", R.mipmap.ic_launcher);

        return Arrays.asList(onRoad, hidden);
    }

    private static MajorReport makeReportTraffic()
    {
        return new MajorReport("Traffic", makeMinorReportTraffic(), R.drawable.ic_report_traffic);
    }

    private static List<MinorReport> makeMinorReportTraffic()
    {
        MinorReport moderate = new MinorReport("Moderate", R.drawable.ic_report_traffic_moderate);
        MinorReport heavy = new MinorReport("Heavy", R.drawable.ic_report_traffic_heavy);
        MinorReport stuck = new MinorReport("Stuck", R.drawable.ic_report_traffic_stuck);

        return Arrays.asList(moderate, heavy, stuck);
    }
}
