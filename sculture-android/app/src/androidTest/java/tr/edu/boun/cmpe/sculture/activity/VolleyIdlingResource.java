package tr.edu.boun.cmpe.sculture.activity;

import android.support.test.espresso.IdlingResource;

import com.android.volley.RequestQueue;

import tr.edu.boun.cmpe.sculture.BaseApplication;

final class VolleyIdlingResource implements IdlingResource {
    private static final String TAG = "VolleyIdlingResource";
    private final String resourceName = "resource";

    // written from main thread, read from any thread.
    private volatile ResourceCallback resourceCallback;

    private RequestQueue mVolleyRequestQueue;

    public VolleyIdlingResource() throws SecurityException, NoSuchFieldException {
        mVolleyRequestQueue = BaseApplication.baseApplication.mRequestQueue;
    }
    public String getName() {
        return VolleyIdlingResource.class.getName();
    }

    @Override
    public void registerIdleTransitionCallback(
            ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

    @Override
    public boolean isIdleNow() {
        boolean idle = mVolleyRequestQueue.getSize() == 0;
        if (idle && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

}