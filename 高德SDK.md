# 高德Android SDK

## 一、高德Android SDK简介	

高德开放平台是国内技术领先的LBS服务提供商，拥有先进的数据融合技术和海量的数据处理能力。服务超过三十万款移动应用，日均处理定位请求及路径规划数百亿次。高德开放平台向广大开发者提供覆盖移动端和Web端的开发工具，开发者通过调用开发包或接口即可在应用或网页中实现地图显示、标注、位置检索等功能。使得LBS应用的开发过程更加容易。

高德正在进行的位置大数据探索与实践，高德地图开放平台通过其服务的三十万款应用，每日百亿级的位置请求和相关行为，对现实世界做了人群走向、区域热度、行为偏好等分析和洞察，试图通过数据画像还原一个在我们身边熟悉却又陌生的世界。

**高德开放平台为开发者提供了三项主要的能力：**

1、专业、易用的地图开发工具：API/SDK

2、快捷的位置云计算：云图

3、权威的位置大数据：高德位智



**高德Android SDK主要功能**

高德提供了地图、定位、导航、猎鹰、室内地图、室内导航等一系列的SDK接口，方便开发者调用



## 二、高德Android SDK使用

### 0.基本配置

- 在使用高德 Android SDK之前必须，在[高德开放平台上申请一个专属的key](https://lbs.amap.com/dev/key/app)

- 具体的流程如下

  ​	进入控制台之后创建新应用，

  ​	在创建的应用上点击"添加新Key"按钮，在弹出的对话框中，依次：输入应用名名称，选择绑定的服务为“Android平台SDK”，选择可使用的服务，输入发布版安全码  SHA1、调试版安全码 SHA1、以及 PackageName

> 需要注意的是： 1个KEY只能用于一个应用（多渠道安装包属于多个应用），1个Key在多个应用上使用会出现服务调用失败。



### 1. 定位SDK



#### 1.1 定位SDK简介

- 最新版本为 Android 定位SDK V4.3.0，包含全球定位，同时支持室内定位功能。
- Android 定位 SDK 是一套简单的LBS服务定位接口，您可以使用这套定位API获取定位结果、逆地理编码（地址文字描述）、以及地理围栏功能。



#### 1.2 定位SDK使用

##### 1.2.1 添加依赖

- 通过拷贝集成SDK

   **本文采用的是最新版本的定位SDK V4.3.0 (AMap_Location_V4.3.0_20181016.jar)**

   将下载的定位 SDK jar 文件复制到工程的 libs 目录下，如果有老版本定位 jar 文件存在，请删除。定位 SDK 无需 so 库文件支持。

		在 build.gradle（主工程） 文件的 dependencies 中配置 compile fileTree(include: ['*.jar'], dir: 'libs')。



##### 1.2.2 配置AndroidManifest.xml

- 声明Service组件

		请在application标签中声明service组件,每个app拥有自己单独的定位service。

```
<service android:name="com.amap.api.location.APSService"></service>
```

- 声明权限

```
<!--用于进行网络定位-->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"></uses-permission>
<!--用于访问GPS定位-->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission>
<!--用于获取运营商信息，用于支持提供运营商信息相关的接口-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
<!--用于访问wifi网络信息，wifi信息会用于进行网络定位-->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
<!--用于获取wifi的获取权限，wifi信息会用来进行网络定位-->
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"></uses-permission>
<!--用于访问网络，网络定位需要上网-->
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
<!--用于读取手机当前的状态-->
<uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
<!--用于写入缓存数据到扩展存储卡-->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
<!--用于申请调用A-GPS模块-->
<uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS"></uses-permission>
<!--用于申请获取蓝牙信息进行室内定位-->
<uses-permission android:name="android.permission.BLUETOOTH"></uses-permission>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"></uses-permission>
```

- 最后，设置高德Key

```
<meta-data android:name="com.amap.api.v2.apikey" android:value="key">//开发者申请的key       
            
</meta-data>
```



##### 1.2.3  初始话定位

```
//声明AMapLocationClient类对象
public AMapLocationClient mLocationClient = null;
//声明定位回调监听器
public AMapLocationListener mLocationListener = new AMapLocationListener();
//初始化定位
mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
mLocationClient.setLocationListener(mLocationListener);
```



##### 1.2.4 定位配置参数

- 创建AMapLocationClientOption对象

```
//声明AMapLocationClientOption对象
public AMapLocationClientOption mLocationOption = null;
//初始化AMapLocationClientOption对象
mLocationOption = new AMapLocationClientOption();
```

- 选择定位模式

  高德定位服务包含GPS和网络定位（Wi-Fi和基站定位）两种能力。定位SDK将GPS、网络定位能力进行了封装，以三种定位模式对外开放，**默认选择使用高精度定位模式**。

  **高精度定位模式**：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。

  ```
  //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
  mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
  ```

  **低功耗定位模式**：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；

  ```
  //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
  mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
  ```

  **仅用设备定位模式**：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，需要在室外环境下才可以成功定位。

  ```
  //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
  mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);
  ```

  **设置单次定位**

  ```
  //获取一次定位结果：
  //该方法默认为false。
  mLocationOption.setOnceLocation(true);
  
  //获取最近3s内精度最高的一次定位结果：
  //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
  mLocationOption.setOnceLocationLatest(true);
  
  ```

  > 如果设置了单次定位，机器则会获取最近**3秒**内精度最高的一次定位结果，正因为如此，所以导致给人感觉较慢

  **自定义连续定位**

  SDK默认采用连续定位模式，时间间隔2000ms。如果您需要自定义调用间隔：

  ```
  //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
  mLocationOption.setInterval(1000);
  ```

- 其他配置

  设置定位同时是否需要返回地址描述。

  ```
  //设置是否返回地址信息（默认返回地址信息）
  mLocationOption.setNeedAddress(true);
  
  //设置是否允许模拟软件Mock位置结果，多为模拟GPS定位结果，默认为true，允许模拟位置
  mLocationOption.setMockEnable(true);
  
  //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
  //注意：setHttpTimeOut(long httpTimeOut)方法不仅会限制低功耗定位、高精度定位两种模式的定位超时时间，同样会作用在仅设备定位时。如果单次定位发生超时情况，定位随即终止；连续定位状态下当前这一次定位会返回超时，但按照既定周期的定位请求会继续发起。
  mLocationOption.setHttpTimeOut(20000);
  
  //缓存机制默认开启，可以通过以下接口进行关闭。
  //当开启定位缓存功能，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
  mLocationOption.setLocationCacheEnable(false);
  
  //isWifiActiveScan是布尔型参数，true表示会主动刷新设备wifi模块，获取到最新鲜的wifi列表（wifi新鲜程度决定定位精度）；false表示不主动刷新。默认 false
  //传入true，启动定位，AmapLocationClient会驱动设备扫描周边wifi，获取最新的wifi列表（相比设备被动刷新会多消耗一些电量），从而获取更精准的定位结果。注意：模式为仅设备模式(Device_Sensors)时无效
  mLocationOption.setWifiActiveScan(false)
  
  //Protocol是整型参数，用于设定网络定位时所采用的协议，提供http/https两种协议。默认 AMapLocationProtocol.HTTP
  //起AMapLocationProtocol.HTTP代表http;AMapLocationProtocol.HTTPS代表https。
  mLocationOption.setProtocol(AMapLocationProtocol.HTTP)
  ```

- 选择定位场景

  目前支持3种定位场景的设置：签到、出行、运动。默认无场景 

  ```
  AMapLocationClientOption option = new AMapLocationClientOption();
      /**
       * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
       */
  option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
  if(null != locationClient){
     locationClient.setLocationOption(option);
     //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
     locationClient.stopLocation();
     locationClient.startLocation();
  }
  ```

##### 1.2.5 启动定位

```
//给定位客户端对象设置定位参数
mLocationClient.setLocationOption(mLocationOption);
//启动定位
mLocationClient.startLocation();
```

##### 1.2.6 获取定位的结果

AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，回调参数是AMapLocation。

```
//可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
//以下为后者的举例：
AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
@Override
public void onLocationChanged(AMapLocation amapLocation) {
    
  }
}
```

获取回调中的AMapLocation，解析AmapLocation就可以得到对应的信息

当判断AMapLocation对象不为空，当定位错误码类型为0时定位成功。

```
if (amapLocation != null) {
    if (amapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
    }else {
    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
    Log.e("AmapError","location Error, ErrCode:"
        + amapLocation.getErrorCode() + ", errInfo:"
        + amapLocation.getErrorInfo());
    }
}
```

- 当定位成功时，可在如上判断中解析amapLocation对象的具体字段，参考如下： 

| 方法                   | 返回值 | 返回值说明          | 方法效果                                              | 备注                                                         |
| ---------------------- | ------ | ------------------- | ----------------------------------------------------- | ------------------------------------------------------------ |
| getLatitude()          | double | 纬度                | 获取纬度                                              |                                                              |
| getLongitude()         | double | 经度                | 获取经度                                              |                                                              |
| getAccuracy()          | float  | 精度                | 获取定位精度 单位:米                                  |                                                              |
| getAltitude()          | double | 海拔                | 获取海拔高度信息                                      |                                                              |
| getSpeed()             | float  | 速度                | 单位：米/秒                                           |                                                              |
| getBearing()           | float  | 方向角              | 获取方向角信息                                        |                                                              |
| getBuildingId()        | String | 室内定位建筑物Id    | 获取室内定位建筑物Id                                  |                                                              |
| getFloor()             | String | 室内定位楼层        | 获取室内定位楼层                                      |                                                              |
| getAddress()           | String | 地址描述            | 获取地址描述                                          | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getCountry()           | String | 国家                | 获取国家名称                                          | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getProvince()          | String | 省                  | 获取省名称                                            | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getCity()              | String | 城市                | 获取城市名称                                          | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getDistrict()          | String | 城区                | 获取城区名称                                          | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getStreet()            | String | 街道                | 获取街道名称                                          | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getStreetNum()         | String | 街道门牌号          | 获取街道门牌号信息                                    | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getCityCode()          | String | 城市编码            | 获取城市编码信息                                      | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getAdCode()            | String | 区域编码            | 获取区域编码信息                                      | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getPoiName()           | String | 当前位置POI名称     | 获取当前位置的POI名称                                 | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getAoiName()           | String | 当前位置所处AOI名称 | 获取当前位置所处AOI名称                               | 模式为仅设备模式(Device_Sensors)时无此信息                   |
| getGpsAccuracyStatus() | int    | 设备当前 GPS 状态   | 获取GPS当前状态，返回值可参考AMapLocation类提供的常量 | 模式为仅设备模式(Device_Sensors)时提供此信息                 |
| getLocationType()      | int    | 定位来源            | 获取定位结果来源                                      | 可参考[定位类型编码表](https://lbs.amap.com/api/android-location-sdk/guide/utilities/location-type/) |
| getLocationDetail()    | String | 定位信息描述        | 定位信息描述                                          | 用于问题排查                                                 |
| getErrorInfo()         | String | 定位错误信息描述    | 定位出现异常的描述                                    | 可参考[定位错误码表](https://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/) |
| getErrorCode()         | String | 定位错误码          | 定位出现异常时的编码                                  | 可参考[定位错误码表](https://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/) |

​	定位类型对照表

​	当定位成功之后，getLocationType可以获得本次定位的定位信息描述

| 响应码 | 说明         | 介绍                                                         |
| ------ | ------------ | ------------------------------------------------------------ |
| 0      | 定位失败     | 请通过AMapLocation.getErrorCode()方法获取错误码，并参考[错误码对照表](https://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/)进行问题排查。 |
| 1      | GPS定位结果  | 通过设备GPS定位模块返回的定位结果，精度较高，在10米－100米左右 |
| 2      | 前次定位结果 | 网络定位请求低于1秒、或两次定位之间设备位置变化非常小时返回，设备位移通过传感器感知。 |
| 4      | 缓存定位结果 | 返回一段时间前设备在同样的位置缓存下来的网络定位结果         |
| 5      | Wifi定位结果 | 属于网络定位，定位精度相对基站定位会更好，定位精度较高，在5米－200米之间。 |
| 6      | 基站定位结果 | 纯粹依赖移动、联通、电信等移动网络定位，定位精度在500米-5000米之间。 |
| 8      | 离线定位结果 | -                                                            |

- 定位失败时，回调接口将返回错误码，可通过AMapLocation.getErrorCode()方法获取错误码，通过错误码可粗略得到本次定位失败的原因，错误码对照码见下表

| 响应码 | 问题说明                                                  | 问题排查策略                                                 |
| ------ | --------------------------------------------------------- | ------------------------------------------------------------ |
| 0      | 定位成功。                                                | 可以在定位回调里判断定位返回成功后再进行业务逻辑运算。       |
| 1      | 一些重要参数为空，如context；                             | 请对定位传递的参数进行非空判断。                             |
| 2      | 定位失败，由于仅扫描到单个wifi，且没有基站信息。          | 请重新尝试。                                                 |
| 3      | 获取到的请求参数为空，可能获取过程中出现异常。            | 请对所连接网络进行全面检查，请求可能被篡改。                 |
| 4      | 请求服务器过程中的异常，多为网络情况差，链路不通导致      | 请检查设备网络是否通畅，检查通过接口设置的网络访问超时时间，建议采用默认的30秒。 |
| 5      | 请求被恶意劫持，定位结果解析失败。                        | 您可以稍后再试，或检查网络链路是否存在异常。                 |
| 6      | 定位服务返回定位失败。                                    | 请获取errorDetail（通过getLocationDetail()方法获取）信息并参考[定位常见问题](https://lbs.amap.com/faq/android/android-location/292)进行解决。 |
| 7      | KEY鉴权失败。                                             | 请仔细检查key绑定的sha1值与apk签名sha1值是否对应，或通过[高频问题](https://lbs.amap.com/faq/top/hot-questions/253)查找相关解决办法。 |
| 8      | Android exception常规错误                                 | 请将errordetail（通过getLocationDetail()方法获取）信息通过[工单系统](https://lbs.amap.com/dev/ticket/create/18)反馈给我们。 |
| 9      | 定位初始化时出现异常。                                    | 请重新启动定位。                                             |
| 10     | 定位客户端启动失败。                                      | 请检查AndroidManifest.xml文件是否配置了APSService定位服务    |
| 11     | 定位时的基站信息错误。                                    | 请检查是否安装SIM卡，设备很有可能连入了伪基站网络。          |
| 12     | 缺少定位权限。                                            | 请在设备的设置中开启app的定位权限。                          |
| 13     | 定位失败，由于未获得WIFI列表和基站信息，且GPS当前不可用。 | 建议开启设备的WIFI模块，并将设备中插入一张可以正常工作的SIM卡，或者检查GPS是否开启；如果以上都内容都确认无误，请您检查App是否被授予定位权限。 |
| 14     | GPS 定位失败，由于设备当前 GPS 状态差。                   | 建议持设备到相对开阔的露天场所再次尝试。                     |
| 15     | 定位结果被模拟导致定位失败                                | 如果您希望位置被模拟，请通过`setMockEnable(true);方法开启允许位置模拟` |
| 16     | 当前POI检索条件、行政区划检索条件下，无可用地理围栏       | 建议调整检索条件后重新尝试，例如调整POI关键字，调整POI类型，调整周边搜区域，调整行政区关键字等。 |
| 18     | 定位失败，由于手机WIFI功能被关闭同时设置为飞行模式        | 建议手机关闭飞行模式，并打开WIFI开关                         |
| 19     | 定位失败，由于手机没插sim卡且WIFI功能被关闭               | 建议手机插上sim卡，打开WIFI开关                              |

##### 1.2.7  关闭定位

```
mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
```

销毁定位客户端之后，若要重新开启定位请重新New一个AMapLocationClient对象。

##### 1.2.8 简单示例

```
//声明AMapLocationClient类对象
private AMapLocationClient mLocationClient = null;
//声明定位回调监听器
private AMapLocationListener mLocationListener = null;

public void getAMapLocation() {
    //声明AMapLocationClient类对象
    mLocationClient = null;
    //声明定位回调监听器
    mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        }
    };
    //初始化定位
    mLocationClient = new AMapLocationClient(getApplicationContext());
    //设置定位回调监听
    mLocationClient.setLocationListener(mLocationListener);
    //给定位客户端对象设置定位参数
    mLocationClient.setLocationOption(getDefaultOption());
    //启动定位
    mLocationClient.startLocation();
}

private AMapLocationClientOption getDefaultOption() {
    //初始化AMapLocationClientOption对象
    AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
	//设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
	//获取一次定位结果：
	//该方法默认为false。
    mLocationOption.setOnceLocation(false);
	//获取最近3s内精度最高的一次定位结果：
    //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
	mLocationOption.setOnceLocationLatest(false);
	mLocationOption.setInterval(3000);
    //设置是否返回地址信息（默认返回地址信息）
	mLocationOption.setNeedAddress(true);
    //设置是否允许模拟位置,默认为true，允许模拟位置
	mLocationOption.setMockEnable(true);
    //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
	mLocationOption.setHttpTimeOut(20000);
    //关闭缓存机制
	mLocationOption.setLocationCacheEnable(false);

	return mLocationOption;
}
```



### 2. 地图SDK

#### 2.1 地图SDK简介

- 高德开放平台目前开放了Android 地图 SDK 以及 Android 地图 SDK 专业版两套地图SDK工具。
- 高德地图 Android SDK 是一套地图开发调用接口，开发者可以轻松地在自己的Android应用中加入地图相关的功能，包括：地图显示（含室内、室外地图）、与地图交互、在地图上绘制、兴趣点搜索、地理编码、离线地图等功能。
- 高德地图 Android SDK 专业版是在 Android SDK 已有服务的基础上，新增支持了自定义地图在线加载、自定义地图元素纹理等功能，便于开发者完成基于自身场景的更深层、更个性化地图的开发需求。
- 本文介绍普通版Android 地图 SDK



#### 2.2 地图SDK使用

##### 2.2.1 添加依赖

- 通过拷贝集成SDK及SO文件

  **本文采用的是最新版本的地图SDK V6.5.0 (AMap3DMap_6.5.0_AMapSearch_6.5.0_20180930.jar)**

  将下载的定位 SDK jar 文件复制到工程的 libs 目录下，如果有老版本定位 jar 文件存在，请删除。

  在 build.gradle（主工程） 文件的 dependencies 中配置 compile fileTree(include: ['*.jar'], dir: 'libs')。

  

  **由于本文集成的是3D地图，所以需要添加so库**

  在 main 目录下创建文件夹 jniLibs (如果有就不需要创建了)，将下载文件的 armeabi 文件夹复制到这个目录下,如果已经有这个目录，将下载的 so 库复制到这个目录即可。 

##### 2.2.2 配置AndroidManifest.xml

- 声明权限

```
<!--允许程序打开网络套接字-->
<uses-permission android:name="android.permission.INTERNET" />  
<!--允许程序设置内置sd卡的写权限-->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />    
<!--允许程序获取网络状态-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 
<!--允许程序访问WiFi网络信息-->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> 
<!--允许程序读写手机状态和身份-->
<uses-permission android:name="android.permission.READ_PHONE_STATE" />     
<!--允许程序访问CellID或WiFi热点来获取粗略的位置-->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /> 
```

- 最后，设置高德Key

```
<meta-data android:name="com.amap.api.v2.apikey" android:value="key">//开发者申请的key       
            
</meta-data>
```

##### 2.2.3 显示地图

- java中的代码

```
public class MainActivity extends Activity {
  MapView mMapView = null;
  private AMap aMap;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.activity_main);
    //获取地图控件引用
    mMapView = (MapView) findViewById(R.id.map);
    //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
    mMapView.onCreate(savedInstanceState);
    if (aMap == null) {
    //初始化地图控制器对象
      aMap = mapView.getMap();        
	}
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
}

```

- xml代码

```
<com.amap.api.maps.MapView

    android:id="@+id/map"

    android:layout_width="match_parent"

    android:layout_height="match_parent"/>
```

##### 2.2.4 地图相关参数设置

- 控制室内地图是否开启

```
aMap.showIndoorMap(true); //true：显示室内地图；false：不显示；
	//缩放级别≥17级时，地图上可以显示室内地图。缩放级别≥18级时，不仅可以看到室内地图效果，还允许操作切换楼层，显示精细化室内地图。
```

- 切换地图图层

```
aMap.setMapType(type);// 设置卫星地图模式，aMap是地图控制器对象。
```

​	type参数说明

| 名称               | 说明                   |
| ------------------ | ---------------------- |
| MAP_TYPE_NAVI      | 导航地图               |
| MAP_TYPE_NIGHT     | 夜景地图               |
| MAP_TYPE_NORMAL    | 白昼地图（即普通地图） |
| MAP_TYPE_SATELLITE | 卫星图                 |

##### 2.2.5 地图控件相关参数设置

​	控件是指浮在地图图面上的一系列用于操作地图的组件，例如缩放按钮、指南针、定位按钮、比例尺等。

​	UiSettings 类用于操控这些控件，以定制自己想要的视图效果。UiSettings 类对象的实例化需要通过 AMap 类来实现：

```
private UiSettings mUiSettings;//定义一个UiSettings对象
mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
```

- 缩放按钮

```
//是否允许显示缩放按钮
UiSettings.setZoomControlsEnabled(boolean)
//设置缩放按钮的位置
UiSettings.setZoomPosition(int position)
//获取缩放按钮的位置
UiSettings.getZoomPosition()
```

- 指南针

```
//是否显示指南针
UiSettings.setCompassEnabled(boolean b);
```

-  定位按钮

		App 端用户可以通过点击定位按钮在地图上标注一个蓝色定位点，代表其当前位置。不同于以上控件，定位按钮内部的逻辑实现依赖 Android 定位 SDK。 

```
aMap.setLocationSource(this);//通过aMap对象设置定位数据源的监听

mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮

aMap.setMyLocationEnabled(true);// 可触发定位并显示当前位置
```

- 地图logo

```
mUiSettings.setLogoPosition(int position);//设置logo位置
```

​	Logo位置说明

| 名称                                    | 位置说明                 |
| --------------------------------------- | ------------------------ |
| AMapOptions.LOGO_POSITION_BOTTOM_LEFT   | LOGO边缘MARGIN（左边）   |
| AMapOptions.LOGO_MARGIN_BOTTOM          | LOGO边缘MARGIN（底部     |
| AMapOptions.LOGO_MARGIN_RIGHT           | LOGO边缘MARGIN（右边）   |
| AMapOptions.LOGO_POSITION_BOTTOM_CENTER | Logo位置（地图底部居中） |
| AMapOptions.LOGO_POSITION_BOTTOM_LEFT   | Logo位置（地图左下角）   |
| AMapOptions.LOGO_POSITION_BOTTOM_RIGHT  | Logo位置（地图右下角）   |

##### 2.2.6 手势交互

​	地图 SDK 提供了多种手势供 App 端用户与地图之间进行交互，如缩放、旋转、滑动、倾斜。这些手势默认开启，如果想要关闭某些手势，可以通过2.2.5中提到的 UiSetting 类提供的接口来控制手势的开关。 

以下是控制手势生效与否的方法：

| 名称     | 调用方法                                     |
| -------- | -------------------------------------------- |
| 缩放手势 | UiSettings.setZoomGesturesEnabled(boolean)   |
| 滑动手势 | UiSettings.setScrollGesturesEnabled(boolean) |
| 旋转手势 | UiSettings.setRotateGesturesEnabled(boolean) |
| 倾斜手势 | UiSettings.setTiltGesturesEnabled(boolean)   |
| 所有手势 | UiSettings.setAllGesturesEnabled (boolean）  |

以下是检测手势是否生效的方法：

| 名称     | 调用方法                             |
| -------- | ------------------------------------ |
| 缩放手势 | UiSettings.isZoomGesturesEnabled()   |
| 滑动手势 | UiSettings.isScrollGesturesEnabled() |
| 旋转手势 | UiSettings.isRotateGesturesEnabled() |
| 倾斜手势 | UiSettings.isTiltGesturesEnabled()   |

- 指定屏幕中心点手势

  在对地图进行手势操作时（滑动手势除外），可以指定屏幕中心点后执行相应手势。 

```
aMap.setPointToCenter(int x, int y);//x、y均为屏幕坐标，屏幕左上角为坐标原点，即(0,0)点。
```

​	开启以中心点进行手势操作的方法：

```
aMap.getUiSettings().setGestureScaleByMapCenter(true);
```

##### 2.2.7 更改地图显示的状态及内容

- 地图移动

  先指定一个中心点，中心点定义如下

```
//参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.977290,116.337000),18,30,0));
```

​	再将mCameraUpdate传入地图移动的方法中。地图移动有两个方法：

​	1) 带有地图视角移动的方法

```
//AMap类中提供，带有移动过程的动画
animateCamera(CameraUpdate cameraupdate,long duration,AMap.CancelableCallback cancelablecallback);
```

​	2) 不带地图视角移动动画的方法：

```
//AMap类中提供，直接移动过去，不带移动过程动画
moveCamera(CameraUpdate cameraupdate);
```

- 地图缩放

  如果想改变地图的缩放级别，可以通过如下方法构造改变地图缩放级别的 CameraUpdate 参数： 

  ```
      private void scaleMap() {
          CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);
          //带动画的缩放
  	    //aMap.animateCamera(mCameraUpdate);
          //不带动画的缩放
          aMap.moveCamera(mCameraUpdate);
      }
  ```

  地图的缩放级别一共分为 17 级，从 3 到 19。数字越大，展示的图面信息越精细。

| 名称   | 参数说明                       | 调用方法                                        |
| ------ | ------------------------------ | ----------------------------------------------- |
| ZoomTo | 缩放地图到指定的缩放级别       | AMap.moveCamera(CameraUpdateFactory.zoomTo(17)) |
| ZoomIn | 缩放地图到当前缩放级别的上一级 | AMap.moveCamera(CameraUpdateFactory.zoomIn())   |

##### 2.2.8 当前位置显示

- 获取初始话之后的AMap
- 实现定位的蓝点

```
MyLocationStyle myLocationStyle;
myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
```

​	 **通过MyLocationStyle来设置定位蓝点的相关属性**

```
myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。

//以下三种模式从5.1.0版本开始提供
myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
```

​	 **是否显示定位蓝点**  

```
MyLocationStyle showMyLocation(boolean visible)//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
```

​	 **自定义定位蓝点图标**

```
MyLocationStyle myLocationIcon(BitmapDescriptor myLocationIcon);
//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
```

​	 **自定义定位蓝点图标的锚点**

​	锚点是指定位蓝点图标像素与定位蓝点坐标的关联点，例如需要将图标的左下方像素点与定位蓝点的经纬度关联在一起，通过如下方法传入（0.0,1.0）。图标左上点为像素原点。 

```
MyLocationStyle anchor(float u, float v);//设置定位蓝点图标的锚点方法。
```

​	 **自定义定位的频次**

​	定位频次修改只会在定位蓝点的连续定位模式下生效，定位蓝点支持连续定位的模式是：MyLocationStyle.LOCATION_TYPE_FOLLOW，MyLocationStyle.LOCATION_TYPE_MAP_ROTATE，MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE

​	设置方法为

```
MyLocationStyle interval(long interval);
//设置定位频次方法，单位：毫秒，默认值：1000毫秒，如果传小于1000的任何值将按照1000计算。该方法只会作用在会执行连续定位的工作模式上。
```

​	 **获取经纬度信息**

​	实现 AMap.OnMyLocationChangeListener 监听器，通过如下回调方法获取经纬度信息：

```
public void onMyLocationChange(android.location.Location location)｛
   //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
}
```

##### 2.2.9 地图上添加点

- 添加默认标点

  ```
  LatLng latLng = new LatLng(39.906901,116.397972);
  final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
  ```

  **Marker 常用属性**

| 名称      | 说明                                 |
| --------- | ------------------------------------ |
| position  | 在地图上标记位置的经纬度值。必填参数 |
| title     | 点标记的标题                         |
| snippet   | 点标记的内容                         |
| draggable | 点标记是否可拖拽                     |
| visible   | 点标记是否可见                       |
| anchor    | 点标记的锚点                         |
| alpha     | 点的透明度                           |

-  添加自定义点

		java代码

```
    //新增点
    private void addPoint() {
     LatLng latLng = new LatLng(30.2829363358, 120.1232242624);
	//Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
	//final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromView(getMyView("测试文字"))));
            markers.add(marker);
    }
    
    
    protected View getMyView(String pm_val) {
        View view = getLayoutInflater().inflate(R.layout.marker, null);
        TextView tv_val = (TextView) view.findViewById(R.id.marker_tv_val);
        tv_val.setText(pm_val);
        return view;
    }
```

​	对应的xml文件

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <ImageView
        android:id="@+id/imageView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:scaleType="centerCrop"
        android:src="@mipmap/ic_launcher" />

    <TextView
        android:id="@+id/marker_tv_val"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="10dp"
        android:layout_marginBottom="7dp"
        android:background="#444444"
        android:textColor="@android:color/black" />
</LinearLayout>
```

##### 2.2.10 简单示例

```
public class AMapActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener, AMap.OnCameraChangeListener {
    //
    private MapView mMapView;
    //
    private Button button;

    private AMap aMap;

    private List<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);
        initView(savedInstanceState);
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
//                addPoint();
                scaleMap();
            }
        });
    }

    private void scaleMap() {
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);
        //带动画的缩放
//        aMap.animateCamera(mCameraUpdate);
        //不带动画的缩放
        aMap.moveCamera(mCameraUpdate);
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

    //新增点
    private void addPoint() {
        if (!isAdd) {
            isAdd = true;
            LatLng latLng = new LatLng(30.2829363358, 120.1232242624);
//            Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
//            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
            Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromView(getMyView("测试文字"))));
            markers.add(marker);
        }
        aMap.getUiSettings().setAllGesturesEnabled(!aMap.getUiSettings().isScrollGesturesEnabled());
    }

    protected View getMyView(String pm_val) {
        View view = getLayoutInflater().inflate(R.layout.marker, null);
        TextView tv_val = (TextView) view.findViewById(R.id.marker_tv_val);
        tv_val.setText(pm_val);
        return view;
    }


    boolean isMove = false;
    Location nowLocation;

    @Override
    public void onMyLocationChange(Location location) {
        nowLocation = location;
        if (!isMove) {
            isMove = true;
            //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取（获取地址描述数据章节有介绍）
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(location.getLatitude(), location.getLongitude()), 12, 0, 0));
            //移动到当前的位置
            aMap.moveCamera(mCameraUpdate);
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (markers.size() != 0) {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
        isAdd = false;
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        Toast.makeText(getApplicationContext(), "move finish", Toast.LENGTH_SHORT).show();
        getAMapZoom();
    }
}
```

对应的xml代码

```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.innotek.parking.patrol.MainActivity">


    <com.amap.api.maps.MapView
        android:id="@+id/map_mapview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="1.0" />

    <Button
        android:id="@+id/btn_map"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginLeft="8dp"
        android:layout_marginEnd="8dp"
        android:layout_marginRight="8dp"
        android:layout_marginBottom="8dp"
        android:text="Button"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

