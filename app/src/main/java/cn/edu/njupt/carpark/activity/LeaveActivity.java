package cn.edu.njupt.carpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.njupt.carpark.MainActivity;
import cn.edu.njupt.carpark.R;
import cn.edu.njupt.carpark.entity.CarDO;
import cn.edu.njupt.carpark.service.ParkNumberService;
import cn.edu.njupt.carpark.service.CarParkService;


public class LeaveActivity extends AppCompatActivity implements View.OnClickListener {
    private int garageId;    //车库号
    private long time;        //总时间
    private long cost;           //费用
    private Button leaveBtn;
    private TextView plateNumberTextView;
    private TextView usernameTextView;
    private TextView garageIdTextView;
    private TextView timeTextView;
    private TextView costTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        plateNumberTextView = findViewById(R.id.plate_number);
        usernameTextView = findViewById(R.id.username);
        garageIdTextView = findViewById(R.id.garage_id);
        timeTextView = findViewById(R.id.park_time);
        costTextView = findViewById(R.id.cost);
        leaveBtn = findViewById(R.id.leave);

        garageId = getIntent().getIntExtra("garageId", 0);
        time = getIntent().getLongExtra("time", 0L);
        cost = getIntent().getLongExtra("cost", 0L);
        // 获取反序列化的user
        CarDO carDO = (CarDO) getIntent().getSerializableExtra("carDO");
        if (carDO.getMonthRent()) {
            cost = 0L;
        }

        plateNumberTextView.setText(carDO.getNumber());
        usernameTextView.setText(carDO.getUsername());

        leave(carDO.getNumber());
        garageIdTextView.setText(garageId + "");
        timeTextView.setText(time + "小时");
        long day = 30 - ((System.currentTimeMillis() - carDO.getMonthRentStartTime()) / 1000 / 60 / 60 / 24 + 1);
        if (carDO.getMonthRent()) {
            costTextView.setText("月租剩余:" + day + "天");
        } else {
            costTextView.setText(cost + "元");
        }

        leaveBtn.setOnClickListener(this);
    }

    // 出库 删除数据库相关信息
    private void leave(String CarNum) {
        ParkNumberService parkNumberService = ParkNumberService.getInstance();
        CarParkService carParkService = CarParkService.getInstance();
        //删除关联表相关信息
        carParkService.deleteCarParkDOByNumber(CarNum);
        //维护set集合
        parkNumberService.outParkNumber(garageId);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(LeaveActivity.this, MainActivity.class);
        startActivity(intent);
    }
}