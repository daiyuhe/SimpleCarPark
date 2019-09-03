package cn.edu.njupt.carpark;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import cn.edu.njupt.carpark.activity.ChooseActivity;
import cn.edu.njupt.carpark.activity.LeaveActivity;
import cn.edu.njupt.carpark.activity.RegisterActivity;
import cn.edu.njupt.carpark.bean.CarParkDO;
import cn.edu.njupt.carpark.bean.CarDO;
import cn.edu.njupt.carpark.service.DistributionGarageIdService;
import cn.edu.njupt.carpark.service.GarageRelationService;
import cn.edu.njupt.carpark.service.UserService;
import cn.edu.njupt.carpark.util.GetCarCode;
import cn.edu.njupt.carpark.util.ImageHandler;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static GarageRelationService garageRelationService = new GarageRelationService();
    private static UserService userService = new UserService();
    private static DistributionGarageIdService distributionGarageIdService = new DistributionGarageIdService();

    private int parkingSpace; //车库可用的车位
    private TextView garageId;
    private Button scanBtn;
    private Button choosePhoto;
    private ImageView picture;

    public static final int TAKE_PHOTO = 1; // 拍照
    public static final int CHOOSE_PHOTO = 2; // 选择相册
    private static String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Uri imageUri;

    //............byte array
    private static byte[] imagedata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LitePal.initialize(this);
        setContentView(R.layout.activity_main);
        garageId = findViewById(R.id.garage_id);
        scanBtn = findViewById(R.id.scan_btn);
        choosePhoto = findViewById(R.id.choose_from_album);
        picture = findViewById(R.id.iv_picture);
        parkingSpace = new DistributionGarageIdService().leaveGaragedIdNubers();
        garageId.setText(parkingSpace + "");
        scanBtn.setOnClickListener(this);
        choosePhoto.setOnClickListener(this);
        getPermission();
    }

    //调用扫描识别车牌
    @Override
    public void onClick(View v) {
        if (parkingSpace <= 0) {
            Toast.makeText(MainActivity.this, "暂无车位！", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.choose_from_album:
                getPermission();
                openAlbum();
                break;
            case R.id.scan_btn:
                scan();
        }
    }

    // 打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    // 扫描函数返回车牌号码
    private void scan() {
        getPermission();

        //创建file对象，用于存储拍照后的图片；
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(MainActivity.this,
                    "cn.edu.njupt.carpark.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    // 获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, permissions)) {
            //已经打开权限
            // Toast.makeText(this, "已经申请相关权限", Toast.LENGTH_SHORT).show();
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 拍照
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                        if (null != bm) {
                            bm = ImageHandler.rotateBitmap(bm, 90);
                            bm = ImageHandler.imageZoom(bm);

                            picture.setImageBitmap(bm);
                            imagedata = ImageHandler.bitmap2ByteArray(bm);
                        }
                        new Thread(networkTask).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 选择图片
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap;
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    InputStream is = null;
                    try {
                        is = cr.openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap = BitmapFactory.decodeStream(is);
                    picture.setImageBitmap(bitmap);

                    if (null != bitmap) {
                        imagedata = ImageHandler.bitmap2ByteArray(bitmap);
                    }
                    if (imagedata != null) {
                        new Thread(networkTask).start();
                    }
                }
            default:
                break;
        }
    }

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // 子线程使用Toast
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            try {
                String result = GetCarCode.checkFile(imagedata);
                JSONObject jsonObject = JSONObject.parseObject(result);

                if (jsonObject.getString("error_code") != null) {
                    System.out.println(result);
                    Toast.makeText(MainActivity.this, "识别失败，请重试！", Toast.LENGTH_SHORT).show();
                } else {
                    String plateNumber = jsonObject.getJSONObject("words_result").getString("number");
                    park(plateNumber);
                }
                Looper.loop();
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            } finally {
                imagedata = null;
            }
        }
    };

    private void park(String plateNumber) {
        CarParkDO carParkDO = garageRelationService.getGarageRelation(plateNumber);
        // 离开停车场
        if (carParkDO != null) {
            CarDO carDO = userService.getByNumber(plateNumber);
            // 小时向上取整
            long time = (System.currentTimeMillis() / 1000 - carParkDO.getEnterTime()) / 60 / 60 + 1;
            Intent intent = new Intent(MainActivity.this, LeaveActivity.class);

            // 缴费金额
            long cost = time * 5;
            // 传递相关数据
            intent.putExtra("cost", cost);
            intent.putExtra("time", time);
            intent.putExtra("garageId", carParkDO.getGarageNumber());
            intent.putExtra("carDO", carDO);
            if (carParkDO.getMonthRent()) {
                // 是月租
                intent.putExtra("cost", 0L);
            }
            startActivity(intent);

            // 出库
            //删除关联表信息
            garageRelationService.deleteGarageRelation(carParkDO.getNumber());
            //维护set集合信息
            distributionGarageIdService.outGarageId(carParkDO.getGarageNumber());

        } else { // 停车
            CarDO carDO = userService.getByNumber(plateNumber);
            if (null == carDO) {
                // 注册流程
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.putExtra("CarNum", plateNumber);
                startActivity(intent);
                return;
            } else if (carDO.getMonthRent()) {
                if ((System.currentTimeMillis() - carDO.getMonthRentStartTime()) / 1000 / 60 / 60 / 24 > 30) {
                    int i = userService.monthRentExpired(plateNumber, carDO.getUsername());
                    // 避免无限递归
                    if (i > 0) {
                        Toast.makeText(MainActivity.this, "月租已到期，请重新选择", Toast.LENGTH_SHORT).show();
                        park(plateNumber);
                    }
                }
                // 直接进入
                //添加关联表信息
                garageRelationService.saveGarageRelation(plateNumber, true, distributionGarageIdService.getGarageId());
                Toast.makeText(MainActivity.this, "月租用户，欢迎光临！", Toast.LENGTH_SHORT).show();
                return;
            }

            // 进入ChooseActivity
            Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
            intent.putExtra("CarNum", plateNumber);
            intent.putExtra("carUser", carDO.getUsername());
            startActivity(intent);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // Toast.makeText(this, "相关权限获取成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }
}


