package reporting;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {
    static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
    private static ExtentReports extent = ExtentManager.getInstance();
    private static ExtentReports extentUpdate = ExtentManager.getInstance();
    public static synchronized ExtentTest getTest() {
        //returns ExtentTest object, hence its stored in a Map that has threadId as key
        return extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }
    public static synchronized void endTest() {
        //ends ExtentTest instance by calling ExtentReport.ExtentTest method
        extent.endTest(extentTestMap.get((int) (long) (Thread.currentThread().getId())));
    }
    //this is what gets called first, then it calls third startTest method
    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }
    /*this method is not used. Here for demonstration*/
    public static synchronized ExtentTest startTestClass(String className,String testName) {
        return startTest(className,testName, "");
    }
    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = extent.startTest(testName, desc);
        //Map to store an integer and ExtentTest instance
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        return test;
    }
    /*this method is not used. Here for demonstration*/
    public static synchronized ExtentTest startTest(String className,String testName, String desc) {
        ExtentTest test = extentUpdate.startTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        return test;
    }
}
