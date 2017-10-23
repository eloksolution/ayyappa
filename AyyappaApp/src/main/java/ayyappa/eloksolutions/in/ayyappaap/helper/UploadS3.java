package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;


public class UploadS3 {
    // We only need one instance of the clients and credentials provider
    private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static TransferUtility sTransferUtility;

    /**
     * Gets an instance of CognitoCachingCredentialsProvider which is
     * constructed using the given Context.
     *
     * @param context An Context instance.
     * @return A app credential provider.
     */
    public static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context,
                    "us-east-1:e022e889-e609-4216-893e-2136dc89750c", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );
        }
        return sCredProvider;
    }
    /**
     * Gets an instance of a S3 client which is constructed using the given
     * Context.
     *
     * @param context An Context instance.
     * @return A app S3 client.
     */
    public static AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context.getApplicationContext()));
        }
        return sS3Client;
    }
    /**
     * Gets an instance of the TransferUtility which is constructed using the
     * given Context
     *
     * @param context
     * @return a TransferUtility instance
     */
    public static TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context.getApplicationContext()),
                    context.getApplicationContext());
        }
        return sTransferUtility;
    }
    /*
* Begins to upload the file specified by the file path.
*/
    public static String beginUpload(String filePath,TransferUtility transferUtility,String keyName) {
        if (filePath == null) {
            return null;
        }
        System.out.println("the image is "+filePath);
        try {
            File file = new File(filePath);
            TransferObserver observer = transferUtility.upload(Config.BUCKET_NAME, keyName,file);
            observer.setTransferListener(new UploadListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyName;
    }

    public static String updateImage(String filePath,String imageName,TransferUtility transferUtility) {
        if (filePath == null) {
            // Toast.makeText(this, "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            return null;
        }
        System.out.println("the image is "+imageName);
        System.out.println("file path in update Image is :"+filePath);
        File file = new File(filePath);
        try {
            String[] image1;// = new String[2];

            String upldFile = file.getName();
            upldFile = upldFile.substring(upldFile.lastIndexOf('.'),
                    upldFile.length());

            try {
                if(imageName.contains(".")){
                    image1 = imageName.split("\\.");
                    System.out.println("imag name after spliting is :"+image1[0]+" after :"+image1[1]+" full name :"+imageName);
                    imageName = image1[0]+upldFile;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return imageName;
        } finally {
            TransferObserver observer = transferUtility.upload(Config.BUCKET_NAME, imageName,file);
 /*
  * Note that usually we set the transfer listener after initializing the
  * transfer. However it isn't required in this sample app. The flow is
  * click upload button -> start an activity for image selection
  * startActivityForResult -> onActivityResult -> beginUpload -> onResume
  * -> set listeners to in progress transfers.
  */
            observer.setTransferListener(new UploadListener());
        }
    }

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());

    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public static class UploadListener implements TransferListener {
        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
           // imageName=null;
            //Log.e(TAG, "Error during upload: " + id, e);
        }
        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
           // Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",id, bytesTotal, bytesCurrent));
        }
        @Override
        public void onStateChanged(int id, TransferState newState) {
           // Log.d(TAG, "onStateChanged: " + id + ", " + newState);
        }
    }
}
