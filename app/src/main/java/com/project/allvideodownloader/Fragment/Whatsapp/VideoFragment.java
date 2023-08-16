package com.project.allvideodownloader.Fragment.Whatsapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.project.allvideodownloader.Adapter.VideoAdapter;
import com.project.allvideodownloader.Model.VideoStatus;
import com.project.allvideodownloader.R;
import com.project.allvideodownloader.databinding.FragmentVideoBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class VideoFragment extends Fragment {
    private static VideoAdapter adapter;
    private static ArrayList<VideoStatus> list = new ArrayList<>();
    FragmentVideoBinding binding;
    private final Handler handler = new Handler();
    VideoStatus model;
    String size;
    Long time;
    private boolean mSearchCheck;

    String targetPath, targetPathBusiness, path1,path2,path3,businessPath1,businessPath2,businessPath3;


    private SearchView searchView;
    private MenuItem menuItem;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        //binding.rvFile.setHasFixedSize(true);

        getData();

        binding.videoSwipeRefreshId.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        //Select search item
        final MenuItem menuItem = menu.findItem(R.id.menu_search);
        menuItem.setVisible(true);
        SearchView searchView = (SearchView) menuItem.getActionView();
        Drawable d = getResources().getDrawable(R.drawable.gray_background);
        searchView.setBackground(d);
        ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        ImageView searchCloseIcon = (ImageView)searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchCloseIcon.setImageResource(R.drawable.ic_close);
        searchView.setOnQueryTextListener(onQuerySearchView);
        mSearchCheck = false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            mSearchCheck = true;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            if (mSearchCheck) {
                filter(s);
            }
            return false;
        }
    };

    public void filter(String text) {
        ArrayList<VideoStatus> filteredlist = new ArrayList<VideoStatus>();
        for (VideoStatus item : list) {
            if (item.getFilename().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(getActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            adapter.filterList(filteredlist);
        }
    }




    @SuppressLint("Range")
    private void getData() {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/";

        path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/Statuses/";
        path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Video/Sent/";
        path3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Video/Private/";

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
        businessPath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/WhatsApp Video/Sent/";
        businessPath3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp Business/Media/WhatsApp Video/Private/";

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

            //assert allFiles != null;
            if (allFiles != null &&  allFiles.length > 0) {
                Arrays.sort(allFiles, ((o1, o2) -> {
                    if (o1.lastModified() > o2.lastModified())
                        return -1;
                    else if (o1.lastModified() < o2.lastModified())
                        return +1;
                    else return 0;

                }));

                for (int i = 0; i < allFiles.length; i++) {
                    File file = allFiles[i];

                    if (Build.VERSION.SDK_INT >= 14) {
                        try {
                            retriever.setDataSource(getActivity(), Uri.fromFile(file));
                            time = Long.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                            //retriever.release();
                        }catch (RuntimeException ex) {
                            //throw new RuntimeException(e);
                        }
                    }

                    size = String.valueOf(allFiles[i].length());
                    Long lastModifiedDate = file.lastModified();
                    //DateFormat.format("dd/MM/yyyy", new Date(new File(file.getPath()).lastModified())).toString();

                    if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                        model = new VideoStatus("whats" + i,
                                Uri.fromFile(file.getAbsoluteFile()),
                                allFiles[i].getAbsolutePath(),
                                allFiles[i].getName(),
                                time,
                                size,
                                lastModifiedDate);
                        list.add(model);
                    }
                }

            }
        }





        File targetDirectorBusiness = new File(targetPathBusiness);
        if (targetDirectorBusiness.exists()){
            File[] allFilesBusiness = targetDirectorBusiness.listFiles();
            //assert allFilesBusiness != null;
            if (allFilesBusiness != null &&  allFilesBusiness.length > 0) {
                Arrays.sort(allFilesBusiness, ((o1, o2) -> {
                    if (o1.lastModified() > o2.lastModified())
                        return -1;
                    else if (o1.lastModified() < o2.lastModified())
                        return +1;
                    else return 0;

                }));

                for (int i = 0; i < allFilesBusiness.length; i++) {
                    File file = allFilesBusiness[i];

                    if (Build.VERSION.SDK_INT >= 14) {
                        try {
                            retriever.setDataSource(getActivity(), Uri.fromFile(file));
                            time = Long.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                        } catch (RuntimeException ex) {
                            // something went wrong with the file, ignore it and continue
                        }
                    }

                    size = String.valueOf(allFilesBusiness[i].length());
                    Long lastModifiedDate = file.lastModified();

                    if (Uri.fromFile(file).toString().endsWith(".mp4")) {
                        model = new VideoStatus("whats" + i,
                                Uri.fromFile(file.getAbsoluteFile()),
                                allFilesBusiness[i].getAbsolutePath(),
                                allFilesBusiness[i].getName(),
                                time,
                                size,
                                lastModifiedDate);
                        list.add(model);
                    }


                }

            }

        }




        if (list != null && list.size() > 0) {
            handler.post(() -> {
                binding.videoTextImage.setVisibility(View.GONE);
                binding.videoTextImage.setText("");

                adapter = new VideoAdapter(list);
                binding.rvFile.setAdapter(adapter);
                binding.progressBarVideoId.setVisibility(View.GONE);
            });

        } else {
            handler.post(() -> {
                binding.progressBarVideoId.setVisibility(View.GONE);
                binding.videoTextImage.setVisibility(View.VISIBLE);
                binding.videoTextImage.setText(R.string.no_files_found);
            });
        }
        binding.videoSwipeRefreshId.setRefreshing(false);
    }
}