</android.support.constraint.ConstraintLayout>
```

另外自定义的标点请看2.2.9中的xml代码



#### 2.3 获取地理编码/逆地理编码

- 地理编码，又称为地址匹配，是从已知的结构化地址描述到对应的经纬度坐标的转换过程。该功能适用于根据用户输入的地址确认用户具体位置的场景，常用于配送人员根据用户输入的具体地址找地点。

结构化地址的定义：   首先，地址肯定是一串字符，内含国家、省份、城市、城镇、乡村、街道、门牌号码、屋邨、大厦等建筑物名称。按照由大区域名称到小区域名称组合在一起的字符。一个有效的地址应该是独一无二的。注意：针对大陆、港、澳地区的地理编码转换时可以将国家信息选择性的忽略，但省、市、城镇等级别的地址构成是不能忽略的。

**注意：该功能可以返回一部分POI数据内容，但核心能力是完成结构化地址到经纬度的转换。**

​	POI关键字搜索，是根据关键词找到现实中存在的地物点（POI）。

​	地理编码是依据当前输入，根据标准化的地址结构（省/市/区或县/乡/村或社区/商圈/街道/门牌号/POI）进行各个地址级别的匹配，以确认输入地址对应的地理坐标，只有返回的地理坐标匹配的级别为POI，才会对应一个具体的地物（POI）。

根据给定的地理名称和查询城市，返回地理编码的结果列表。



- 逆地理编码，又称地址解析服务，是指从已知的经纬度坐标到对应的地址描述（如行政区划、街区、楼层、房间等）的转换。**常用于根据定位的坐标来获取该地点的位置详细信息，与定位功能是黄金搭档。** 



##### 2.3.1 添加依赖

​	详见2.2.1

##### 2.3.2 配置AndroidManifest.xml

​	详见2.2.2

##### 2.3.3 地理编码

实现步骤如下：

1、继承 OnGeocodeSearchListener 监听。

2、构造 GeocodeSearch 对象，并设置监听。

```
geocoderSearch = new GeocodeSearch(this);
geocoderSearch.setOnGeocodeSearchListener(this);
```

3、通过  GeocodeQuery(java.lang.String locationName, java.lang.String city)  设置查询参数，调用 GeocodeSearch 的 getFromLocationNameAsyn(GeocodeQuery  geocodeQuery) 方法发起请求。

```
// name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode  
GeocodeQuery query = new GeocodeQuery(name, "010");  

