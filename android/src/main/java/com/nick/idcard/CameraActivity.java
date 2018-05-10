package com.nick.idcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.nick.idcard.utils.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private SurfaceView sfv;
    private CameraManager cameraManager;
    private static final String TAG = "CameraActivity";
    private boolean hasSurface;

    //训练数据路径，必须包含tessdata文件夹
    static final String TESSBASE_PATH = Environment.getExternalStorageDirectory() + File.separator + "nick" + File.separator;
    //识别语言英文
    static final String DEFAULT_LANGUAGE = "eng";
    private ImageView iv_result;
    private Intent intent;
    private String requestCode;
    public static final int REQUEST_ID_FRONT = 15001;
    public static final int REQUEST_ID_BACK = 15002;

    private String regExIDCardFront = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";
    private Pattern patternIDCardFront = Pattern.compile(regExIDCardFront);
    private String regExIDCardBack = "([0-9]{4}).([0-9]{2}).([0-9]{2})-([0-9]{4}).([0-9]{2}).([0-9]{2})";
    private Pattern patternIDCardBack = Pattern.compile(regExIDCardBack);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_camera);
        intent = getIntent();
        requestCode = intent.getStringExtra("requestCode");
        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws IOException {
        sfv = (SurfaceView) findViewById(R.id.sfv);
        SurfaceHolder surfaceHolder = sfv.getHolder();
        iv_result = (ImageView) findViewById(R.id.iv_result);
        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraManager.stopPreview();
        cameraManager.closeDriver();
        SurfaceHolder surfaceHolder = sfv.getHolder();
        surfaceHolder.removeCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            initCamera(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initCamera(SurfaceHolder holder) throws IOException {
        cameraManager = new CameraManager();
        if (holder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(holder, this);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            cameraManager.startPreview(this);
        } catch (Exception ioe) {
            Log.d("zk", ioe.toString());

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        Matrix matrix = new Matrix();
        matrix.postScale(1, 1);// 缩放比例

        camera.addCallbackBuffer(data);
        ByteArrayOutputStream baos;
        byte[] rawImage;
        Bitmap bitmap;
        Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                previewSize.width,
                previewSize.height,
                null);
        baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        if (bitmap == null) {
            Log.d("zka", "bitmap is nlll");
            return;
        } else {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            final Bitmap bitmap1 = Bitmap.createBitmap(bitmap, width / 2 - dip2px(150), height / 2 - dip2px(92), dip2px(300), dip2px(185), matrix, true);

            int x, y, w, h;
            x = (int) (bitmap1.getWidth() * 0.340);
            y = (int) (bitmap1.getHeight() * 0.850);//0.800

            //todo 为了提高识别速度
            if ((REQUEST_ID_FRONT + "").equals(requestCode)) {  //todo 身份证正面
                y = (int) (bitmap1.getHeight() * 0.800);//0.800

            } else if ((REQUEST_ID_BACK + "").equals(requestCode)) { //todo 身份证反面
                y = (int) (bitmap1.getHeight() * 0.850);//0.800
            }

            w = (int) (bitmap1.getWidth() * 0.6 + 0.5f);
            h = (int) (bitmap1.getHeight() * 0.12 + 0.5f);
            Bitmap bit_hm = Bitmap.createBitmap(bitmap1, x, y, w, h, matrix, true);
            // iv_result.setImageBitmap(bit_hm);
            if (bit_hm != null) {


                String localre = localre(bit_hm);
                if ((REQUEST_ID_FRONT + "").equals(requestCode)) {
                    Matcher matcher = patternIDCardFront.matcher(localre);
                    boolean b = matcher.matches();
                    if (b) {
                        FileUtils.saveImg2LocalWithName(bitmap, "bitmap_front1");
                        FileUtils.saveImg2LocalWithName(bitmap1, "bitmap_front2");
                        FileUtils.saveImg2LocalWithName(bit_hm, "bitmap_front3");

                        String bitmap_front1_sm = ImageUtils.compressImage(FileUtils.getImageDirectoryPath() + "bitmap_front1.jpg", FileUtils.getImageDirectoryPath() + "bitmap_front1_sm.jpg", 80);
                        String bitmap_front2_sm = ImageUtils.compressImage(FileUtils.getImageDirectoryPath() + "bitmap_front2.jpg", FileUtils.getImageDirectoryPath() + "bitmap_front2_sm.jpg", 80);

                        Log.e(TAG, "onPreviewFrame: " + localre);
//                        onPreviewFrame: 130130199101053056
                        Toast.makeText(getApplicationContext(), localre, Toast.LENGTH_SHORT).show();

                        intent.putExtra("data", localre);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else if ((REQUEST_ID_BACK + "").equals(requestCode)) {

                    FileUtils.saveImg2LocalWithName(bitmap, "bitmap_back1");
                    FileUtils.saveImg2LocalWithName(bitmap1, "bitmap_back2");
                    FileUtils.saveImg2LocalWithName(bit_hm, "bitmap_back3");

                    String bitmap_back1_sm = ImageUtils.compressImage(FileUtils.getImageDirectoryPath() + "bitmap_back1.jpg", FileUtils.getImageDirectoryPath() + "bitmap_back1_sm.jpg", 80);
                    String bitmap_back2_sm = ImageUtils.compressImage(FileUtils.getImageDirectoryPath() + "bitmap_back2.jpg", FileUtils.getImageDirectoryPath() + "bitmap_back2_sm.jpg", 80);


                    Log.e(TAG, "onPreviewFrame: " + localre);
//                    onPreviewFrame: 1 2015.02.16-2025.02.16
//                    Toast.makeText(getApplicationContext(), localre, Toast.LENGTH_SHORT).show();

                    String d[] = localre.split(" ");
                    String ret = "";
                    if (d.length > 0) {
                        ret = d[d.length - 1];
                    }

                    Matcher matcher = patternIDCardBack.matcher(ret);
                    boolean b = matcher.matches();
                    if (b) {
                        Toast.makeText(getApplicationContext(), ret, Toast.LENGTH_SHORT).show();

                        intent.putExtra("data", ret);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

            }
        }
    }


    private String localre(Bitmap bm) {
        String content = "";
        bm = bm.copy(Bitmap.Config.ARGB_8888, true);
        iv_result.setImageBitmap(bm);
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
        //设置识别模式
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        //设置要识别的图片
        baseApi.setImage(bm);
        baseApi.setVariable("tessedit_char_whitelist", "0123456789Xx.-");
        Log.e(TAG, "localre: " + baseApi.getUTF8Text());
        content = baseApi.getUTF8Text();
        baseApi.clear();
        baseApi.end();
        return content;
    }

    public int dip2px(int dp) {
        float density = this.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }
}
