package com.ocse.hse.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ocse.hse.Models.ContactInfo;
import com.ocse.hse.Models.RecordInfo;
import com.ocse.hse.R;
import com.ocse.hse.app.AppLog;
import com.ocse.hse.app.ApplicationConstants;
import com.ocse.hse.app.ApplicationController;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class EditRecordActivity extends Activity {

    ActionBar actionBar;
    Button btnCapture,btnShowGallery;
    RecordInfo recordInfo;
    TextView txtPicTotal;
    ImageView imgPreview1,imgPreview2,imgPreview3,imgPreview4,imgPreview5;
    FrameLayout imgContainer1,imgContainer2,imgContainer3,imgContainer4,imgContainer5;
    EditText txtCreated,txtDescription;
    AutoCompleteTextView txtContact,txtPhone;

    ArrayList<String> imageNameList;
    ArrayList<String> imageFilePathList;
    ArrayList<Bitmap> imageBitmaps;
    ArrayList<ImageView> imageviews;
    ArrayList<FrameLayout> imageContainers;

    static final String DIR_RECORD_PREVIEW="records/preview";
    static final String DIR_RECORD_CONTENT="records/content";
    Bitmap bitmap;
    String filePath;
    private Uri fileUri;
    private static final int GALLERY_ACTIVITY_REQUEST_CODE = 200;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;


    String record_id,record_task_id,record_organ_id,record_description,record_contact,record_phone,record_created,record_updated;

    ArrayList<String> imageList;
    ArrayList<ContactInfo> contactList;
    ArrayList<String> contactNameList;
    ArrayList<String> contactPhoneList;
    Boolean recordSaved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        actionBar=getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.icon_hse_actionbar);
        actionBar.setTitle("编辑隐患信息");

        imageNameList=new ArrayList<String>();
        recordInfo=null;
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            recordInfo= (RecordInfo) bundle.getSerializable(ApplicationConstants.APP_BUNDLE_RECORD);
            imageNameList=recordInfo.getImagePathList();//image name list
        }
        txtCreated=(EditText)findViewById(R.id.txtCreated);
        txtDescription=(EditText)findViewById(R.id.txtDescription);
        txtContact=(AutoCompleteTextView)findViewById(R.id.txtContact);
        txtPhone=(AutoCompleteTextView)findViewById(R.id.txtPhone);


        txtPicTotal=(TextView)findViewById(R.id.txtPicTotal);

        imgContainer1=(FrameLayout)findViewById(R.id.imgContainer1);
        imgContainer2=(FrameLayout)findViewById(R.id.imgContainer2);
        imgContainer3=(FrameLayout)findViewById(R.id.imgContainer3);
        imgContainer4=(FrameLayout)findViewById(R.id.imgContainer4);
        imgContainer5=(FrameLayout)findViewById(R.id.imgContainer5);
        imageContainers=new ArrayList<FrameLayout>();
        imageContainers.add(imgContainer1);
        imageContainers.add(imgContainer2);
        imageContainers.add(imgContainer3);
        imageContainers.add(imgContainer4);
        imageContainers.add(imgContainer5);

        imgPreview1=(ImageView)findViewById(R.id.imgPreview1);
        imgPreview2=(ImageView)findViewById(R.id.imgPreview2);
        imgPreview3=(ImageView)findViewById(R.id.imgPreview3);
        imgPreview4=(ImageView)findViewById(R.id.imgPreview4);
        imgPreview5=(ImageView)findViewById(R.id.imgPreview5);
        imageviews=new ArrayList<ImageView>();
        imageviews.add(imgPreview1);
        imageviews.add(imgPreview2);
        imageviews.add(imgPreview3);
        imageviews.add(imgPreview4);
        imageviews.add(imgPreview5);

        imageBitmaps=new ArrayList<Bitmap>();
        imageFilePathList=new ArrayList<String>();
        imageList=new ArrayList<String>();
        fillContent();

        //Button
        btnShowGallery=(Button)findViewById(R.id.btnShowGallery);
        btnShowGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBitmaps.size()==5)
                {
                    showMessage("图片","图片数量上限为5张");
                }
                else {
                    selectFromGallery();
                }
            }
        });
        btnCapture=(Button)findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageBitmaps.size()==5)
                {
                    showMessage("图片","图片数量上限为5张");
                }
                else {
                    captureImageFromCamera();
                }
            }
        });
    }
    private void showMessage(String title,String message)
    {
        AlertDialog dialog= new AlertDialog.Builder(this).setNeutralButton("确定", null).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //其实这里什么都不要做 解决读取相机拍照后Activity旋转的问题
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==android.R.id.home)
        {
            quitActivity();
            return true;
        }
        if (id == R.id.action_add_save) {
            saveRecord();
            return true;
        }
        if(id==R.id.action_add_cancel)
        {
            quitActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                quitActivity();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void quitActivity()
    {
        finish();
        overridePendingTransition(R.anim.in_just_show, R.anim.out_push_left_to_right);
    }

    private void fillContent()
    {
        record_id="";
        record_task_id="";
        record_organ_id="";
        record_description="";
        record_contact="";
        record_phone="";
        record_created="";
        if(recordInfo!=null)
        {
            record_id=recordInfo.getRecordID();
            record_task_id=recordInfo.getTaskID();
            record_organ_id=recordInfo.getOrganID();
            record_description=recordInfo.getDescription();
            record_contact=recordInfo.getContact();
            record_phone=recordInfo.getPhone();
            record_created=recordInfo.getCreated();
        }
        txtCreated.setText(record_created);
        txtDescription.setText(record_description);
        txtContact.setText(record_contact);
        txtPhone.setText(record_phone);

        listSavedPreviewImages();
        loadImagePathList();
        loadImagesToPreview();
    }
    private void loadImagePathList()
    {
        //imageFilepathList
        int total=imageNameList.size();
        if(total==0)
        {
            AppLog.i("No Saved Content Images");
        }
        for(int i=0;i<total;i++)
        {
            File file= ApplicationController.getFile(DIR_RECORD_CONTENT, imageNameList.get(i));
            imageFilePathList.add(file.getAbsolutePath());
        }
    }
    private void listSavedPreviewImages()
    {

        int total=imageNameList.size();
        if(total==0)
        {
            AppLog.i("No Saved Preview Images");
        }
        for(int i=0;i<total;i++)
        {
            File file= ApplicationController.getFile(DIR_RECORD_PREVIEW, imageNameList.get(i));
            Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath().toString());
            imageBitmaps.add(bitmap);
        }
    }
    private void loadImagesToPreview()
    {
        int total=imageBitmaps.size();
        for(int i=0;i<total;i++)
        {
            imageContainers.get(i).setVisibility(View.VISIBLE);
            imageviews.get(i).setImageBitmap(imageBitmaps.get(i));
        }
        for(int i=4;i>=total;i--)
        {
            imageContainers.get(i).setVisibility(View.GONE);
            imageviews.get(i).setImageBitmap(null);
        }
        txtPicTotal.setText(total+"张");
    }

    public void onImageDetail(View view)
    {
        AppLog.i("Detail Image:"+view.getTag().toString());

        final int imgIndex=Integer.parseInt(view.getTag().toString());

    }
    public void onRemove(View view)
    {
        AppLog.i("Remove Button:"+view.getTag().toString());

        final int imgIndex=Integer.parseInt(view.getTag().toString());
        AlertDialog dialog= new AlertDialog.Builder(this).setPositiveButton("删除",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                imageBitmaps.remove(imgIndex);
                imageNameList.remove(imgIndex);
                imageFilePathList.remove(imgIndex);
                loadImagesToPreview();
            }
        }).setNeutralButton("取消", null).create();

        dialog.setTitle("图片");
        dialog.setMessage("删除本图片？");
        dialog.show();

    }
    //Edit
    private void selectFromGallery()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_ACTIVITY_REQUEST_CODE);
    }
    private void captureImageFromCamera()
    {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
            {
                if (resultCode == Activity.RESULT_OK) {
                    // Image captured and saved to fileUri specified in the Intent
                    //Toast.makeText(this, "Image saved to:\n" +data.getData(), Toast.LENGTH_LONG).show();
                    AppLog.i("Camera:"+"Image saved to:\n" +data.getData());
                    Uri selectedImageUri=data.getData();
                    loadImage(selectedImageUri);

                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                    AppLog.i("Camera:"+"Image capture canceled");
                } else {
                    // Image capture failed, advise user
                    AppLog.i("Camera:"+"Image capture failed");
                }
                break;
            }
            case GALLERY_ACTIVITY_REQUEST_CODE:
                AppLog.i("result code" + resultCode + "  result   " + Activity.RESULT_OK);
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    AppLog.i("Gallery:Image from "+data.getData());
                    loadImage(selectedImageUri);
                }
                else if(resultCode==RESULT_CANCELED){

                }else{

                }
                break;
            default:
        }
    }
    private void loadImage(Uri selectedImageUri)
    {
        try {
            // OI FILE Manager
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            filePath = picturePath;
            AppLog.i("filePath:"+filePath);
            if (filePath != null) {
                decodeFile(filePath);
            } else {
                bitmap = null;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Internal error",
                    Toast.LENGTH_LONG).show();
            AppLog.e(e.getMessage());
        }
    }
    private void decodeFile(String filePath) {

        // Decode image size
        String fileName="";
        try {
            fileName = new File(filePath).getName();
        }catch (Exception e){}
        imageFilePathList.add(filePath);
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 512;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        AppLog.i("width:"+width_tmp+" height:"+height_tmp);
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        try
        {
            ExifInterface exifInterface = new ExifInterface(filePath);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            AppLog.i("orientation:"+orientation);

            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                matrix.postRotate(90);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                matrix.postRotate(180);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                matrix.postRotate(270);
            }

            // Rotate the bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch (Exception exception)
        {
            AppLog.d("Could not rotate the image");
        }
        AppLog.i("resized width:"+bitmap.getWidth()+" height:"+bitmap.getHeight());
        imageBitmaps.add(bitmap);
        loadImagesToPreview();
    }

    private void saveOriginalImage(File output,String filePath)
    {
        String fileName="";
        try {
            fileName = new File(filePath).getName();
        }catch (Exception e){}

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        AppLog.i("width:"+width_tmp+" height:"+height_tmp);
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        try
        {
            ExifInterface exifInterface = new ExifInterface(filePath);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            AppLog.i("orientation:"+orientation);

            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                matrix.postRotate(90);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                matrix.postRotate(180);
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                matrix.postRotate(270);
            }

            // Rotate the bitmap
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch (Exception exception)
        {
            AppLog.d("Could not rotate the image");
        }
        if(bitmap!=null)
        {
            saveBitmapToFile(output,bitmap);
        }
    }
    private void saveBitmapToFile(File file,Bitmap bitmap)
    {
        FileOutputStream out;
        out=null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                out.close();
            } catch(Throwable ignore) {}
        }
    }

    private void saveImageContents()
    {
        if(imageFilePathList.size()>0)
        {
            int total=imageFilePathList.size();
            for(int i=0;i<total;i++)
            {
                String fileName= ApplicationController.getCurrentOrganID();
                String filePath=imageFilePathList.get(i);
                try {
                    String tempFileName = new File(filePath).getName();
                    if(ApplicationController.fileExists(DIR_RECORD_CONTENT,tempFileName))
                    {
                        imageList.add(tempFileName);
                        continue;
                    }
                    fileName=fileName+"_"+tempFileName;
                    if(ApplicationController.fileExists(DIR_RECORD_CONTENT,fileName))
                    {
                        imageList.add(fileName);
                        continue;
                    }
                }catch (Exception e){}
                File output=ApplicationController.getFile(DIR_RECORD_CONTENT,fileName);
                saveOriginalImage(output, filePath);
                imageList.add(fileName);
            }
        }
    }
    private void saveImagePreviews()
    {
        if(imageBitmaps.size()>0)
        {
            int total=imageBitmaps.size();
            for(int i=0;i<total;i++)
            {
                String fileName= ApplicationController.getCurrentOrganID();
                String filePath=imageFilePathList.get(i);
                try {
                    String tempFileName = new File(filePath).getName();
                    if(ApplicationController.fileExists(DIR_RECORD_PREVIEW,tempFileName))
                    {
                        continue;
                    }
                    fileName=fileName+"_"+tempFileName;
                }catch (Exception e){}
                File output=ApplicationController.getFile(DIR_RECORD_PREVIEW,fileName);
                saveBitmapToFile(output,imageBitmaps.get(i));
            }
        }
    }

    private void saveRecord(){
        record_description=txtDescription.getText().toString().trim();
        record_contact=txtContact.getText().toString().trim();
        record_phone=txtPhone.getText().toString().trim();
        record_created=null;
        record_updated=null;
        if(record_description.equals(""))
        {
            showMessage("隐患内容","请输入隐患内容后保存");
            txtDescription.requestFocus();
            return;
        }
        //Save Image Previews
        saveImagePreviews();
        //Save Image Content
        saveImageContents();
        //Save to DB


        RecordInfo record=new RecordInfo(recordInfo.getIntRecordID(),record_task_id,record_organ_id,record_description,imageList,record_contact, record_phone,record_created, record_updated);
        recordInfo.updateToDB();
        recordSaved=true;
        quitActivity();
    }
}