geocoderSearch.getFromLocationNameAsyn(query);  
```

4、通过回调接口 onGeocodeSearched 解析返回的结果。

1）可以在回调中解析result，获取坐标信息。

2）返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）

```
@Override
public void onGeocodeSearched(GeocodeResult result, int rCode) {
    //解析result获取坐标信息
} 
```

##### 2.3.4 逆地理编码

1、继承 OnGeocodeSearchListener 监听。

2、构造 GeocodeSearch 对象，并设置监听。

```
geocoderSearch = new GeocodeSearch(this);
geocoderSearch.setOnGeocodeSearchListener(this);
```

3、通过 RegeocodeQuery(LatLonPoint  point, float radius, java.lang.String latLonType) 设置查询参数，调用  GeocodeSearch 的 getFromLocationAsyn(RegeocodeQuery regeocodeQuery)  方法发起请求。

```
// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);

geocoderSearch.getFromLocationAsyn(query);
```

4、通过回调接口 onRegeocodeSearched 解析返回的结果。

1）可以在回调中解析result，获取地址、adcode等等信息。

2）返回结果成功或者失败的响应码。1000为成功，其他为失败（详细信息参见网站开发指南-实用工具-错误码对照表）

```
@Override
public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
    //解析result获取地址描述信息
}
```

##### 2.3.5 简单示例

```
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
```
