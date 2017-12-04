package group0669.com.example.supermart;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Ayanami on 2017/12/1.
 */

public class ImageSwipeActivity extends PagerAdapter {

  private int[] images = {R.drawable.fishing_rod, R.drawable.hockey_stick, R.drawable.protein_bar,
      R.drawable.skates};
  private Context context;
  private LayoutInflater layoutInflater;

  public ImageSwipeActivity(Context ctx) {
    this.context = ctx;
  }

  @Override
  public int getCount() {
    return images.length;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = layoutInflater.inflate(R.layout.swipe_images, null);
    ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
    imageView.setImageResource(images[position]);
    ViewPager viewPager = (ViewPager) container;
    viewPager.addView(view, 0);
    return view;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    ViewPager viewPager = (ViewPager) container;
    View view = (View) object;
    viewPager.removeView(view);
  }


}
