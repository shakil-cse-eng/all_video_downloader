package com.project.allvideodownloader.Fragment.Images;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;
import com.project.allvideodownloader.R;
import java.io.File;

public class ImageDetailsFragment extends Fragment {

    String imgPath;

    public ImageDetailsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_details, container, false);
        ZoomageView imageView = view.findViewById(R.id.demoViewId);

        imgPath = getArguments().getString("imagePath");
        File imgFile = new File(imgPath);

        if (imgFile.exists()) {
            Glide.with(getActivity()).load(imgPath).into(imageView);
        }
        return view;
    }
}