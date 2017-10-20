package com.bigsing.changer.hooks;

import android.content.ContentResolver;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bigsing.util.Constant;
import com.bigsing.util.PhoneInfoUtils;
import com.bigsing.util.XposedLog;
import com.blankj.utilcode.utils.StringUtils;

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
        XposedLog.logd("HookerPhone.hook() begin");
        String modValue = null;

        //////////////////////////////////////////////////////////////////////////
        // IMEI
        if (Constant.HARDCODED) {
            modValue = "1234567890123456";
        } else {
            modValue = PhoneInfoUtils.getValue("IMEI");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook IMEI, it's  null");
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
                }else if (Build.VERSION.SDK_INT >= 23) { //23 android.os.Build.VERSION_CODES.M
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking IMEI");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // PhoneNumber
        if (Constant.HARDCODED) {
            modValue = "13456789012";
        } else {
            modValue = PhoneInfoUtils.getValue("PhoneNumber");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook PhoneNumber, it's  null");
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking PhoneNumber");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // WifiMac
        if (Constant.HARDCODED) {
            modValue = "00:00:00:00:00:00";
        } else {
            modValue = PhoneInfoUtils.getValue("WifiMac");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook WifiMac, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(WifiInfo.class.getName(), param.classLoader, "getMacAddress", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        hookParam.setResult(hookValue);
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking WifiMac");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // BluetoothMac
        if (Constant.HARDCODED) {
            modValue = "00:00:00:00:00:00";
        } else {
            modValue = PhoneInfoUtils.getValue("BluetoothMac");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook BluetoothMac, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod("android.bluetooth.BluetoothAdapter", param.classLoader, "getAddress", new Object[]{XC_MethodReplacement.returnConstant(hookValue)});
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking BluetoothMac");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // Wifissid
        if (Constant.HARDCODED) {
            modValue = "00:00:00:00:00:00";
        } else {
            modValue = PhoneInfoUtils.getValue("Wifissid");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook Wifissid, it's  null");
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking Wifissid");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // WifiBssid
        if (Constant.HARDCODED) {
            modValue = "00:00:00:00:00:00";
        } else {
            modValue = PhoneInfoUtils.getValue("WifiBssid");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook WifiBssid, it's  null");
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking WifiBssid");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SubscriberId
        if (Constant.HARDCODED) {
            modValue = "";
        } else {
            modValue = PhoneInfoUtils.getValue("SubscriberId");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook SubscriberId, it's  null");
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking SubscriberId");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SimOperator
        if (Constant.HARDCODED) {
            modValue = "";
        } else {
            modValue = PhoneInfoUtils.getValue("SimOperator");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook SimOperator, it's  null");
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking SimOperator");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SimOperatorName
        if (Constant.HARDCODED) {
            modValue = "";
        } else {
            modValue = PhoneInfoUtils.getValue("SimOperatorName");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook SimOperatorName, it's  null");
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking SimOperatorName");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SimCountryIso
        if (Constant.HARDCODED) {
            modValue = "";
        } else {
            modValue = PhoneInfoUtils.getValue("SimCountryIso");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook SimCountryIso, it's  null");
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking SimCountryIso");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SimState
        if (Constant.HARDCODED) {
            modValue = "1";
        } else {
            modValue = PhoneInfoUtils.getValue("SimState");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook SimState, it's  null");
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
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking SimState");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // AndroidId
        if (Constant.HARDCODED) {
            modValue = "b89ab8a123abcd7b";
        } else {
            modValue = PhoneInfoUtils.getValue("AndroidId");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook AndroidId, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                findAndHookMethod(Settings.Secure.class.getName(), param.classLoader, "getString", new Object[]{ContentResolver.class.getName(), String.class.getName(), new XC_MethodHook()  {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam hookParam)
                            throws Throwable {
                        if (hookParam.args[1].equals("android_id"))
                            hookParam.setResult(PhoneInfoUtils.getValue("AndroidId"));
                        super.afterHookedMethod(hookParam);
                    }
                }
                });
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking AndroidId");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // SDK
        if (Constant.HARDCODED) {
            modValue = "19";
        } else {
            modValue = PhoneInfoUtils.getValue("SDK");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook SDK, it's  null");
        } else {
            int sdkInt = 19;
            try {
                sdkInt = Integer.parseInt(modValue);
            } catch (Exception e) {
                sdkInt = 19;
            }
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK_INT", sdkInt);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking SDK");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // Release
        if (Constant.HARDCODED) {
            modValue = "4.4.4";
        } else {
            modValue = PhoneInfoUtils.getValue("Release");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook RELEASE, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking RELEASE");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // INCREMENTAL
        if (Constant.HARDCODED) {
            modValue = "m35x.Flyme_OS_3.7.3.20140909113158";
        } else {
            modValue = PhoneInfoUtils.getValue("Incremental");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook INCREMENTAL, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.VERSION.class, "INCREMENTAL", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking INCREMENTAL");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // SERIAL
        if (Constant.HARDCODED) {
            modValue = "897EEPMFJE0P";
        } else {
            modValue = PhoneInfoUtils.getValue("SerialNo");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook SERIAL, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "SERIAL", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking SERIAL");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // MODEL
        if (Constant.HARDCODED) {
            modValue = "M351";
        } else {
            modValue = PhoneInfoUtils.getValue("Model");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook MODEL, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "MODEL", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking MODEL");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // MANUFACTURER
        if (Constant.HARDCODED) {
            modValue = "Meizu";
        } else {
            modValue = PhoneInfoUtils.getValue("Manufacturer");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook MODEL, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking MANUFACTURER");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // HARDWARE
        if (Constant.HARDCODED) {
            modValue = "mx3";
        } else {
            modValue = PhoneInfoUtils.getValue("Hardware");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook HARDWARE, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking HARDWARE");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // BRAND
        if (Constant.HARDCODED) {
            modValue = "Meizu";
        } else {
            modValue = PhoneInfoUtils.getValue("Brand");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook BRAND, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "BRAND", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking BRAND");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // BuildID
        if (Constant.HARDCODED) {
            modValue = "KTU84P";
        } else {
            modValue = PhoneInfoUtils.getValue("BuildID");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook ID, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "ID", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking ID");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // USER
        if (Constant.HARDCODED) {
            modValue = "flyme";
        } else {
            modValue = PhoneInfoUtils.getValue("USER");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook USER, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "USER", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking USER");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // CPU_ABI
        if (Constant.HARDCODED) {
            modValue = "armeabi-v7a";
        } else {
            modValue = PhoneInfoUtils.getValue("CPU_ABI");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook CPU_ABI, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking CPU_ABI");
            }
        }
        //////////////////////////////////////////////////////////////////////////


        //////////////////////////////////////////////////////////////////////////
        // CPU_ABI2
        if (Constant.HARDCODED) {
            modValue = "armeabi";
        } else {
            modValue = PhoneInfoUtils.getValue("CPU_ABI2");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook CPU_ABI2, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI2", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking CPU_ABI2");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // BOARD
        if (Constant.HARDCODED) {
            modValue = "mx3";
        } else {
            modValue = PhoneInfoUtils.getValue("Board");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook BOARD, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "BOARD", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking BOARD");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // DEVICE
        if (Constant.HARDCODED) {
            modValue = "mx3";
        } else {
            modValue = PhoneInfoUtils.getValue("Device");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook DEVICE, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "DEVICE", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking DEVICE");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // PRODUCT
        if (Constant.HARDCODED) {
            modValue = "meizu_mx3";
        } else {
            modValue = PhoneInfoUtils.getValue("Product");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook PRODUCT, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking PRODUCT");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // HOST
        if (Constant.HARDCODED) {
            modValue = "mz-builder-1";
        } else {
            modValue = PhoneInfoUtils.getValue("Host");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook HOST, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "HOST", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking HOST");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // DISPLAY
        if (Constant.HARDCODED) {
            modValue = "Flyme OS 3.7.3A";
        } else {
            modValue = PhoneInfoUtils.getValue("Display");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook DISPLAY, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking DISPLAY");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////
        // FINGERPRINT
        if (Constant.HARDCODED) {
            modValue = "Meizu/meizu_mx3/mx3:4.4.4/KTU84P/m35x.Flyme_OS_3.7.3.20140909113158:user/release-keys";
        } else {
            modValue = PhoneInfoUtils.getValue("FingerPrint");
        }
        if (TextUtils.isEmpty(modValue)) {
            XposedLog.logd("HookerPhone.hook(): no hook FINGERPRINT, it's  null");
        } else {
            final String hookValue = modValue;
            try {
                XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", hookValue);
            } catch (Exception e) {
                XposedLog.logd("HookerPhone.hook() : error hooking FINGERPRINT");
            }
        }
        //////////////////////////////////////////////////////////////////////////

        XposedLog.logd(Constant.TAG + ": HookerPhone.hook() end");
    }

}



