package arabiannight.tistroy.com.expandable.adapter;

import java.util.ArrayList;

import kr.ac.kookmin.cs.cap3.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class BaseExpandableAdapter extends BaseExpandableListAdapter{
	
	private ArrayList<String> groupList = null;
	private ArrayList<ArrayList<String>> childList = null;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	
	public BaseExpandableAdapter(Context c, ArrayList<String> groupList, 
			ArrayList<ArrayList<String>> childList){
		super();
		this.inflater = LayoutInflater.from(c);
		this.groupList = groupList;
		this.childList = childList;
	}
	
	// 洹몃９ ��ъ�������� 諛����������.
	@Override
	public String getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	// 洹몃９ ��ъ�댁��瑜� 諛����������.
	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	// 洹몃９ ID瑜� 諛����������.
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 洹몃９酉� 媛�媛���� ROW 
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if(v == null){
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_row, parent, false);
			viewHolder.tv_groupName = (TextView) v.findViewById(R.id.tv_group);
		//	viewHolder.iv_image = (ImageView) v.findViewById(R.id.iv_image);
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		
		// 洹몃９��� ��쇱�������� ��レ����� �����댁����� 蹂�寃쏀�� 以����.
	/*	if(isExpanded){
			viewHolder.iv_image.setBackgroundColor(Color.GREEN);
		}else{
			viewHolder.iv_image.setBackgroundColor(Color.WHITE);
		}*/
		
		viewHolder.tv_groupName.setText(getGroup(groupPosition));
		
		return v;
	}
	
	// 李⑥�쇰��酉곕�� 諛����������.
	@Override
	public String getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}
	
	// 李⑥�쇰��酉� ��ъ�댁��瑜� 諛����������.
	@Override
	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
	}

	// 李⑥�쇰��酉� ID瑜� 諛����������.
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 李⑥�쇰��酉� 媛�媛���� ROW
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if(v == null){
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_row, null);
			viewHolder.tv_childName = (TextView) v.findViewById(R.id.tv_child);
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		
		viewHolder.tv_childName.setText(getChild(groupPosition, childPosition));
		
		return v;
	}

	@Override
	public boolean hasStableIds() {	return true; }

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
	
	class ViewHolder {
		public ImageView iv_image;
		public TextView tv_groupName;
		public TextView tv_childName;
	}

}




