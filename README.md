# Get ONE store Install Referrer
ONE store Install Referrer API 샘플

Introduction
----
이 샘플은 ONE store Install Referrer API를 사용하는 방법에 대한 가이드를 제공합니다. 

Getting Started
----

서비스 패키지명, 서비스명, 서비스 액션명

```java
String[] SERVICE_PACKAGE_NAMES = {"com.skt.skaf.A000Z00040", "com.kt.olleh.storefront", "com.kt.olleh.istore", "com.lguplus.appstore", "android.lgt.appstore"};
String SERVICE_NAME = "com.onestore.android.external.installreferrer.GetInstallReferrerService";
String SERVICE_ACTION_NAME = "com.onestore.android.external.BIND_GET_INSTALL_REFERRER_SERVICE";
```

Service Binding

```java
Intent serviceIntent = new Intent(SERVICE_ACTION_NAME);
serviceIntent.setComponent(new ComponentName(servicePackageName, SERVICE_NAME));
bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
```

Service Connection

```java
private ServiceConnection mServiceConnection = new ServiceConnection() { 
    @Override 
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        mService = IOnestoreGetInstallReferrerService.Stub.asInterface(iBinder); 
        try {
            bundle = mService.getInstallReferrer(getApplicationContext().getPackageName()); 
            ......
        } catch (RemoteException e) {
            e.printStackTrace(); 
        } 
    } 
......
};
```

License
----

Copyright 2019 원스토어(주)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
