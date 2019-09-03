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
    private String CarNum;      //车牌
    private String CarUser;     //车主
    private Button Enter;
    private TextView CarNumTextView;        //车牌文字框
    private EditText CarUserEditText;       //车主输入框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CarNumTextView=findViewById(R.id.plate_number);
        CarUserEditText=findViewById(R.id.username);
        Enter=findViewById(R.id.Enter);
        CarNum=getIntent().getStringExtra("CarNum");
        CarNumTextView.setText(CarNum);
        Enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CarUser=CarUserEditText.getText().toString();
        Intent intent = new Intent(RegisterActivity.this, ChargePolicyActivity.class);
        intent.putExtra("CarNum", CarNum);
        intent.putExtra("carUser", CarUser);
        startActivity(intent);
    }

}