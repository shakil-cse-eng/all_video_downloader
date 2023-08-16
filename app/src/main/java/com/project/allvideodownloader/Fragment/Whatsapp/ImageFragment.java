package com.project.allvideodownloader.Fragment.Whatsapp;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.project.allvideodownloader.Adapter.ImageAdapter;
import com.project.allvideodownloader.R;
import com.project.allvideodownloader.Model.ImageStatus;
import com.project.allvideodownloader.databinding.FragmentImageBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private final ArrayList<ImageStatus> imagesList = new ArrayList<>();
    private final Handler handler = new Handler();
    private TextView messageTextView;
    private FragmentImageBinding binding;
    private ImageAdapter adapter;
    private ArrayList<ImageStatus> list;
    private ImageStatus model;
    String targetPath, targetPathBusiness, path1,path2,path3,businessPath1,businessPath2,businessPath3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        list = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerViewImageId);
        progressBar = view.findViewById(R.id.progressBarImageId);
        messageTextView = view.findViewById(R.id.messageTextImage);

        //recyclerView.setHasFixedSize(true);

        getData();
        binding.imageSwipeRefreshId.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

    }

    private void getData() {
        targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/";
        path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/Statuses/";
        path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images/Sent/";
        path3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images/Private/";

        File filePath1 = new File(path1);
        File[] statusFiles = filePath1.listFiles();
        File filePath2 = new File(path2);
        File[] statusFiles2 = filePath2.listFiles();
        File filePath3 = new File(path3);
        File[] statusFiles3 = filePath3.listFiles();

        if (statusFiles != null && statusFiles.length > 0){
            targetPath = path1;
        }else if (statusFiles2 != null && statusFiles2.length > 0) {
            targetPath = path2;
        } else if (statusFiles3 != null && statusFiles3.length > 0) {
            targetPath = path3;
        }



        targetPathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/";
        businessPath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/Statuses/";
        businessPath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/WhatsApp Images/Sent/";
        businessPath3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/WhatsApp Images/Private/";

        File fileBusinessPath1 = new File(businessPath1);
        File[] statusBusinessFiles = fileBusinessPath1.listFiles();
        File fileBusinessPath2 = new File(businessPath2);
        File[] statusBusinessFile2 = fileBusinessPath2.listFiles();
        File fileBusinessPath3 = new File(businessPath3);
        File[] statusBusinessFile3 = fileBusinessPath3.listFiles();

        if (statusBusinessFiles != null && statusBusinessFiles.length > 0){
            targetPathBusiness = businessPath1;
        }else if (statusBusinessFile2 != null && statusBusinessFile2.length > 0) {
            targetPathBusiness = businessPath2;
        } else if (statusBusinessFile3 != null && statusBusinessFile3.length > 0) {
            targetPathBusiness = businessPath3;
        }




        list.clear();

        File targetDirector = new File(targetPath);
        if (targetDirector.exists()) {
            File[] allFiles = targetDirector.listFiles();
            if (allFiles != null &&  allFiles.length > 0) {
                Arrays.sort(allFiles, ((o1, o2) -> {
                    if (o1.lastModified() > o2.lastModified())
                        return -1;
                    else if (o1.lastModified() < o2.lastModified())
                        return +1;
                    else return 0;

                }));

                for (int i=0; i<allFiles.length; i++) {
                    File file = allFiles[i];
                    if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
                        model = new ImageStatus("whats" + i,
                                Uri.fromFile(file),
                                allFiles[i].getAbsolutePath(),
                                file.getName());
                        list.add(model);
                    }
                }
            }

        }


        File targetDirectorBusiness = new File(targetPathBusiness);
        if (targetDirectorBusiness.exists()) {
            File[] allFilesBusiness = targetDirectorBusiness.listFiles();
            if (allFilesBusiness != null &&  allFilesBusiness.length > 0) {
                Arrays.sort(allFilesBusiness, ((o1, o2) -> {
                    if (o1.lastModified() > o2.lastModified())
                        return -1;
                    else if (o1.lastModified() < o2.lastModified())
                        return +1;
                    else return 0;

                }));

                for (int i=0; i<allFilesBusiness.length; i++) {
                    File file = allFilesBusiness[i];
                    if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
                        model = new ImageStatus("whatsBusiness" + i,
                                Uri.fromFile(file),
                                allFilesBusiness[i].getAbsolutePath(),
                                file.getName());
                        list.add(model);
                    }
                }
            }
        }


        if (list != null && list.size() > 0) {
            handler.post(() -> {
                binding.messageTextImage.setVisibility(View.GONE);
                binding.messageTextImage.setText("");

                adapter = new ImageAdapter(list, getActivity());
                GridLayoutManager layoutManager=new GridLayoutManager(getActivity(),3);
                recyclerView.setLayoutManager(layoutManager);
                binding.recyclerViewImageId.setAdapter(adapter);
                binding.progressBarImageId.setVisibility(View.GONE);
            });

        }else {
            handler.post(() -> {
                binding.progressBarImageId.setVisibility(View.GONE);
                binding.messageTextImage.setVisibility(View.VISIBLE);
                binding.messageTextImage.setText(R.string.no_files_found);
            });
        }
        binding.imageSwipeRefreshId.setRefreshing(false);

    }

}