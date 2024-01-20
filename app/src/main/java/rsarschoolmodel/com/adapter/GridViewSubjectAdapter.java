package rsarschoolmodel.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import rsarschoolmodel.com.modelClass.response.SubjectResponseModel;
import rsarschoolmodel.com.modelClass.response.UserLoginResponseModel;
import rsarschoolmodel.com.rsarschoolmodel.R;
import rsarschoolmodel.com.utilities.SharedPreferenceManager;


public class GridViewSubjectAdapter extends ArrayAdapter<SubjectResponseModel.SubjectDatum> {
	String topBgCode,schoolNameColor;

	public GridViewSubjectAdapter(Context context, List<SubjectResponseModel.SubjectDatum> subjectData) {
		super(context, 0, subjectData);

		UserLoginResponseModel.UserDatum userData =(UserLoginResponseModel.UserDatum) SharedPreferenceManager.getUserData(context);
		topBgCode = userData.getTopBgCode();
		schoolNameColor = userData.getSchoolNameColor();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		View listitemView = convertView;
		if (listitemView == null) {
			listitemView = LayoutInflater.from(getContext()).inflate(R.layout.gridview, parent, false);
		}

		SubjectResponseModel.SubjectDatum subjectDatum = getItem(position);
		TextView subjectName = listitemView.findViewById(R.id.txtName);
		LinearLayout llBackground = (LinearLayout) listitemView.findViewById(R.id.llBackground);

		subjectName.setText(subjectDatum.getSubjectTitle());
		subjectName.setTextColor(Color.parseColor(schoolNameColor));
		llBackground.setBackgroundColor(Color.parseColor(topBgCode));
		return listitemView;
	}

}