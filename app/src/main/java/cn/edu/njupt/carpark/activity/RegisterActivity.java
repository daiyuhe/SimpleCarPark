package cn.edu.njupt.carpark.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.edu.njupt.carpark.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private String plateNumber;      //车牌
    private String username;     //用户名
    private Button enter;
    private TextView plateNumberTextView;        //车牌文本框
    private EditText usernameEditText;       //用户名输入框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        plateNumberTextView =findViewById(R.id.plate_number);
        usernameEditText =findViewById(R.id.username);
        enter =findViewById(R.id.Enter);
        plateNumber =getIntent().getStringExtra("plateNumber");
        plateNumberTextView.setText(plateNumber);
        enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        username = usernameEditText.getText().toString();
        Intent intent = new Intent(RegisterActivity.this, ChargePolicyActivity.class);
        intent.putExtra("plateNumber", plateNumber);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}