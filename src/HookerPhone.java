package com.bigsing.changer.hooks;

import android.content.ContentResolver;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.bigsing.changer.util.PhoneInfoUtils;
import com.bigsing.changer.util.XposedLog;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by sing on 2017/4/18.
 */

public class HookerPhone {
    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        XposedLog.logd("HookerPhone.hook : 1");

        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {  //22
            findAndHookMethod("com.android.internal.telephony.gsm.GSMPhone", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(PhoneInfoUtils.getValue("imei"))});
            findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(PhoneInfoUtils.getValue("imei"))});
            XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getDeviceId", new Object[]{new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                        throws Throwable {
                    XposedLog.logd("onHook 1 before");
                    hookParam.setResult(PhoneInfoUtils.getValue("imei"));
                    super.afterHookedMethod(hookParam);
                    XposedLog.logd("onHook 1 after");
                }
            }
            });
            findAndHookMethod("com.android.internal.telephony.PhoneProxy", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(PhoneInfoUtils.getValue("imei"))});
        } else if (Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {  //22
            try {
                XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", param.classLoader, "getDeviceId", new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedLog.logd("onHook 2 before");
                        super.afterHookedMethod(param);
                        param.setResult(PhoneInfoUtils.getValue("imei"));
                        XposedLog.logd("onHook 2 after");
                    }
                });
            } catch (Exception v0) {
                v0.printStackTrace();
            }

            try {
                XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneProxy", param.classLoader, "getDeviceId", new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedLog.logd("onHook 3 before");
                        super.afterHookedMethod(param);
                        param.setResult(PhoneInfoUtils.getValue("imei"));
                        XposedLog.logd("onHook 3 after");
                    }
                });
            } catch (Exception v0) {
                v0.printStackTrace();
            }

            try {
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", param.classLoader,
                        "getDeviceId", new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedLog.logd("onHook 4 before");
                                super.afterHookedMethod(param);
                                param.setResult(PhoneInfoUtils.getValue("imei"));
                                XposedLog.logd("onHook 4 after");
                            }
                        });
            } catch (Exception v0) {
                v0.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT >= 23) { //23 android.os.Build.VERSION_CODES.M
            XposedLog.logd("HookerPhone.hook : 2");
            try {
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedLog.logd("onHook 5 before");
                                super.afterHookedMethod(param);
                                param.setResult(PhoneInfoUtils.getValue("imei"));
                                XposedLog.logd("onHook 5 after");
                            }
                        });

            } catch (Exception v0) {
                v0.printStackTrace();
            }

            try {
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getImei", new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedLog.logd("onHook 6 before");
                                super.afterHookedMethod(param);
                                param.setResult(PhoneInfoUtils.getValue("imei"));
                                XposedLog.logd("onHook 6 after");
                            }
                        });
            } catch (Exception v0) {
                v0.printStackTrace();
            }

            try {
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", Integer.TYPE, new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedLog.logd("onHook 7 before");
                                super.afterHookedMethod(param);
                                param.setResult(PhoneInfoUtils.getValue("imei"));
                                XposedLog.logd("onHook 7 after");
                            }
                        });
            } catch (Exception v0) {
                v0.printStackTrace();
            }

            try {
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getImei", Integer.TYPE, new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                XposedLog.logd("onHook 8 before");
                                super.afterHookedMethod(param);
                                param.setResult(PhoneInfoUtils.getValue("imei"));
                                XposedLog.logd("onHook 8 after");
                            }
                        });
            } catch (Exception v0) {
                v0.printStackTrace();
            }
        }

        XposedLog.logd("HookerPhone.hook : 3");
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getLine1Number", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 9 before");
                hookParam.setResult(PhoneInfoUtils.getValue("tel"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 9 after");
            }
        }
        });
        XposedLog.logd("HookerPhone.hook : 4");
        XposedHelpers.findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getMacAddress", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 10 before");
                hookParam.setResult(PhoneInfoUtils.getValue("wifimac"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 10 after");
            }
        }
        });

        XposedLog.logd("HookerPhone.hook : 5");
        findAndHookMethod("android.bluetooth.BluetoothAdapter", param.classLoader, "getAddress", new Object[]{XC_MethodReplacement.returnConstant(PhoneInfoUtils.getValue("bluetoothmac"))});

        XposedLog.logd("HookerPhone.hook : 6");
        XposedHelpers.findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getSSID", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 11 before");
                hookParam.setResult(PhoneInfoUtils.getValue("wifissid"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 11 after");
            }
        }
        });
        XposedLog.logd("HookerPhone.hook : 7");
        XposedHelpers.findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getBSSID", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 12 before");
                hookParam.setResult(PhoneInfoUtils.getValue("wifibssid"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 12 after");
            }
        }
        });
        XposedLog.logd("HookerPhone.hook : 8");
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSubscriberId", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 13 before");
                hookParam.setResult(PhoneInfoUtils.getValue("subscriberid"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 13 after");
            }
        }
        });
        XposedLog.logd("HookerPhone.hook : 9");
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimOperator", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 14 before");
                hookParam.setResult(PhoneInfoUtils.getValue("simoperator"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 14 after");
            }
        }
        });
        XposedLog.logd("HookerPhone.hook : 10");
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimOperatorName", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 15 before");
                hookParam.setResult(PhoneInfoUtils.getValue("simoperatorname"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 15 after");
            }
        }
        });
        XposedLog.logd("HookerPhone.hook : 11");
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimCountryIso", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 16 before");
                hookParam.setResult(PhoneInfoUtils.getValue("simcountryiso"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 16 after");
            }
        }
        });
        XposedLog.logd("HookerPhone.hook : 12");
        XposedHelpers.findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimState", new Object[]{new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 17 before");
                int nState = SIM_STATE_UNKNOWN;
                try {
                    nState = Integer.parseInt(PhoneInfoUtils.getValue("simstate"));
                } catch (Exception e) {
                    nState = SIM_STATE_UNKNOWN;
                }
                hookParam.setResult(nState);
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 17 after");
            }
        }
        });
        XposedLog.logd("HookerPhone.hook : 13");
        XposedHelpers.findAndHookMethod(Settings.Secure.class.getName(), param.classLoader, "getString", new Object[]{ContentResolver.class.getName(), String.class.getName(), new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                    throws Throwable {
                XposedLog.logd("onHook 18 before");
                if (hookParam.args[1].equals("android_id"))
                    hookParam.setResult(PhoneInfoUtils.getValue("androidid"));
                super.afterHookedMethod(hookParam);
                XposedLog.logd("onHook 18 after");
            }
        }
        });

        int sdkInt = 19;
        try {
            sdkInt = Integer.parseInt(PhoneInfoUtils.getValue("sdk"));
        } catch (Exception e) {
            sdkInt = 19;
        }
        XposedLog.logd("HookerPhone.hook : 14");
        XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK_INT", sdkInt);
        XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", PhoneInfoUtils.getValue("release"));
        XposedHelpers.setStaticObjectField(Build.VERSION.class, "INCREMENTAL", "");
        XposedLog.logd("HookerPhone.hook : 15");

        XposedHelpers.setStaticObjectField(Build.class, "SERIAL", PhoneInfoUtils.getValue("serial"));
        XposedHelpers.setStaticObjectField(Build.class, "MODEL", PhoneInfoUtils.getValue("model"));
        XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", PhoneInfoUtils.getValue("manufacturer"));
        XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", PhoneInfoUtils.getValue("hardware"));
        XposedLog.logd("HookerPhone.hook : 16");
        XposedHelpers.setStaticObjectField(Build.class, "BRAND", PhoneInfoUtils.getValue("brand"));
        XposedHelpers.setStaticObjectField(Build.class, "ID", PhoneInfoUtils.getValue("buildid"));
        XposedHelpers.setStaticObjectField(Build.class, "USER", PhoneInfoUtils.getValue("user"));
        XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI", PhoneInfoUtils.getValue("cpu_abi"));
        XposedLog.logd("HookerPhone.hook : 17");
        XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI2", PhoneInfoUtils.getValue("cpu_abi2"));
        XposedHelpers.setStaticObjectField(Build.class, "BOARD", PhoneInfoUtils.getValue("board"));
        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", PhoneInfoUtils.getValue("device"));
        XposedLog.logd("HookerPhone.hook : 18");
        XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", PhoneInfoUtils.getValue("product"));
        XposedHelpers.setStaticObjectField(Build.class, "HOST", PhoneInfoUtils.getValue("host"));
        XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", PhoneInfoUtils.getValue("display"));
        XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", "");
        XposedLog.logd("HookerPhone.hook : 19");
    }

}



