package com.project.allvideodownloader.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.allvideodownloader.Model.ImageStatus;
import com.project.allvideodownloader.R;
import com.project.allvideodownloader.View.ImageDetailsActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{
    private boolean multiSelect = false;
    private ArrayList<Integer> selectedItems = new ArrayList<Integer>();
    private ArrayList<ImageStatus> imagesList;
    Context context;
    ActionMode mActionMode;
    ArrayList<Uri> files = new ArrayList<Uri>();

    boolean check = false;


    public ImageAdapter(ArrayList<ImageStatus> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mActionMode = mode;
            multiSelect = true;
            mode.getMenuInflater().inflate(R.menu.gallery_menu, menu);
            //menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                Collections.sort(selectedItems, Collections.reverseOrder());
                for (Integer intItem : selectedItems) {
                    deleteItem(intItem);
                }
                mode.finish();

            } else if (item.getItemId() == R.id.action_share) {
                for (Integer intItem : selectedItems) {
                    files.add(Uri.parse(imagesList.get(intItem).getPath()));
                }
                Intent shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                shareIntent.setType("image/*");
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                context.startActivity(Intent.createChooser(shareIntent , "Share image"));
                mode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
            check = false;
        }
    };

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.update(position);


    }

    public void deleteItem(int position) {
        String path = imagesList.get(position).getPath();
        File file = new File(path);
        try {
            if (file.exists()) {
                boolean del = file.delete();
                imagesList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, imagesList.size());
                notifyDataSetChanged();
                if (del) {
                    MediaScannerConnection.scanFile(
                            context,
                            new String[]{path, path},
                            new String[]{"image/*"}, //new String[]{"image/jpg", "image/png"},
                            new MediaScannerConnection.MediaScannerConnectionClient() {
                                public void onMediaScannerConnected() {
                                }

                                @Override
                                public void onScanCompleted(String s, Uri uri) {
                                    Log.d("Video path: ", s);
                                }
                            });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        CardView relativeLayout;
        ImageView imageView, imgCheck;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCheck = itemView.findViewById(R.id.imgCheckId);
            imageView = itemView.findViewById(R.id.imageViewId);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutViewId);
        }
        void selectItem(Integer item) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    imgCheck.setVisibility(View.GONE);
                } else {
                    selectedItems.add(item);
                    imgCheck.setVisibility(View.VISIBLE);
                }
                mActionMode.setTitle(selectedItems.size() + " Selected");
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        void update(final Integer value) {
            ImageStatus files = imagesList.get(value);
            Glide.with(context).load(files.getPath()).into(imageView);
            //Glide.with(context).load(video.getPath()).skipMemoryCache(false).into(imageView);

            if (selectedItems.contains(value)) {
                imgCheck.setVisibility(View.VISIBLE);
            } else {
                imgCheck.setVisibility(View.GONE);
            }



            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    check = true;
                    ((AppCompatActivity)view.getContext()).startSupportActionMode(actionModeCallbacks);
                    selectItem(value);
                    return true;
                }
            });


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (check) {
                        selectItem(value);
                    }else {
                        try {
                            Intent intent = new Intent(context, ImageDetailsActivity.class);
                            intent.putParcelableArrayListExtra("data", imagesList);
                            intent.putExtra("ImagePosition", value);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Log.d("Tag", "Message" + e.getLocalizedMessage());
                        }
                    }
                }
            });


        }
    }
}
