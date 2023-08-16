package com.project.allvideodownloader.Adapter;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.project.allvideodownloader.Fragment.Images.ImageDetailsFragment;
import com.project.allvideodownloader.Model.ImageStatus;
import java.util.ArrayList;

public class ImageFragmentAdapter extends FragmentPagerAdapter {
    ArrayList<ImageStatus> imageList;
    public ImageFragmentAdapter(@NonNull FragmentManager fm, ArrayList<ImageStatus> imageList) {
        super(fm);
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ImageDetailsFragment imageFragment = new ImageDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", imageList.get(position).getPath());
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}
