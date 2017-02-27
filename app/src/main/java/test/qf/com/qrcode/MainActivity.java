package test.qf.com.qrcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Set;

import info.vividcode.android.zxing.CaptureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et;
    private ImageView iv;
    private Button button;
    private HashMap<EncodeHintType, Object> map;
    private Button read_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = ((EditText) findViewById(R.id.et));
        iv = ((ImageView) findViewById(R.id.iv));
        button = ((Button) findViewById(R.id.btn));
        read_btn = ((Button) findViewById(R.id.clickRead));
        map = new HashMap<>();
        map.put(EncodeHintType.CHARACTER_SET, "utf-8");
        map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        Camera.Parameters parameters = Camera.open().getParameters();
        parameters.setRotation(90);
//        Camera.open().setParameters(parameters);
        button.setOnClickListener(this);
        read_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                String s = et.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    QRCodeWriter writer = new QRCodeWriter();
                    BitMatrix encode;
                    try {
                        encode = writer.encode(s, BarcodeFormat.QR_CODE, 400, 400, map);
                        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);
                        for (int i = 0; i < bitmap.getWidth(); i++) {
                            for (int j = 0; j < bitmap.getHeight(); j++) {
                                if (encode.get(i, j)) {
                                    bitmap.setPixel(i, j, Color.BLACK);
                                } else {
                                    bitmap.setPixel(i, j, Color.WHITE);
                                }
                            }
                        }
                        Bitmap resource = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                        new Canvas(bitmap).drawBitmap(resource, new Rect(0, 0, resource.getWidth(), resource.getHeight()), new Rect(150, 150, 250, 250), null);
                        iv.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.clickRead:
//                new IntentIntegrator(this).initiateScan();
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        String contents = result.getContents();
//        Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
        if (resultCode==RESULT_OK) {
            if (requestCode==0) {
                Bundle bundle = data.getExtras();
                Set<String> strings = bundle.keySet();
                for (String key : strings) {
                    Log.i("1620", "onActivityResult: "+key+", "+bundle.getString(key));
                }
                String result = data.getStringExtra("SCAN_RESULT");
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
