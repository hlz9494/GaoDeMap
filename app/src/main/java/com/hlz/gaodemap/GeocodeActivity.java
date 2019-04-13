package com.hlz.gaodemap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/***
 * 地理编码
 * @author hlz
 */
public class GeocodeActivity extends AppCompatActivity implements OnGeocodeSearchListener {
    GeocodeSearch geocodeSearch;
    private TextView tv_result;
    //经纬度
    private EditText edittext1, edittext2;
    //中文地址
    private EditText edittext3;
    private Button btn_gencode;

    private Button btn_regencode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocode);
        initView();
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

    }

    private void initView() {
        tv_result = (TextView) findViewById(R.id.tv_result);
        edittext1 = (EditText) findViewById(R.id.edittext1);
        edittext2 = (EditText) findViewById(R.id.edittext2);
        edittext3 = (EditText) findViewById(R.id.edittext3);
        btn_gencode = (Button) findViewById(R.id.btn_gencode);
        btn_gencode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = edittext3.getText().toString();
                geocodeAddress(address);
            }
        });
        btn_regencode = (Button) findViewById(R.id.btn_regencode);
        btn_regencode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double lon = Double.valueOf(edittext2.getText().toString());
                    double lat = Double.valueOf(edittext1.getText().toString());
                    regeocodeAddress(lon, lat);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "转换失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void geocodeAddress(String address) {
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(address, "杭州");
        geocodeSearch.getFromLocationNameAsyn(query);
    }

    private void regeocodeAddress(double lon, double lat) {
// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        LatLonPoint latLonPoint = new LatLonPoint(lat, lon);

        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);

        geocodeSearch.getFromLocationAsyn(query);
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        //逆地理编码
        if (i == 1000) {
            if (regeocodeResult != null) {
                tv_result.setText(regeocodeResult.getRegeocodeAddress().getAois().get(0).getAoiName());
            }
        } else {
            //失败
            Toast.makeText(getApplicationContext(), "获取反地理编码失败!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        //地理编码 返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）
        if (i == 1000) {
            if (geocodeResult.getGeocodeAddressList() != null && geocodeResult.getGeocodeAddressList().size() != 0) {
                GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                tv_result.setText(address.getLatLonPoint().getLatitude() + " " + address.getLatLonPoint().getLongitude());
            }
        } else {
            //失败
            Toast.makeText(getApplicationContext(), "获取地理编码失败!", Toast.LENGTH_SHORT).show();
        }
    }
}
