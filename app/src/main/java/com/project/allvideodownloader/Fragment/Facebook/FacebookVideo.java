package com.project.allvideodownloader.Fragment.Facebook;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
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

public class FacebookVideo extends Fragment {
    private Cursor videoCursor;
    private VideoAdapter adapter;
    private ArrayList<VideoStatus> videoFileArrayList = new ArrayList<>();
    FragmentVideoBinding binding;
    private final Handler handler = new Handler();

    private boolean mSearchCheck;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        binding.rvFile.setHasFixedSize(true);
        //binding.rvFile.setLayoutManager(new GridLayoutManager(getActivity(), Constant.GRID_COUNT));

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

    private void filter(String text) {
        ArrayList<VideoStatus> filteredlist = new ArrayList<VideoStatus>();
        for (VideoStatus item : videoFileArrayList) {
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
        String name = "";
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String down = path.getPath().toString();
        if (down.contains("Download")) {
            name = "/Download";
        } else if (down.contains("Downloads")) {
            name = "/Downloads";
        }

        System.gc();
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED};
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + name + "/StatusSaver/facebook/Video/" + "%"};
        videoCursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,  proj, selection, selectionArgs, MediaStore.Video.Media.DATE_ADDED + " DESC");
        videoFileArrayList = new ArrayList<>();

        if (videoCursor.moveToFirst()) {
            do {
                VideoStatus videoFile = new VideoStatus();
                String data = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String filename = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String fileSize = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String fileDuration = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String fileDate = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                videoFile.setFilename(filename);
                videoFile.setPath(data);
                videoFile.setSize(fileSize);
                videoFile.setDate(Long.parseLong(fileDate)*1000);
                videoFile.setDuration(Long.parseLong(fileDuration));
                videoFileArrayList.add(videoFile);

            } while (videoCursor.moveToNext());
        }

        if (videoFileArrayList != null && videoFileArrayList.size() > 0) {
            handler.post(() -> {
                binding.videoTextImage.setVisibility(View.GONE);
                binding.videoTextImage.setText("");

                adapter = new VideoAdapter(videoFileArrayList);
                binding.rvFile.setAdapter(adapter);
                binding.progressBarVideoId.setVisibility(View.GONE);
            });

        }else {
            handler.post(() -> {
                binding.progressBarVideoId.setVisibility(View.GONE);
                binding.videoTextImage.setVisibility(View.VISIBLE);
                binding.videoTextImage.setText(R.string.no_files_found);
            });
        }
        binding.videoSwipeRefreshId.setRefreshing(false);


    }


}