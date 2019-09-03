package cn.edu.njupt.carpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.edu.njupt.carpark.MainActivity;
import cn.edu.njupt.carpark.R;
import cn.edu.njupt.carpark.service.ParkNumberService;
import cn.edu.njupt.carpark.service.CarParkService;
import cn.edu.njupt.carpark.service.CarService;


public class ChargePolicyActivity extends AppCompatActivity implements View.OnClickListener {
    private String plateNumber;      // 车牌号
    private String username;     // 用户名
    private Button monthBtn;
    private Button singleBtn;

    private CarService carService = CarService.getInstance();
    private CarParkService carParkService = CarParkService.getInstance();

    private ParkNumberService parkNumberService = ParkNumberService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_policy);

        monthBtn = findViewById(R.id.Month);
        singleBtn = findViewById(R.id.Hour);
        plateNumber = getIntent().getStringExtra("plateNumber");
        username = getIntent().getStringExtra("username");

        monthBtn.setOnClickListener(this);
        singleBtn.setOnClickListener(this);
    }

    //点击包月或者按次计费之后跳到主页
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Month:
                registerAndEnter(true);
                Intent intent1 = new Intent(ChargePolicyActivity.this, MainActivity.class);
                startActivity(intent1);
                break;
            case R.id.Hour:
                registerAndEnter(false);
                Intent intent2 = new Intent(ChargePolicyActivity.this, MainActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void registerAndEnter(boolean isMonthRent) {
        carService.saveOrUpdate(plateNumber, username, isMonthRent);

        //添加车库关联信息
        int garageId = parkNumberService.getParkNumber();
        carParkService.saveCarParkDO(plateNumber, isMonthRent, garageId);
    }
}