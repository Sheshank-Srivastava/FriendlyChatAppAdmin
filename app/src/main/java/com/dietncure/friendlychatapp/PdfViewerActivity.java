package com.dietncure.friendlychatapp;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class PdfViewerActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    //    PDFView pdfView;
    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String url, time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        Intent intent = getIntent();
        url = intent.getStringExtra("Url");
        time = intent.getStringExtra("timeStamp");
        Log.d("Files", time);
//        pdfView = (PDFView) findViewById(R.id.pdfViewer);
        Log.d("PdfView Url", intent.getStringExtra("Url"));
//        pdfView.fromUri(Uri.parse(url)).load();

        Log.d("Testing Part 1", "In pdf Activity class");
        if (CheckForSDCard.isSDCardPresent()) {

            Log.d("Testing Part 1", "Is sdcard present IF statement" + CheckForSDCard.isSDCardPresent());
            //check if app has permission to write to the external storage.
            if (EasyPermissions.hasPermissions(PdfViewerActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Get the URL entered

                Log.d("Testing Part 1", "PdfActivity has permission and download file function Call");
                new DownloadFile().execute(url);
                ;

            } else {
                //If permission is not present request for the same.

                Log.d("Testing Part 1", EasyPermissions.hasPermissions(PdfViewerActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) + "     PdfActivity dosent have permission");
                EasyPermissions.requestPermissions(PdfViewerActivity.this, getString(R.string.write_file), WRITE_REQUEST_CODE, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }


        } else {
            Toast.makeText(getApplicationContext(),
                    "SD Card not found", Toast.LENGTH_LONG).show();

        }

    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        new DownloadFile().execute(url);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            Log.d("Testing Part 1", "in progress");

            super.onPreExecute();
            this.progressDialog = new ProgressDialog(PdfViewerActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            this.progressDialog.setTitle("Downloading");
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

            Log.d("Testing Part 1", "In DoIn Background");
            int count;
            try {
                Log.d("Testing Part 1", "In try block");

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = time/*new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())*/;

                //Extract file name from URL
//                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = timestamp + ".pdf";

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "androiddeft/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    Log.d("Testing Part 1", "Creating Folder With Folder Name---" + directory.getName());
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);
                Log.d("Folder+File", Environment.getExternalStorageDirectory().getAbsolutePath() + "/androiddeft/" + fileName);
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/androiddeft/" + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                            openPDF(file);

                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.d("Testing Part 1",  " In catch Block");

                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded

            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }
    public void openPDF(File file) {
        //file should contain path of pdf file
//        Log.d("File Exist File name", "file:/" + file.getAbsolutePath());
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target, "Open File");
        startActivity(intent);

//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            // Instruct the user to install a PDF reader here, or something
////                    ShowToast(TAG, "Unable to open PDF. Please, install a PDF reader app.");
//            Toast.makeText(this, "Unable to open PDF. Please, install a PDF reader app.", Toast.LENGTH_SHORT).show();
//        }
        finish();
    }
}
