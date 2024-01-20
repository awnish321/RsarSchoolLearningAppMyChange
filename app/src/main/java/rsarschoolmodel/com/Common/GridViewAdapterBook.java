package rsarschoolmodel.com.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import rsarschoolmodel.com.rsarschoolmodel.R;

public class GridViewAdapterBook extends BaseAdapter {

		//Context
	private Context context;

	List<SetterGetter_Sub_Chap> setterGetters;
	SetterGetter_Sub_Chap vimSetter;
	boolean array[];
	SharedPreferences preferences;
	String Pref_Bg_Code, Pref_Top_Bg_Code, Pref_Button_Bg, Pref_School_UI, Pref_School_name, Pref_Fd_School_name,Pref_Sch_Name_Color;


	public GridViewAdapterBook(Context context, List<SetterGetter_Sub_Chap> getters) {
		this.context = context;
		this.setterGetters = getters;
		array = new boolean[getters.size()];
		preferences = context.getSharedPreferences("RSAR_School_Model", Context.MODE_PRIVATE);
		Pref_School_UI = preferences.getString("Rsar_School_UI", "");
		Pref_Bg_Code = preferences.getString("Rsar_Bg_Code", "");
		Pref_Top_Bg_Code = preferences.getString("Rsar_Top_Bg_Code", "");
		Pref_Button_Bg = preferences.getString("Rsar_Button_Bg", "");
		Pref_Sch_Name_Color = preferences.getString("Rsar_Sch_Bgcol_Name", "");
	}

	@Override
	public int getViewTypeCount() {

		return getCount();
	}

	@Override
	public int getItemViewType(int position) {

		return position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return setterGetters.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return setterGetters.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View grid;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vimSetter = new SetterGetter_Sub_Chap();
		vimSetter = setterGetters.get(position);
		if (convertView == null) {
			grid = new View(context);
			grid = inflater.inflate(R.layout.gridview, null);
			TextView Sub_Name = (TextView) grid.findViewById(R.id.txtName);
			LinearLayout llBackground = (LinearLayout) grid.findViewById(R.id.llBackground);

			Sub_Name.setText(vimSetter.getBookName());
			Sub_Name.setTextColor(Color.parseColor(Pref_Sch_Name_Color));
			llBackground.setBackgroundColor(Color.parseColor(Pref_Top_Bg_Code));

		} else {
			grid = (View) convertView;
		}
		return grid;
	}

}
