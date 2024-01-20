package rsarschoolmodel.com.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import rsarschoolmodel.com.modelClass.response.HelpBannerResponseModel;
import rsarschoolmodel.com.rsarschoolmodel.R;

public class HelpImageAdapter extends PagerAdapter {


    private final ArrayList<HelpBannerResponseModel.BannersDatum> imageModelArrayList;
    private final LayoutInflater inflater;
    private final Context context;

    public HelpImageAdapter(Context context, ArrayList<HelpBannerResponseModel.BannersDatum> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_images_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);

        Glide
                .with(context)
                .load(imageModelArrayList.get(position).getBannerURL())
                .into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}