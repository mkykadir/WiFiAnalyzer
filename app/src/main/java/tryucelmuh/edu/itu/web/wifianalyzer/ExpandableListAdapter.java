package tryucelmuh.edu.itu.web.wifianalyzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Muhammed Kadir Yücel on 2.01.2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<mainSsid> _groupList;
    private HashMap<mainSsid, List<mainBssid>> _childList;

    public ExpandableListAdapter(Context getContext, List<mainSsid> getGroup, HashMap<mainSsid, List<mainBssid>> getChild){
        this._context = getContext;
        this._groupList = getGroup;
        this._childList = getChild;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._childList.get(this._groupList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final mainBssid currObject = (mainBssid)getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_subitem, null);
        }

        TextView bssid = (TextView)convertView.findViewById(R.id.wifiBssid);
        TextView strength = (TextView)convertView.findViewById(R.id.signalStrength);

        bssid.setText(currObject.returnBssid());
        strength.setText(currObject.returnStrength());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._childList.get(this._groupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._groupList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._groupList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final mainSsid currObject = (mainSsid) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_item, null);
        }

        TextView ssid = (TextView)convertView.findViewById(R.id.ssidName);
        TextView count = (TextView)convertView.findViewById(R.id.countOfSsid);
        TextView date = (TextView)convertView.findViewById(R.id.dateText);
        TextView id = (TextView)convertView.findViewById(R.id.identificator);

        ssid.setText(currObject.returnSsid());
        count.setText(String.valueOf(currObject.returnSsidCount()));
        date.setText(currObject.returnCurrDate());
        id.setText(currObject.returnId());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
