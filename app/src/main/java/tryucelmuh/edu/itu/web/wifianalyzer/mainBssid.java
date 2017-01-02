package tryucelmuh.edu.itu.web.wifianalyzer;

/**
 * Created by Muhammed Kadir Yücel on 2.01.2017.
 */

class mainBssid {
    private String bssid;
    private String strength;

    mainBssid(String getBssid, String getStrength){
        this.bssid = getBssid;
        this.strength = getStrength + " dB";
    }

    String returnBssid(){
        return this.bssid;
    }

    String returnStrength(){
        return this.strength;
    }
}
