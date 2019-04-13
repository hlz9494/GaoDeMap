package com.hlz.gaodemap;


import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener, AMap.OnCameraChangeListener {
    //
    private MapView mMapView;
    //
    private Button button;

    private AMap aMap;

    private List<Marker> markers = new ArrayList<>();
    private List<LatLng> latLngs = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);
        initView(savedInstanceState);
        initPoint();
        showLocationPoint();
        //监听
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnCameraChangeListener(this);

    }

    private void initView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.map_mapview);
        //在activity执行onCreate时执行m MapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        button = (Button) findViewById(R.id.btn_map);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPoint();
            }
        });
    }

    private void scaleMap() {
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);
        //带动画的缩放
        aMap.animateCamera(mCameraUpdate);
        //不带动画的缩放
//        aMap.moveCamera(mCameraUpdate);
    }

    /***
     * 定位小蓝点
     */
    private void showLocationPoint() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.getUiSettings().setGestureScaleByMapCenter(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setRotateGesturesEnabled(false);
        aMap.getUiSettings().setTiltGesturesEnabled(false);
        aMap.showIndoorMap(true);
    }

    private void getAMapZoom() {
        //获取当前MapView的显示的范围
        VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion();
        LatLng farLeft = visibleRegion.farLeft;     //可视区域的左上角。
        LatLng farRight = visibleRegion.farRight;   //可视区域的右上角。
        LatLng nearLeft = visibleRegion.nearLeft;   //可视区域的左下角。
        LatLng nearRight = visibleRegion.nearRight; //可视区域的右下角。
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
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

    boolean isAdd = false;
    int addSize = 0;


    //新增点
    private void addPoint() {
        while (true) {
            if (addSize < points.size()) {
                isAdd = true;
//            LatLng latLng = new LatLng(30.2829363358, 120.1232242624);
                LatLng latLng = new LatLng(points.get(addSize).getLat(), points.get(addSize).getLon());
//            Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
//            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
                //自定义point
                Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromView(getMyView(points.get(addSize).getLat() + "\n" + points.get(addSize).getLon()))));
                markers.add(marker);
                latLngs.add(latLng);
                addSize++;
            } else {
                LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
                for (int i = 0; i < latLngs.size(); i++) {
                    //trajectorylist为轨迹集合
                    newbounds.include(latLngs.get(i));
                }
                //第二个参数为四周留空宽度
                aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(), 100));
                break;
            }
            aMap.getUiSettings().setAllGesturesEnabled(!aMap.getUiSettings().isScrollGesturesEnabled());
        }
    }

    protected View getMyView(String pm_val) {
        View view = getLayoutInflater().inflate(R.layout.item_marker, null);
        TextView tv_val = (TextView) view.findViewById(R.id.marker_tv_val);
        tv_val.setText(pm_val);
        return view;
    }

    private List<Point> points = new ArrayList<>();

    //初始话点的坐标
    private void initPoint() {
        points.clear();
        points.add(new Point(121.374591f, 29.121103f));
        points.add(new Point(121.374599f, 29.121099f));
        points.add(new Point(121.374348f, 29.115833f));
        points.add(new Point(121.373204f, 29.124605f));
        points.add(new Point(121.372047f, 29.122997f));
        points.add(new Point(121.373208f, 29.124595f));
        points.add(new Point(121.373194f, 29.124579f));
        points.add(new Point(121.371955f, 29.113922f));
        points.add(new Point(121.376512f, 29.117527f));
    }


    boolean isMove = false;
    Location nowLocation;

    @Override
    public void onMyLocationChange(Location location) {
//        nowLocation = location;
//        if (!isMove) {
//            isMove = true;
//            //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
//            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 12, 0, 0));
//            //移动到当前的位置
//            aMap.moveCamera(mCameraUpdate);
//        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (markers.size() != 0) {
//            for (Marker marker : markers) {
//                marker.remove();
//            }
//            markers.clear();
        }
        isAdd = false;
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Toast.makeText(getApplicationContext(), "move finish", Toast.LENGTH_SHORT).show();
        getAMapZoom();
    }

}
