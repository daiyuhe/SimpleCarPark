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
import cn.edu.njupt.carpark.service.UserService;


public class ChargePolicyActivity extends AppCompatActivity implements View.OnClickListener {
    private String CarNum;      //车牌
    private String carUser;     //车主
    private Button MonthBt;
    private Button HourBt;

    private UserService userService = UserService.getInstance();
    private CarParkService carParkService = CarParkService.getInstance();

    private ParkNumberService parkNumberService = ParkNumberService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_policy);

        MonthBt = findViewById(R.id.Month);
        HourBt = findViewById(R.id.Hour);
        CarNum = getIntent().getStringExtra("CarNum");
        carUser = getIntent().getStringExtra("carUser");

        MonthBt.setOnClickListener(this);
        HourBt.setOnClickListener(this);
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
        userService.saveOrUpdate(CarNum, carUser, isMonthRent);

        //添加车库关联信息
        int garageId = parkNumberService.getGarageId();
        carParkService.saveGarageRelation(CarNum, isMonthRent, garageId);
    }
}