package com.r2.scau.moblieofficing.fragement;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.gson.GsonPhoto;
import com.r2.scau.moblieofficing.retrofit.IPhotoBiz;
import com.r2.scau.moblieofficing.untils.BitmapToRound_Util;
import com.r2.scau.moblieofficing.untils.DateUtil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 嘉进 on 9:23.
 * 底部导航栏中我的Fragment
 */

public class UserInfoFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Uri imageUri;
    private Toolbar mToolbar;
    private Context mContext;
    private TextView titleTV;
    private ImageView photoIV;
    // 头像图片 path
    private String mCurrentPhotoPath = null;
    private RelativeLayout mRelativeLayout;
    private BitmapToRound_Util round_Util = new BitmapToRound_Util();

    public static final int ALBUM_REQUEST = 100;
    public static final int CAMERA_REQUEST = 200;
    public static final int CROP_REQUEST = 300;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        mContext = getActivity();
        initView();
        return view;
    }


    public void initView() {
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.layout_user_photo);
        photoIV = (ImageView) view.findViewById(R.id.iv_user_photo);
        titleTV = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mRelativeLayout.setOnClickListener(this);
        mToolbar.setTitle("");
        titleTV.setText("我的");
    }

    public void createDialog() {
        final String items[] = {"拍照", "从相册中选择"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("设置头像");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    case 0:
                        getPortraitFromCamera();
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, ALBUM_REQUEST);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ALBUM_REQUEST:
                Log.e("start", "imageCrop");
                if (data != null) {
                    imageCrop(data.getData());
                }
                break;
            case CAMERA_REQUEST:
                /**
                 * 获得外部存储目录
                 */
                imageCrop(imageUri);
                break;
            case CROP_REQUEST:
                if (imageUri != null) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(imageUri));
                        bitmap = round_Util.toRoundBitmap(bitmap);
                        photoIV.setImageBitmap(bitmap);
                        upLoadPhoto(new File(imageUri.getPath()));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
        }

    }

    private void getPortraitFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();//创建临时图片文件
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI;
                // 兼容 Android 7.x 以上
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    photoURI = Uri.fromFile(photoFile);
                } else {
                    //FileProvider 是一个特殊的 ContentProvider 的子类，
                    //它使用 content:// Uri 代替了 file:/// Uri. ，更便利而且安全的为另一个app分享文件
                    photoURI = FileProvider.getUriForFile(mContext,
                            "com.r2.scau.moblieofficing.fileprovider",
                            photoFile);

                }
                imageUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }
    /**
     * 创建临时图片文件
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //创建临时文件,文件前缀不能少于三个字符,后缀如果为空默认未".tmp"
        File image = File.createTempFile(
                imageFileName,  /* 前缀 */
                ".jpg",         /* 后缀 */
                storageDir      /* 文件夹 */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public void imageCrop(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        //是否返回数据  false会保存图片

        imageUri = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/"
                + DateUtil.currentDatetime()  + UserUntil.phone + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_REQUEST);
        Log.e("event", "false1");

    }



    public void upLoadPhoto(File file){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_IP + "/fileServer/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestBody photo = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        /*Map<String, RequestBody> photos = new HashMap<>();
        photos.put("fileName",  RequestBody.create(null, file.getName()));
        photos.put("userPhone",  RequestBody.create(null, UserUntil.phone));
        photos.put("file\"; filename=\"" + file.getName(), photo);*/
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), photo);

        RequestBody fileName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), file.getName());

        RequestBody userPhone =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), UserUntil.phone);

        IPhotoBiz iPhotoBiz = retrofit.create(IPhotoBiz.class);
        Call<GsonPhoto> call = iPhotoBiz.upLoadPhoto(fileName, userPhone, body);
        call.enqueue(new Callback<GsonPhoto>() {
            @Override
            public void onResponse(Call<GsonPhoto> call, Response<GsonPhoto> response) {
                Log.e("upLoadPhoto", response.toString());
                GsonPhoto gsonPhoto= response.body();
                if (gsonPhoto != null && gsonPhoto.getCode() == 200){
                    Log.e("upLoadPhoto", gsonPhoto.getPath());
                }else {
                    Log.e("upLoadPhoto", response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<GsonPhoto> call, Throwable t) {
                Log.e("upLoadPhoto", t.toString());
            }
        });

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_user_photo:
                if ((ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    createDialog();
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                createDialog();
            } else {
                // Permission Denied
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
