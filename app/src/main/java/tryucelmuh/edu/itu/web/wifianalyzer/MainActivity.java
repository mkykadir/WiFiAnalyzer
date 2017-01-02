package tryucelmuh.edu.itu.web.wifianalyzer;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Spinner apNameList;
    WifiManager mainManager;
    ExpandableListView mainList;
    private String[] ssidNames;
    HashMap<mainSsid, List<mainBssid>> uniqueBssidList;
    List<mainSsid> mainItems;
    EditText identificatorInput;
    public static String PACKAGE_NAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        mainManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if(!mainManager.isWifiEnabled())
        {
            alertEnableWifi().show();
        }

        identificatorInput = (EditText)findViewById(R.id.tanimText);
        mainList = (ExpandableListView)findViewById(R.id.logsList);
        apNameList = (Spinner)findViewById(R.id.wifiNameList);
        uniqueBssidList = new HashMap<>();
        mainItems = new ArrayList<mainSsid>();

        scanForSSID();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ssidNames);
        apNameList.setAdapter(adapter);
    }

    public void analyzeWifi(View v){
        mainBssid itemToAdd;
        String getSelectedSsid = apNameList.getSelectedItem().toString();
        mainManager.startScan();
        List<ScanResult> wifiList = mainManager.getScanResults();


        List<mainBssid> bssidList = new ArrayList<>();

        for(int i = 0; i < wifiList.size(); i++){
            ScanResult temp = wifiList.get(i);
            if(temp.SSID.equals(getSelectedSsid)){
                itemToAdd = new mainBssid(temp.BSSID, String.valueOf(temp.level));
                bssidList.add(itemToAdd);
            }
        }

        mainSsid currObject = new mainSsid(getSelectedSsid, bssidList.size(), identificatorInput.getText().toString());
        uniqueBssidList.put(currObject, bssidList);
        mainItems.add(currObject);
        mainList.setAdapter(new tryucelmuh.edu.itu.web.wifianalyzer.ExpandableListAdapter(this, mainItems, uniqueBssidList));
    }

    public void exportData(View v){
        DataBaseHelper mainDb = new DataBaseHelper(this);
        for(HashMap.Entry<mainSsid, List<mainBssid>> entry : uniqueBssidList.entrySet()){
            mainDb.insertValue(entry);
        }

        try{
            File storage = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if(storage.canWrite()){
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
                String current = "//data//" + PACKAGE_NAME + "//databases//" + mainDb.DB_NAME;
                String export = "exportedDb_" + df.format(c.getTime())+".db";


                File currFile = new File(data,current);
                File exportFile = new File(storage, export);

                if(currFile.exists()){
                    FileChannel input = new FileInputStream(currFile).getChannel();
                    FileChannel output = new FileOutputStream(exportFile).getChannel();
                    output.transferFrom(input, 0, input.size());
                    input.close();
                    output.close();
                }
                Toast.makeText(this, getString(R.string.fileExport), Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(this, getString(R.string.fileError), Toast.LENGTH_LONG).show();
        }
    }

    public void clearData(View v){
        clearDatabase().show();
    }

    private void clearList(){
        uniqueBssidList.clear();
        mainItems.clear();
        mainList.setAdapter(new tryucelmuh.edu.itu.web.wifianalyzer.ExpandableListAdapter(this, mainItems, uniqueBssidList));
    }

    private void scanForSSID(){
        mainManager.startScan();
        List<ScanResult> ssidList = mainManager.getScanResults();

        Set<String> uniqueList = new HashSet<String>();

        for(int i = 0; i < ssidList.size(); i++){
            uniqueList.add(ssidList.get(i).SSID);
        }
        ssidList.clear();

        ssidNames = new String[uniqueList.size()+1];
        int i =0;
        for(Iterator<String> it = uniqueList.iterator(); it.hasNext();){
            String temp = it.next();
            ssidNames[i] = temp;
            i++;
        }

        ssidNames[i] = getString(R.string.newWifi);
        uniqueList.clear();
    }

    private AlertDialog clearDatabase(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(R.string.clearDbPromptMsg).setTitle(R.string.clearDbPromptTitle);

        builder.setPositiveButton(R.string.yesButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO clear table
                DataBaseHelper mainDb = new DataBaseHelper(MainActivity.this);
                clearList();
                mainDb.clearTable();
            }
        });

        builder.setNegativeButton(R.string.noButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearList();
            }
        });

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog alertEnableWifi(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(R.string.alertEnableWifiMessage).setTitle(R.string.alertEnableWifiTitle);

        builder.setPositiveButton(R.string.activateDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainManager.setWifiEnabled(true);
            }
        });

        builder.setNegativeButton(R.string.exitDialog, new DialogInterface.OnClickListener() {
           @Override
            public void onClick(DialogInterface dialog, int which){
               android.os.Process.killProcess(android.os.Process.myPid());
               System.exit(1);
           }
        });



        AlertDialog dialog = builder.create();
        return dialog;
    }
}
