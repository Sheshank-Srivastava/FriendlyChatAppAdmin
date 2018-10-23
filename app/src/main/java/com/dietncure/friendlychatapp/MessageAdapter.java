package com.dietncure.friendlychatapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage> {

    String folder;
    ImageView photoImageView;
    TextView messageTextView, pdfImageView;
    FriendlyMessage message;

    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        pdfImageView = convertView.findViewById(R.id.pdfImageView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        final TextView timeTextView = convertView.findViewById(R.id.timeTextView);
        LinearLayout layout = convertView.findViewById(R.id.layout_item_message);
        message = getItem(position);

        pdfImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = Environment.getExternalStorageDirectory().toString() + "/androiddeft";
                boolean storedFile = false;
                Log.d("Date and Postition", message.getTime() + " And " + position);
                File directory = new File(path);
                //Create androiddeft folder if it does not exist
                String name, time = timeTextView.getText() + "";
//                Log.d("Files", "Path: " + path);
                File[] files = directory.listFiles();
                int no = 0;
                if (directory.exists()) {
                    no = files.length;
                }
//                Log.d("Files", "Size: " + no);
                for (int i = 0; i < no; i++) {
                    name = files[i].getName().replace(".pdf", "");
                    if (time.equals(name) || no == 0) {
                        storedFile = true;
                        directory = files[i];
                    }
//                    Log.d("Files", "FileName:" + name + "   " + timeTextView.getText());

                }
                if (!storedFile) {
                    time = timeTextView.getText() + "";

                    Log.d("Testing Part 1", "In Intent");
                    Intent intent = new Intent(getContext(), PdfViewerActivity.class);
                    intent.putExtra("Url", pdfImageView.getText() + "");
                    Log.d("PdfView_Url", pdfImageView.getText() + "+++++Url");
                    intent.putExtra("timeStamp", time);
                    ((Activity) getContext()).startActivity(intent);

                }
                if (storedFile) {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/androiddeft/" + directory.getName());
                    openPDF(file);
                }
            }

        });
//        boolean isPhoto = message.getPhotoUrl() != null;
//        boolean isPdf = message.getPdfUrl() != null;
//        if (isPdf) {
//            messageTextView.setVisibility(View.GONE);
//            photoImageView.setVisibility(View.VISIBLE);
//
//            Log.d("Pdf Uri", message.getPhotoUrl() + "");
//            pdfImageView.setText(message.getPdfUrl());
//        } else {
//            messageTextView.setVisibility(View.VISIBLE);
//            photoImageView.setVisibility(View.GONE);
//            messageTextView.setText(message.getText());
//        }
//        if (isPhoto) {
//            messageTextView.setVisibility(View.GONE);
//            photoImageView.setVisibility(View.VISIBLE);
//            Glide.with(photoImageView.getContext())
//                    .load(message.getPhotoUrl())
//                    .into(photoImageView);
//        } else {
//            messageTextView.setVisibility(View.VISIBLE);
//            photoImageView.setVisibility(View.GONE);
//            messageTextView.setText(message.getText());
//        }
        authorTextView.setText(message.getName());
        timeTextView.setText(message.getTime());
        viewShow(message.getTAG_VIEW());
        return convertView;
    }

    public void openPDF(File file) {
        //file should contain path of pdf file
        Log.d("File Exist File name", "file:/" + file.getAbsolutePath());
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Open File");
        getContext().startActivity(intent);
        try {
            getContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
//                    ShowToast(TAG, "Unable to open PDF. Please, install a PDF reader app.");
            Toast.makeText(getContext(), "Unable to open PDF. Please, install a PDF reader app.", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewShow(int tag) {
        /**
         * View Photo
         */
        if (tag == 1) {
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
            photoImageView.setVisibility(View.VISIBLE);
            pdfImageView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.GONE);

        }
        /**
         * View pdf
         */
        if (tag == 2) {
            pdfImageView.setText(message.getPdfUrl());
            photoImageView.setVisibility(View.GONE);
            pdfImageView.setVisibility(View.VISIBLE);
            messageTextView.setVisibility(View.GONE);


        }
        /**
         * View text
         */
        if (tag == 3) {
            messageTextView.setText(message.getText());
            photoImageView.setVisibility(View.GONE);
            pdfImageView.setVisibility(View.GONE);
            messageTextView.setVisibility(View.VISIBLE);
        }

    }

    public interface ClickListener {
        void onItemClick(View v, int position);

    }
}
