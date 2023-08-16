package com.project.allvideodownloader.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.project.allvideodownloader.Model.VideoStatus;
import com.project.allvideodownloader.R;
import com.project.allvideodownloader.View.VideoPlayerActivity;
import com.project.allvideodownloader.databinding.ItemVideoBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{
    private ArrayList<VideoStatus> videoFileArrayList;
    private Context context;


    public VideoAdapter(ArrayList<VideoStatus> videoFileArrayList) {
        this.videoFileArrayList = videoFileArrayList;
    }

    public void filterList(ArrayList<VideoStatus> filterlist) {
        videoFileArrayList = filterlist;
        notifyDataSetChanged();
    }

    public void update(List<VideoStatus> data) {
        this.videoFileArrayList.clear();
        this.videoFileArrayList.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_video, parent, false);
        context = parent.getContext();
        return new VideoViewHolder(binding);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final VideoStatus file = videoFileArrayList.get(position);
        //Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Capture_it.ttf");
        //holder.binding.tvTitle.setTypeface(typeface);
        holder.binding.tvTitle.setText(file.getFilename());

        if (file.getDate() != null && file.getDuration() != null) {
            holder.binding.videoDate.setText(getDate(file.getDate()));
            holder.binding.duration.setText(convertMillieToHMmSs(file.getDuration()));
        }

        Glide.with(context)
                .load(Uri.fromFile(new File(file.getPath())))
                .placeholder(R.drawable.video_play)
                .error(R.drawable.video_play)
                .into(holder.binding.videoThumb);

        long fileSize = (Long.parseLong(file.getSize()) / 1024);
        float mbSize = fileSize / 1024;
        float gbSize = mbSize / 1024;
        if (gbSize > 1) {
            holder.binding.tvSize.setText(String.format("%.2f", gbSize).replace(".00", "") + " GB");
        } else if (mbSize > 1)
            holder.binding.tvSize.setText(String.format("%.2f", mbSize).replace(".00", "") + " MB");
        else
            holder.binding.tvSize.setText(fileSize + " KB");



        holder.binding.videoOnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("VideoPath", file.getPath());
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.d("Tag", "Message" + e.getLocalizedMessage());
                }
            }
        });

        holder.binding.videoOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.inflate(R.menu.video_menu);
                /*Menu menu = popupMenu.getMenu();
                if (type == 0) {
                    menu.findItem(R.id.menu_v_delete).setTitle("Remove");
                }*/


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.menu_open_with) {
                            passVideo(file.getPath());
                            return true;
                        } else if (menuItem.getItemId() == R.id.menu_rename) {
                            Rename(position);
                            return true;
                        }else if (menuItem.getItemId() == R.id.menu_share) {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("video/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getPath()));
                            context.startActivity(Intent.createChooser(shareIntent, "Share image"));
                            return true;
                        }else if (menuItem.getItemId() == R.id.menu_delete) {
                            delete(position);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void passVideo(String path){
        final File videoFile = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(videoFile), "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static String convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);  //time*1000
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public int getItemCount() {
        return videoFileArrayList.size();
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private ItemVideoBinding binding;
        public VideoViewHolder(@NonNull ItemVideoBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }

    @SuppressLint("ResourceAsColor")
    private void delete(int position) {
        final String path = videoFileArrayList.get(position).getPath();
        final String fileName = videoFileArrayList.get(position).getFilename();
        final File file = new File(path);

        long fileSize = (Long.parseLong(videoFileArrayList.get(position).getSize()) / 1024);
        float mbSize = fileSize / 1024;
        float gbSize = mbSize / 1024;
        String size;
        if (gbSize > 1) {
            size = String.format("%.2f", gbSize).replace(".00", "") + " GB";
        } else if (mbSize > 1)
            size = String.format("%.2f", mbSize).replace(".00", "") + " MB";
        else
            size = fileSize + " KB";


        String message = "Deleting the items below will permanently remove them from your device.\n\n" +"Files\n"+ fileName+" ("+size+")";


        ForegroundColorSpan foregroundColorSpan2 = new ForegroundColorSpan(Color.RED);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(message);
        ssBuilder.setSpan(foregroundColorSpan2, 73, 78, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        builder1.setTitle("Delete items permanently?");
        builder1.setMessage(ssBuilder);
        builder1.setCancelable(true);


        //builder1.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.dialog_background));

        builder1.setPositiveButton(
                "Delete",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if (file.exists()) {
                                boolean del = file.delete();
                                videoFileArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, videoFileArrayList.size());
                                notifyDataSetChanged();
                                if (del) {
                                    MediaScannerConnection.scanFile(
                                            context,
                                            new String[]{path, path},
                                            new String[]{"video/*", "audio/*"},  //new String[]{"video/*", "video/mp4"},
                                            new MediaScannerConnection.MediaScannerConnectionClient() {
                                                public void onMediaScannerConnected() {
                                                }

                                                public void onScanCompleted(String path, Uri uri) {
                                                    Log.d("Video path: ", path);
                                                }
                                            });
                                }
                            }
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert11 = builder1.create();
        //builder1.create().show();
        alert11.show();

    }

    private void Rename(int pos) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.rename_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextInputEditText editText = dialog.findViewById(R.id.renameEditTextId);
        MaterialButton renameCancel = dialog.findViewById(R.id.renameCancelId);
        MaterialButton renameButton = dialog.findViewById(R.id.renameButtonId);
        final File file = new File(videoFileArrayList.get(pos).getPath());
        String nameText = file.getName();
        nameText = nameText.substring(0,nameText.lastIndexOf("."));
        editText.setText(nameText);
        editText.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        renameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onlyPath = file.getParentFile().getAbsolutePath();
                String ext = file.getAbsolutePath();
                ext = ext.substring(ext.lastIndexOf("."));
                String newPath = onlyPath+"/"+editText.getText().toString()+ext;
                File newFile = new File(newPath);
                boolean rename = file.renameTo(newFile);
                if(rename) {
                    ContentResolver resolver = context.getApplicationContext().getContentResolver();
                    resolver.delete(
                            MediaStore.Files.getContentUri("external")
                            , MediaStore.MediaColumns.DATA + "=?", new String[] { file.getAbsolutePath() });
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(newFile));
                    context.getApplicationContext().sendBroadcast(intent);
                    Log.d("Tag", "Rename Message" + "Successfully !");
                }else {
                    Toast.makeText(context, "Rename failed", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        notifyDataSetChanged();
        dialog.show();
    }

}
