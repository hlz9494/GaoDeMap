package com.hlz.gaodemap;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AMap.OnMapClickListener {
    TextView textview;
    MapView mMapView = null;
    AMap aMap;
    MyLocationStyle myLocationStyle;
    Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textview);
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        getMyLocationStyle();
        if (mMapView != null) {
            aMap = mMapView.getMap();
            //设置定位蓝点的Style
            aMap.setMyLocationStyle(myLocationStyle);
            //设置默认定位按钮是否显示，非必需设置。
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
            aMap.setMyLocationEnabled(true);
            aMap.getUiSettings().setGestureScaleByMapCenter(true);
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.getUiSettings().setCompassEnabled(false);
            //添加mapView的点击事件
            aMap.setOnMapClickListener(this);
        }

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePosition();
//                addPolyline();
//                addPoint();
                getShowLocation();
            }
        });
    }

    //获取当前显示的范围（经纬度）
    private void getShowLocation(){
        VisibleRegion region = aMap.getProjection().getVisibleRegion();
        Log.i("region","region.farLeft "+region.farLeft+" region.farRight "+region.farRight+" region.nearLeft "+region.nearLeft+" region.nearRight "+region.nearRight);
    }

    //添加点
    private void addPoint() {
        LatLng latLng = new LatLng(39.906901, 116.397972);
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
//        marker.remove();
    }

    //绘制线
    private void addPolyline() {
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(39.999391, 116.135972));
        latLngs.add(new LatLng(39.898323, 116.057694));
        latLngs.add(new LatLng(39.900430, 116.265061));
        latLngs.add(new LatLng(39.955192, 116.140092));
        polyline = aMap.addPolyline(new PolylineOptions().addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
//        polyline.remove();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void getMyLocationStyle() {
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.strokeColor(Color.BLACK);
        myLocationStyle.radiusFillColor(Color.BLACK);

        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        AmapUtil.getAmapUtil().getLocation(new CallBack<AMapLocation>() {
            @Override
            public void onSuccess(AMapLocation location) {
                String locationType = "";
                switch (location.getLocationType()) {
                    case 0:
                        locationType = "定位失败";
                        break;
                    case 1:
                        locationType = "GPS定位结果";
                        break;
                    case 2:
                        locationType = "前次定位结果";
                        break;
                    case 4:
                        locationType = "缓存定位结果";
                        break;
                    case 5:
                        locationType = "Wifi定位结果";
                        break;
                    case 6:
                        locationType = "基站定位结果";
                        break;
                    case 8:
                        locationType = "离线定位结果";
                        break;

                }
                textview.setText(location.getLatitude() + ",\n" + location.getLongitude() + ",\n" + location.getAccuracy() + ",\n" + location.getAoiName() + ",\n" + locationType);
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    //设置中心点
    private void changePosition() {
        //老方法
//        LatLng marker1 = new LatLng(32.90403, 115.407525);
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
//        //改变地图状态
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        //新方法
        //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.977290, 116.337000), 12, 0, 0));
        aMap.moveCamera(mCameraUpdate);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getApplicationContext(), latLng.latitude + " " + latLng.longitude, Toast.LENGTH_SHORT).show();
    }

}
