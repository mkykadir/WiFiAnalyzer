package tryucelmuh.edu.itu.web.wifianalyzer;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Muhammed Kadir Yücel on 2.01.2017.
 */

public class mainSsid {
    private String ssid;
    private String currDate;
    private int ssidCount;
    private String tanim;

    mainSsid(String getSsid, int getCount, String getTanim){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        this.ssid = getSsid;
        this.currDate = df.format(c.getTime());
        this.ssidCount = getCount;
        this.tanim = getTanim;
    }

    public int returnSsidCount(){
        return this.ssidCount;
    }

    public String returnSsid(){
        return this.ssid;
    }

    public String returnCurrDate(){
        return this.currDate;
    }

    public String returnId(){
        return this.tanim;
    }
}
