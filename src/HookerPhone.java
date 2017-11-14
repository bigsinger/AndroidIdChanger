package com.bigsing.changer.hook;

import android.content.ContentResolver;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bigsing.changer.PhoneValue;
import com.bigsing.util.Constant;
import com.bigsing.util.PhoneInfoUtils;
import com.bigsing.util.PreferencesUtils;
import com.bigsing.util.XposedLog;

import java.util.LinkedHashMap;

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
    private static LinkedHashMap<String, String> mHardcodedPhoneInfo = null;

    public static LinkedHashMap<String, String> getPhoneInfo() {
        if (mHardcodedPhoneInfo == null) {
            initHardcodedPhoneInfo();
        }
        return mHardcodedPhoneInfo;
    }

    private static void initHardcodedPhoneInfo() {
        if (mHardcodedPhoneInfo == null) {
            PhoneValue phoneValue = new PhoneValue();
            mHardcodedPhoneInfo = phoneValue.getHardcodedPhoneInfo(null);
        }
    }

    private static String getHookValue(String key) {
        String modValue = null;
        if (Constant.HARDCODED) {
            modValue = mHardcodedPhoneInfo.get(key);
        } else {
            modValue = PhoneInfoUtils.getValue(key);
        }

        return modValue;
    }

    public static void hook(XC_LoadPackage.LoadPackageParam param) {
        XposedLog.log("hook begin");    //绝对输出
        String modValue = null;
        Constant.HARDCODED = PreferencesUtils.isUseHardcodedValueOnXposed();
        if (Constant.HARDCODED) {
            initHardcodedPhoneInfo();
        }

        //////////////////////////////////////////////////////////////////////////
        // IMEI
        modValue = getHookValue("IMEI");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook IMEI, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                if (Build.VERSION.SDK_INT < 22) {  //android.os.Build.VERSION_CODES.LOLLIPOP_MR1
                    findAndHookMethod("com.android.internal.telephony.gsm.GSMPhone", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
                    findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
                    findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getDeviceId", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                                throws Throwable {
                            hookParam.setResult(hookValue);
                            super.afterHookedMethod(hookParam);
                        }
                    }
                    });
                    findAndHookMethod("com.android.internal.telephony.PhoneProxy", param.classLoader, "getDeviceId", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
                } else if (Build.VERSION.SDK_INT == 22) {  //android.os.Build.VERSION_CODES.LOLLIPOP_MR1
                    findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", param.classLoader, "getDeviceId", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });

                    findAndHookMethod("com.android.internal.telephony.PhoneProxy", param.classLoader, "getDeviceId", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });

                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                } else if (Build.VERSION.SDK_INT >= 23) { //23 android.os.Build.VERSION_CODES.M
                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getImei", new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getDeviceId", Integer.TYPE, new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                    findAndHookMethod("android.telephony.TelephonyManager", param.classLoader, "getImei", Integer.TYPE, new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            param.setResult(hookValue);
                        }
                    });
                }
                XposedLog.logd("hooked IMEI with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking IMEI");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // PhoneNumber
        modValue = getHookValue("PhoneNumber");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook PhoneNumber, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getLine1Number", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked PhoneNumber with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking PhoneNumber");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // WifiMac
        modValue = getHookValue("WifiMac");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook WifiMac, it's  null");
        } else {
            final String hookValue = modValue;

            ///////////////////////////
            //hook getMacAddress anyway
            ///////////////////////////
            //ref: https://developer.android.com/reference/android/net/wifi/WifiInfo.html#getMacAddress()
            try {
                findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getMacAddress", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked WifiMac with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking WifiMac");
            }
            ///////////////////////////

            if (Build.VERSION.SDK_INT >= 24) {
                //ref: https://developer.android.com/reference/android/app/admin/DevicePolicyManager.html#getWifiMacAddress(android.content.ComponentName)
                try {
                    findAndHookMethod("android.app.admin.DevicePolicyManager", param.classLoader, "getWifiMacAddress", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                                throws Throwable {
                            hookParam.setResult(hookValue);
                            super.afterHookedMethod(hookParam);
                        }
                    }
                    });
                    XposedLog.logd("hooked WifiMac with " + hookValue);
                } catch (Exception e) {
                    XposedLog.loge("error hooking WifiMac");
                }
            }

        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // BluetoothMac
        modValue = getHookValue("BluetoothMac");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook BluetoothMac, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod("android.bluetooth.BluetoothAdapter", param.classLoader, "getAddress", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
                XposedLog.logd("hooked BluetoothMac with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking BluetoothMac");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // Wifissid
        modValue = getHookValue("Wifissid");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook Wifissid, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getSSID", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked Wifissid with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Wifissid");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // WifiBssid
        modValue = getHookValue("WifiBssid");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook WifiBssid, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getBSSID", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked WifiBssid with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking WifiBssid");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SubscriberId
        modValue = getHookValue("SubscriberId");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook SubscriberId, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSubscriberId", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked SubscriberId with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SubscriberId");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SimOperator
        modValue = getHookValue("SimOperator");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook SimOperator, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimOperator", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked SimOperator with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SimOperator");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SimOperatorName
        modValue = getHookValue("SimOperatorName");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook SimOperatorName, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimOperatorName", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked SimOperatorName with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SimOperatorName");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SimCountryIso
        modValue = getHookValue("SimCountryIso");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook SimCountryIso, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimCountryIso", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked SimCountryIso with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SimCountryIso");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SimState
        modValue = getHookValue("SimState");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook SimState, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(TelephonyManager.class.getName(), param.classLoader, "getSimState", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        int nState = SIM_STATE_UNKNOWN;
                        try {
                            nState = Integer.parseInt(hookValue);
                        } catch (Exception e) {
                            nState = SIM_STATE_UNKNOWN;
                        }
                        hookParam.setResult(nState);
                    }
                }
                });
                XposedLog.logd("hooked SimState with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SimState");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // AndroidId
        modValue = getHookValue("AndroidId");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook AndroidId, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(Settings.Secure.class.getName(), param.classLoader, "getString", new Object[]{ContentResolver.class.getName(), String.class.getName(), new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        if (hookParam.args[1].equals("android_id"))
                            hookParam.setResult(PhoneInfoUtils.getValue("AndroidId"));
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
                XposedLog.logd("hooked AndroidId with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking AndroidId");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // SDK
        modValue = getHookValue("SDK");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook SDK, it's  null");
        } else {
            int sdkInt = 19;
            try {
                sdkInt = Integer.parseInt(modValue);
            } catch (Exception e) {
                sdkInt = 19;
            }
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK_INT", sdkInt);
                XposedLog.logd("hooked SDK_INT with " + sdkInt);
            } catch (Exception e) {
                XposedLog.loge("error hooking SDK");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // Release
        modValue = getHookValue("Release");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook RELEASE, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", hookValue);
                XposedLog.logd("hooked RELEASE with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking RELEASE");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // INCREMENTAL
        modValue = getHookValue("Incremental");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook INCREMENTAL, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "INCREMENTAL", hookValue);
                XposedLog.logd("hooked INCREMENTAL with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking INCREMENTAL");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // Build.VERSION.CODENAME
        modValue = getHookValue("CODENAME");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook Build.VERSION.CODENAME, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "CODENAME", hookValue);
                XposedLog.logd("hooked Build.VERSION.CODENAME with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.VERSION.CODENAME");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SERIAL
        modValue = getHookValue("SerialNo");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook SERIAL, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "SERIAL", hookValue);
                XposedLog.logd("hooked SERIAL with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking SERIAL");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // MODEL
        modValue = getHookValue("Model");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook MODEL, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "MODEL", hookValue);
                XposedLog.logd("hooked MODEL with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking MODEL");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // MANUFACTURER
        modValue = getHookValue("Manufacturer");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook MODEL, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", hookValue);
                XposedLog.logd("hooked MANUFACTURER with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking MANUFACTURER");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // HARDWARE
        modValue = getHookValue("Hardware");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook HARDWARE, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", hookValue);
                XposedLog.logd("hooked HARDWARE with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking HARDWARE");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // BRAND
        modValue = getHookValue("Brand");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook BRAND, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "BRAND", hookValue);
                XposedLog.logd("hooked BRAND with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking BRAND");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // BuildID
        modValue = getHookValue("BuildID");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook Build.ID, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "ID", hookValue);
                XposedLog.logd("hooked Build.ID with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.ID");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // BOOTLOADER
        modValue = getHookValue("BootLoader");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook Build.BOOTLOADER, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "BOOTLOADER", hookValue);
                XposedLog.logd("hooked Build.BOOTLOADER with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.BOOTLOADER");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // RADIO
        modValue = getHookValue("RadioVersion");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook Build.RADIO, it's  null");
        } else {
            final String hookValue = modValue;
            if (Build.VERSION.SDK_INT < 14) {
                try {
                    XposedHelpers.setStaticObjectField(Build.class, "RADIO", hookValue);
                    XposedLog.logd("hooked Build.RADIO with " + hookValue);
                } catch (Exception e) {
                    XposedLog.loge("error hooking Build.RADIO");
                }
            } else {  //Build.VERSION.SDK_INT >= 14
                try {
                    findAndHookMethod(Build.class.getName(), param.classLoader, "getRadioVersion", new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                                throws Throwable {
                            hookParam.setResult(hookValue);
                            super.afterHookedMethod(hookParam);
                        }
                    }
                    });
                    XposedLog.logd("hooked Build.RADIO with " + hookValue);
                } catch (Exception e) {
                    XposedLog.loge("error hooking Build.RADIO");
                }
            }

        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // TAGS
        modValue = getHookValue("TAGS");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook Build.TAGS, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "TAGS", hookValue);
                XposedLog.logd("hooked Build.TAGS with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.TAGS");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // TIME
        modValue = getHookValue("TIME");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook Build.TIME, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "TIME", hookValue);
                XposedLog.logd("hooked Build.TIME with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.TIME");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // TYPE
        modValue = getHookValue("TYPE");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook Build.TYPE, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "TYPE", hookValue);
                XposedLog.logd("hooked Build.TYPE with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking Build.TYPE");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // TIME
        modValue = getHookValue("TIME");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook TIME, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "TIME", hookValue);
                XposedLog.logd("hooked TIME with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking TIME");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // USER
        modValue = getHookValue("USER");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook USER, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "USER", hookValue);
                XposedLog.logd("hooked USER with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking USER");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // CPU_ABI
        modValue = getHookValue("CPU_ABI");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook CPU_ABI, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI", hookValue);
                XposedLog.logd("hooked CPU_ABI with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking CPU_ABI");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // CPU_ABI2
        modValue = getHookValue("CPU_ABI2");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook CPU_ABI2, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI2", hookValue);
                XposedLog.logd("hooked CPU_ABI2 with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking CPU_ABI2");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // BOARD
        modValue = getHookValue("Board");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook BOARD, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "BOARD", hookValue);
                XposedLog.logd("hooked BOARD with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking BOARD");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // DEVICE
        modValue = getHookValue("Device");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook DEVICE, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "DEVICE", hookValue);
                XposedLog.logd("hooked DEVICE with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking DEVICE");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // PRODUCT
        modValue = getHookValue("Product");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook PRODUCT, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", hookValue);
                XposedLog.logd("hooked PRODUCT with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking PRODUCT");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // HOST
        modValue = getHookValue("Host");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook HOST, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "HOST", hookValue);
                XposedLog.logd("hooked HOST with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking HOST");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // DISPLAY
        modValue = getHookValue("Display");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook DISPLAY, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", hookValue);
                XposedLog.logd("hooked DISPLAY with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking DISPLAY");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // FINGERPRINT
        modValue = getHookValue("FingerPrint");
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("no hook FINGERPRINT, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", hookValue);
                XposedLog.logd("hooked FINGERPRINT with " + hookValue);
            } catch (Exception e) {
                XposedLog.loge("error hooking FINGERPRINT");
            }
        }
        //////////////////////////////////////////////////////////////////////////
        XposedLog.log("hook end\r\n");  //绝对输出
    }

}