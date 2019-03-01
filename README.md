[网易招聘内部职位推荐（定期更新）](https://www.zhupite.com/job/nejobs.html)

# AndroidIdChanger（安卓手机设备信息查看/安卓改机工具）LatestVersion:1.24
- 如果Xposed没安装或者没激活，则默认显示原始数值，这样工具作用等于安卓手机设备信息查看工具。
- 如果安装Xposed并激活该工具，则可以实现改机效果，伪装成不同的手机设备。
- 新增**环境检测**功能，主要用Native的方式获取机器的真实信息，持续增加。
- 一键随机修改设备信息
- 支持硬编码的设备信息
- 支持email发送设备信息，主要是方便测试
- 不同App使用不同的伪装配置信息，在一键改机后指定App。
- 支持自定义设备信息，在**data/data/com.xxx.xxx/shared_prefs/phoneinfo.xml**中，可以在电脑中修改该文件，然后覆盖替换掉手机里的这个文件，即刻生效，无须重启手机，但是需要重启App。
- Wifi Mac地址获取方式修改，解决Android6.0以上系统获取为02:00:00:00:00:00的问题，且获取多个，以供参考。

如有问题或建议，请在这里留言：[Issues](https://github.com/bigsinger/AndroidIdChanger/issues)，当然我并不一定会有时间修复和解决。

## 中文 (Chinese):

- 查看并修改安卓手机设备信息：
IMEI、AndroidID、Wifi Mac、SerialNum、Wifi SSID、手机号、Bluetooth Mac、Google Ad Id、Wifi BSSID、IMSI、Sim卡状态、运营商信息、机器型号、制造商、品牌、系统版本、CPU_ABI、CPU_ABI2、SDK、BuildID、分辨率、IP等。

### 使用方法：
- 安装APP，运行APP即可查看手机信息，可以随机修改信息
- 安装Xposed
- 在Xposed框架中启用本模块
- 重启手机
- 后面再做的修改一律不需要重启手机

## English:

- View and Change the value of IMEI, Android Id, Serial Number, Wifi Mac Address and service set identifier (SSID) of the current wifi network, Google advertising id, Bluetooth mac address, and so on.

- You can random or manual set these values.

- Can apply the values System Wide or in Per APP basis.

### How to use :
- Install app.
- Enable module in xposed framework.
- Open app and enter new values.
- Reboot.
- No need to reboot the phone later when random the info.

## Thanks（感谢）
- [lars18th](https://github.com/lars18th)

## Example Screenshots

![](https://github.com/bigsinger/AndroidIdChanger/blob/master/screenshot/1.png)

![](https://github.com/bigsinger/AndroidIdChanger/blob/master/screenshot/2.png)


