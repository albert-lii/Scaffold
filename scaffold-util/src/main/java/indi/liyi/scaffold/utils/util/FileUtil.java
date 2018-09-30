package indi.liyi.scaffold.utils.util;


import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class FileUtil {
    private final static String TAG = "Scaffol-" + FileUtil.class.getSimpleName();

    public static FileUtil getInstance() {
        return FileUtilHolder.INSTANCE;
    }

    private static class FileUtilHolder {
        private static final FileUtil INSTANCE = new FileUtil();
    }

    /**
     * Return whether it is a file
     *
     * @param filePath File path
     * @return {@code true}: yes <br> {@code false}: no
     */
    public static boolean isFile(String filePath) {
        return isFile(new File(filePath));
    }

    /**
     * Return whether it is a file
     *
     * @param file File
     * @return {@code true}: yes <br> {@code false}: no
     */
    public static boolean isFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return file.isFile();
    }

    /**
     * Return whether it is a folder
     *
     * @param dirPath File path
     * @return {@code true}: yes <br>{ @code false}: no
     */
    public boolean isDir(String dirPath) {
        return isDir(new File(dirPath));
    }

    /**
     * Return whether it is a folder
     *
     * @param dir File
     * @return {@code true}: yes <br> {@code false}: no
     */
    public boolean isDir(File dir) {
        if (dir == null || !dir.exists()) {
            return false;
        }
        return dir.isDirectory();
    }


    /**
     * Create a folder
     * (You must create a folder before you create a file. Otherwise, it will report "can't find a path")
     *
     * @param path
     * @return {@code true}: success <br> {@code false}: fail
     */
    public boolean createDir(String path) {
        if (TextUtils.isEmpty(path)) return false;
        boolean isSuccess;
        File file = new File(path);
        if (!file.exists()) {
            // If the folder already exists, executing this method will return false
            isSuccess = file.mkdirs();
        } else {
            isSuccess = true;
        }
        return isSuccess;
    }

    /**
     * Create a file
     *
     * @param path
     * @return {@code true}: success <br> {@code false}: fail
     */
    public boolean createFile(String path) {
        if (TextUtils.isEmpty(path)) return false;
        boolean isSuccess = false;
        File file = new File(path);
        if (!file.exists()) {
            try {
                // If the folder already exists, executing this method will return false
                isSuccess = file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            isSuccess = true;
        }
        return isSuccess;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////  IO Operation
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Save string data
     *
     * @param key   The path of the file that holds the string data
     * @param value String data
     * @throws IOException
     */
    public void put(@NonNull String key, String value) {
        File file = new File(key);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read string data
     *
     * @param key
     * @return
     */
    public String getAsString(@NonNull String key) {
        File file = new File(key);
        if (!file.exists()) {
            return null;
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine;
            }
            return readString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Save byte data
     *
     * @param key
     * @param value
     */
    public void put(@NonNull String key, byte[] value) {
        File file = new File(key);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read byte data
     *
     * @param key
     * @return
     */
    public byte[] getAsBinary(@NonNull String key) {
        File file = new File(key);
        if (!file.exists()) {
            return null;
        }
        byte[] byteArray = null;
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                baos.write(b, 0, n);
            }
            byteArray = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }

    /**
     * Save serialized object
     *
     * @param key
     * @param value
     */
    public void put(@NonNull String key, Serializable value) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            put(key, data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read serialized object
     *
     * @param key
     * @return
     */
    public Object getAsObject(@NonNull String key) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            byte[] data = getAsBinary(key);
            if (data == null || data.length == 0) return null;
            bais = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(bais);
            Object obj = ois.readObject();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bais != null) {
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Save bitmap
     *
     * @param key
     * @param value
     */
    public void put(@NonNull String key, Bitmap value) {
        put(key, bitmap2Byte(value));
    }

    /**
     * Read bitmap data
     *
     * @param key
     * @return
     */
    public Bitmap getAsBitmap(@NonNull String key) {
        if (getAsBinary(key) == null) return null;
        return byte2Bitmap(getAsBinary(key));
    }

    private byte[] bitmap2Byte(Bitmap bmp) {
        if (bmp == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private Bitmap byte2Bitmap(byte[] bytes) {
        if (bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////  Copy files and folders to the specified path
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Copy the file to the specified path
     *
     * @param oldPath The original path of the copied file
     * @param newPath The specified path to which the file was copied
     */
    public void copyFile(@NonNull String oldPath, @NonNull String newPath) {
        int bytesum = 0;
        int byteread = 0;
        File oldfile = new File(oldPath);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        if (oldfile.exists()) {
            try {
                fis = new FileInputStream(oldPath);
                fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = fis.read(buffer)) != -1) {
                    bytesum += byteread;
                    fos.write(buffer, 0, byteread);
                }
                LogUtil.d(TAG, "Copy file success, the total size of the file is ======> " + bytesum + " byte");
            } catch (IOException e) {
                LogUtil.e(TAG, "Copy file error ======> " + e.toString());
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            LogUtil.e(TAG, "Copy file error ======> source file does not exist!");
        }
    }

    /**
     * Copy the folder to the specified path
     *
     * @param oldPath
     * @param newPath
     */
    public void copyDir(@NonNull String oldPath, @NonNull String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File oldDir = new File(oldPath);
            String[] fileNameList = oldDir.list();
            File temp = null;
            for (int i = 0; i < fileNameList.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + fileNameList[i]);
                } else {
                    temp = new File(oldPath + File.separator + fileNameList[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = null;
                    if (newPath.endsWith(File.separator)) {
                        output = new FileOutputStream(newPath + (temp.getName()).toString());
                    } else {
                        output = new FileOutputStream(newPath + File.separator + (temp.getName()).toString());
                    }
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                } else {
                    String op = null, np = null;
                    if (oldPath.endsWith(File.separator)) {
                        op = oldPath + fileNameList[i];
                    } else {
                        op = oldPath + File.separator + fileNameList[i];
                    }
                    if (newPath.endsWith(File.separator)) {
                        np = newPath + fileNameList[i];
                    } else {
                        np = newPath + File.separator + fileNameList[i];
                    }
                    copyDir(op, np);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the number of files in the specified folder
     *
     * @param dir   The path of specified folder
     * @param isAll {@code true}: Get the number of all the files <br> {@code false}: Only get the number of files at the first level folder
     * @return The number of files
     */
    public int getFileCount(String dir, boolean isAll) {
        if (TextUtils.isEmpty(dir)) return 0;
        int count = 0;
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return count;
        }
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                count += 1;
            } else {
                if (isAll) {
                    getFileCount(files[i].getAbsolutePath(), true);
                }
            }
        }
        return count;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////  Delete files
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Delete a file or a folder
     *
     * @param path The path of a file or folder
     * @return {@code true}: success <br>{ @code false}: fail
     */
    public boolean delete(String path) {
        return TextUtils.isEmpty(path) ? true : delete(new File(path));
    }

    /**
     * Delete a file or a folder
     *
     * @param file A file or folder
     * @return {@code true}: success <br> {@code false}: fail
     */
    public boolean delete(File file) {
        if (file == null) return true;
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    return deleteFile(file);
                } else {
                    return deleteDir(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Failed to delete file ======> " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete a file
     *
     * @param path
     * @return {@code true}: success <br> {@code false}: fail
     */
    public boolean deleteFile(String path) {
        return TextUtils.isEmpty(path) ? true : deleteFile(new File(path));
    }

    /**
     * Delete a file
     *
     * @param file
     * @return {@code true}: success <br> {@code false}: fail
     */
    public boolean deleteFile(File file) {
        if (file != null && file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * Delete a folder
     *
     * @param dir
     * @return {@code true}: success <br> {@code false}: fail
     */
    public boolean deleteDir(String dir) {
        if (TextUtils.isEmpty(dir)) return true;
        // Add a file separator at the end of the file path automatically if the folder path does not end with a file separator
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        return deleteDir(new File(dir));
    }

    /**
     * Delete a folder
     *
     * @param fileDir
     * @return {@code true}: success <br> {@code false}: fail
     */
    public boolean deleteDir(File fileDir) {
        if (fileDir == null || !fileDir.exists() || !fileDir.isDirectory()) {
            return true;
        }
        boolean isSuccess = true;
        File[] files = fileDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // If it is a file, delete the file
            if (files[i].isFile()) {
                isSuccess = deleteFile(files[i]);
                if (!isSuccess) break;
            }
            // It it is a folder, call the method of deleting folders
            else if (files[i].isDirectory()) {
                isSuccess = deleteDir(files[i]);
                if (!isSuccess) break;
            }
        }
        if (!isSuccess) return false;
        return fileDir.delete();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////  Get the size of a file or folder
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the size of the specified file or folder
     *
     * @param path
     * @return
     */
    public long getFileSize(String path) {
        return TextUtils.isEmpty(path) ? 0 : getFileSize(new File(path));
    }

    /**
     * Get the size of the specified file or folder
     *
     * @param file
     * @return
     */
    public long getFileSize(File file) {
        if (file == null) return 0;
        long size = 0;
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    size += getSingleFileSize(file);
                } else {
                    size += getFileDirSize(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Failed to get the specified file size ======> " + e.getMessage());
        }
        return size;
    }

    /**
     * Get the size of the specified file
     *
     * @param path
     * @return
     */
    public long getSingleFileSize(String path) {
        return TextUtils.isEmpty(path) ? 0 : getSingleFileSize(new File(path));
    }

    /**
     * Get the size of the specified file
     *
     * @param file
     * @return
     * @throws Exception
     */
    public long getSingleFileSize(File file) {
        if (file == null) return 0;
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                size = fis.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * Get the size of the specified folder
     *
     * @param dir
     * @return
     */
    public long getFileDirSize(String dir) {
        return TextUtils.isEmpty(dir) ? 0 : getFileDirSize(new File(dir));
    }

    /**
     * Get the size of the specified folder
     *
     * @param dir
     * @return
     */
    public long getFileDirSize(File dir) {
        if (dir == null) return 0;
        long size = 0;
        File flist[] = dir.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileDirSize(flist[i]);
            } else {
                size = size + getSingleFileSize(flist[i]);
            }
        }
        return size;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////  Get the real path of the file
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param uri The Uri to query.l
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getRealPath(@NonNull Context context, @NonNull Uri uri) {
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
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(@NonNull Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
