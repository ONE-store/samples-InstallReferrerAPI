package com.onestore.android.samples.installreferrer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.onestore.android.external.installreferrer.IGetInstallReferrerService;

import java.util.List;

public class ReferrerClientActivity extends AppCompatActivity {
    private final static String TAG = "ReferrerClientActivity";
    /**
     * Service binder.
     */
    private IGetInstallReferrerService mService;
    private final static String[] SERVICE_PACKAGE_NAMES = {"com.skt.skaf.A000Z00040", "com.kt.olleh.storefront", "com.kt.olleh.istore", "com.lguplus.appstore", "android.lgt.appstore"};
    private final static String SERVICE_NAME = "com.onestore.android.external.installreferrer.GetInstallReferrerService";
    private final static String SERVICE_ACTION_NAME = "com.onestore.android.external.BIND_GET_INSTALL_REFERRER_SERVICE";
    /**
     * Referrer Details
     */
    private ReferrerDetail mReferrerDetails;
    /**
     * Result codes
     */
    private static final int SUCCESS = 0;
    private static final int DB_ACCESS_FAIL = -1;
    private static final int NOT_FOUND_REFERRER = -2;
    private static final int NOT_ALLOWED_PACKAGE = -3;
    private static final int INVALID_PACKAGE_NAME = -4;
    private static final int ONESTORE_MIN_APP_VER = 60700;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent serviceIntent = new Intent(SERVICE_ACTION_NAME);
        boolean connectionResult = false;
        for (String servicePackageName : SERVICE_PACKAGE_NAMES) {
            serviceIntent.setComponent(new ComponentName(servicePackageName, SERVICE_NAME));
            List<ResolveInfo> intentServices = getPackageManager().queryIntentServices(serviceIntent, 0);
            if (intentServices != null && !intentServices.isEmpty()) {
                if (isOneStoreCompatible(servicePackageName)) connectionResult = bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
                break;
            }
        }
        if (connectionResult) {
            Log.i(TAG, "Service connected!");
        } else {
            Log.e(TAG, "Service not connected!");
        }
    }

    /**
     * Service connection
     */
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            mService = IGetInstallReferrerService.Stub.asInterface(iBinder);
            Bundle bundle = null;
            try {
                bundle = mService.getInstallReferrer(getApplicationContext().getPackageName());
            } catch (RemoteException e) {
                //todo handling RemoteException
            }

            if (bundle != null) {
                try {
                    mReferrerDetails = new ReferrerDetail(bundle);
                } catch (DbAccessFailException e) {
                    //todo handling DbAccessFailException
                } catch (NotFoundReferrerException e) {
                    //todo handling NotFoundReferrerException
                } catch (NotAllowedException e) {
                    //todo handling NotAllowedException
                } catch (InvalidPackageNameException e) {
                    //todo handling InvalidPackageNameException
                } catch (UnknownException e) {
                    //todo handling UnknownException
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    private class ReferrerDetail {
        private final String installReferrer;
        private final String onestorePid;
        private final long referrerClickTimeStampSeconds;
        private final long installBeginTimeStampSeconds;

        public ReferrerDetail(Bundle bundle) throws DbAccessFailException, NotFoundReferrerException, NotAllowedException, InvalidPackageNameException, UnknownException {
            int resultCode = bundle.getInt("result_code", -10);
            if (resultCode == SUCCESS) {
                this.installReferrer = bundle.getString("install_referrer", "");
                this.onestorePid = bundle.getString("onestore_pid", "");
                this.referrerClickTimeStampSeconds = bundle.getLong("referrer_click_timestamp_seconds", -1);
                this.installBeginTimeStampSeconds = bundle.getLong("install_begin_timestamp_seconds", -1);
            } else if (resultCode == DB_ACCESS_FAIL) {
                throw new DbAccessFailException(bundle.getString("description", "DB_ACCESS_FAIL"));
            } else if (resultCode == NOT_FOUND_REFERRER) {
                throw new NotFoundReferrerException(bundle.getString("description", "NOT_FOUND_REFERRER"));
            } else if (resultCode == NOT_ALLOWED_PACKAGE) {
                throw new NotAllowedException(bundle.getString("description", "NOT_ALLOWED_PACKAGE"));
            } else if (resultCode == INVALID_PACKAGE_NAME) {
                throw new InvalidPackageNameException(bundle.getString("description", "INVALID_PACKAGE_NAME"));
            } else {
                throw new UnknownException("resultCode is " + resultCode);
            }
        }
    }

    private class DbAccessFailException extends Exception {
        public DbAccessFailException(String errorMsg) {
            super(errorMsg);
        }
    }
    private class NotFoundReferrerException extends Exception {
        public NotFoundReferrerException(String errorMsg) {
            super(errorMsg);
        }
    }
    private class NotAllowedPackageException extends Exception {
        public NotAllowedPackageException(String errorMsg) {
            super(errorMsg);
        }
    }
    private class InvalidPackageNameException extends Exception {
        public InvalidPackageNameException(String errorMsg) {
            super(errorMsg);
        }
    }
    private class UnknownException extends Exception {
        public UnknownException(String errorMsg) {
            super(errorMsg);
        }
    }
}
