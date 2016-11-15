package com.mu.compet.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mu.compet.MyApplication;
import com.mu.compet.R;
import com.mu.compet.data.ResultMessage;
import com.mu.compet.manager.NetworkManager;
import com.mu.compet.manager.NetworkRequest;
import com.mu.compet.request.IdDuplicateCheckRequest;
import com.mu.compet.request.NewSignUpRequest;
import com.mu.compet.request.NickNameDuplicateCheckRequest;
import com.mu.compet.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends BaseActivity {


    private static final int RC_GET_IMAGE = 1;
    private static final int RC_CAMERA = 2;
    private static final int RC_CROP = 3;

    private ImageView imageProfile;
    private ImageView imageCamera;

    private EditText editId;
    private EditText editNickName;
    private EditText editPassword;
    private EditText editPasswordCheck;

    private Button completeButton;

    private String mCurrentPhotoPath;
    private Uri contentUri;
    private File userFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initToolBar(getString(R.string.activity_sign_up));

        if (savedInstanceState != null) {
            contentUri = Uri.parse(savedInstanceState.getString("media_url"));
        }

        imageProfile = (ImageView) findViewById(R.id.image_profile);
        imageCamera = (ImageView) findViewById(R.id.image_camera);

        editId = (EditText) findViewById(R.id.edit_id);
        editNickName = (EditText) findViewById(R.id.edit_nickname);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editPasswordCheck = (EditText) findViewById(R.id.edit_password_check);
        editPasswordCheck.addTextChangedListener(setTextWatcher());

        completeButton = (Button) findViewById(R.id.btn_complete);
        imageCamera.bringToFront();

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            path.mkdirs();
        }
    }

    public void addPictureClicked(View view) {
        // 갤러리를 통한 이미지 가져오기
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, RC_GET_IMAGE);
        checkPermission(this);
        Intent intent = new Intent(SignUpActivity.this, AddImageActivity.class);
        startActivityForResult(intent, RC_GET_IMAGE);
    }

    public void addCameraClicked(View view) {
        // 카메라를 통한 이미지 가져오기
        checkPermission(this);
        dispatchTakePictureIntent();

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                contentUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, RC_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private TextWatcher setTextWatcher() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String edit = s.toString();
                if (edit.length() > 0 && editId.getText().toString().length() > 0
                        && editNickName.getText().toString().length() > 0
                        && editPasswordCheck.getText().toString().length() > 0) { //활성화
                    completeButton.setBackgroundColor(getResources().getColor(R.color.mainRedColor));
                    completeButton.setEnabled(true);

                } else {
                    completeButton.setBackgroundColor(getResources().getColor(R.color.gray));
                    completeButton.setEnabled(false);
                }
            }
        };
        return textWatcher;
    }

    // SignUp 완료 버튼
    public void loginCompleteSignUp(View view) {

        String userId = editId.getText().toString();
        String userPass = editPassword.getText().toString();
        String userNick = editNickName.getText().toString();
//        if(!TextUtils.isEmpty(mCurrentPhotoPath)) {
//            userFile = new File(mCurrentPhotoPath);
//        }
        if (userFile != null) {
            Log.d("SignUpActivity", "파일 : " + userFile.getAbsolutePath());
        }
        NewSignUpRequest request = new NewSignUpRequest(this, userId, userPass, userNick, userFile);
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
            @Override
            public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {

                Log.d("SignUpActivity", "성공 : " + result.getCode());
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                Log.d("SignUpActivity", "실패 : " + errorCode);


            }
        });


    }

    public void duplicateIdCheckClicked(View view) {
        // id 중복 요청
        String userId = editId.getText().toString();
        if (!TextUtils.isEmpty(userId)) {

            IdDuplicateCheckRequest request = new IdDuplicateCheckRequest(this, userId);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                @Override
                public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                    Toast.makeText(SignUpActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                    Toast.makeText(SignUpActivity.this, "실패", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            ToastUtil.show(this, "아이디를 입력하세요");
        }


    }

    public void duplicateNickNameCheckClicked(View view) {
        // nickname 중복 요청

        String nickName = editNickName.getText().toString();
        if (!TextUtils.isEmpty(nickName)) {
            NickNameDuplicateCheckRequest request = new NickNameDuplicateCheckRequest(this, nickName);
            NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<ResultMessage>() {
                @Override
                public void onSuccess(NetworkRequest<ResultMessage> request, ResultMessage result) {
                    ToastUtil.show(MyApplication.getContext(), "사용가능한 닉네임입니다.");
                }
                @Override
                public void onFail(NetworkRequest<ResultMessage> request, int errorCode, String errorMessage, Throwable e) {
                    ToastUtil.show(MyApplication.getContext(), "실패");
                }
            });
        } else {
            ToastUtil.show(this, "닉네임을 입력하세요");
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case RC_GET_IMAGE:
                    StringBuilder str = new StringBuilder();
                    if (resultCode == RESULT_OK) {
                        String[] files = intent.getStringArrayExtra("files");
                        for (String s : files) {
                            Log.i("ImageFiles", "files : " + s);
                            str.append(s);
                        }
                        String fileName = str.toString();
                        contentUri = Uri.fromFile(new File(fileName));
                        cropImage(contentUri);
                    }

                    break;
//                case RC_GET_IMAGE:
//                    Log.d("SignUpActivity", "uri : " + contentUri);
//                    contentUri = intent.getData();
                case RC_CAMERA:
                    rotatePhoto();
                    cropImage(contentUri);
                    break;
                case RC_CROP:
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        Bitmap bitmap = (Bitmap) extras.get("data");
                        Bitmap circleBitmap = circleImage(bitmap);
                        imageProfile.setImageBitmap(circleBitmap);

                        if (mCurrentPhotoPath != null) {
                            userFile = new File(mCurrentPhotoPath);
                            if (userFile.exists()) {
                                userFile.delete();
                            }
//                            mCurrentPhotoPath = null;
                        }
                    }
                    break;
            }
        }
    }

    private void cropImage(Uri contentUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(contentUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, RC_CROP);
    }

    private Bitmap circleImage(Bitmap bitmapimg) {

        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    public Bitmap getBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inInputShareable = true;
        options.inDither = false;
        options.inTempStorage = new byte[32 * 1024];
        options.inPurgeable = true;
        options.inJustDecodeBounds = false;

        File f = new File(mCurrentPhotoPath);

        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bm = null;

        try {
            if (fs != null) bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bm;
    }

    public void rotatePhoto() {
        ExifInterface exif;
        try {
            if (mCurrentPhotoPath == null) {
                mCurrentPhotoPath = contentUri.getPath();
            }
            exif = new ExifInterface(mCurrentPhotoPath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            if (exifDegree != 0) {
                Bitmap bitmap = getBitmap();
                Bitmap rotatePhoto = rotate(bitmap, exifDegree);
                saveBitmap(rotatePhoto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Bitmap rotate(Bitmap image, int degrees) {
        if (degrees != 0 && image != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) image.getWidth(), (float) image.getHeight());

            try {
                Bitmap b = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), m, true);

                if (image != b) {
                    image.recycle();
                    image = b;
                }

                image = b;
            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return image;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current state
        if (contentUri != null) {
            savedInstanceState.putString("media_url", contentUri.toString());
        }
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    public void saveBitmap(Bitmap bitmap) {
        File file = new File(mCurrentPhotoPath);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
