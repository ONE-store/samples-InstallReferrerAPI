package com.onestore.android.external.installreferrer;

/**
 * An API to onestore install referrer.
 */
interface IGetInstallReferrerService {
     /**
      * onestore install referrer.
      *
      * @param arguments Caller’s package name.
      *     The package name of the caller, used for disambiguation.
      * @return The returned Bundle contains the following:
      * <pre>
      *     key: "result_code" value: string
      *     key: "onestore_pid" value: string
      *         The pid of onestore
      *     key: "install_referrer" value: string
      *         The referrer url of the installed package
      *     key: "referrer_click_timestamp_seconds" value: long
      *         The timestamp in seconds when referrer click happens
      *     key: "install_begin_timestamp_seconds" value: long
      *         The timestamp in seconds when installation begins
      * </pre>
      */
      Bundle getInstallReferrer(String packageName);
}

