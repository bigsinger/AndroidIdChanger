[网易招聘内部职位推荐（定期更新）](http://www.zhupite.com/posts/nejobs.html)

# AndroidIdChanger
## 1.22
- 如果xposed没安装或者没激活，则默认显示原始数值，这样工具作用等于设备信息查看器。
- Wifi Mac地址获取方式修改，解决Android6.0以上系统获取为02:00:00:00:00:00的问题，且获取多个，以供参考。

## 中文 (Chinese):

- 查看并修改安卓手机设备信息：
IMEI、AndroidID、Wifi Mac、SerialNum、Wifi SSID、手机号、Bluetooth Mac、Google Ad Id、Wifi BSSID、IMSI、Sim卡状态、运营商信息、机器型号、制造商、品牌、系统版本、CPU_ABI、CPU_ABI2、SDK、BuildID、分辨率、IP等。

### 使用方法：
- 安装APP，运行APP即可查看手机信息，可以随机修改信息
- 安装Xposed
- 在Xposed框架中启用本模块
- 重启手机

## English:

- View and Change the value of IMEI, Android Id, Serial Number, Wifi Mac Address and service set identifier (SSID) of the current wifi network, Google advertising id, Bluetooth mac address, and so on.

- You can random or manual set these values.

- Can apply the values System Wide or in Per APP basis.

### How to use :
- Install app.
- Enable module in xposed framework.
- Open app and enter new values.
- Reboot.

## Thanks（感谢）
- [lars18th](https://github.com/lars18th)

## Example Screenshots

![](https://github.com/bigsinger/AndroidIdChanger/blob/master/screenshot/1.png)

![](https://github.com/bigsinger/AndroidIdChanger/blob/master/screenshot/2.png)
