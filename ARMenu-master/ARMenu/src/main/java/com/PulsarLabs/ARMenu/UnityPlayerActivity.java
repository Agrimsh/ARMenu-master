package com.PulsarLabs.ARMenu;

import com.unity3d.player.*;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;


/** UnityPlayerActivity.java - Initiates the Unity/AR activity
 *
 *  The following UnityPlayerActivity was generated from exporting Unity project as Gradle. It shows
 *  the Unity splash screen followed by the internal procedures to initiates and start the camera.
 *  In the meantime, the user can be on-boarded on how to use the application by following the instructions.
 *
 */

public class UnityPlayerActivity extends Activity
{
	protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
	private final int ONBOARDING_START_DELAY = 6000; // initial delay till the onboarding dialog starts

	// Setup activity layout
	@Override protected void onCreate (Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		mUnityPlayer = new UnityPlayer(this);
		setContentView(mUnityPlayer);
		mUnityPlayer.requestFocus();

		//After the initial delay, start the sequence of onboarding the experiment procedure
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				final FragmentManager mFragmentManager = getFragmentManager();
				OnBoardingFragment mOnboardingFragment = new OnBoardingFragment();
				mOnboardingFragment.show(mFragmentManager, "Dialog Fragment");
			}
		}, ONBOARDING_START_DELAY);
	}

	// Quit Unity
	@Override protected void onDestroy ()
	{
		mUnityPlayer.quit();
		super.onDestroy();
	}

	// Pause Unity
	@Override protected void onPause()
	{
		super.onPause();
		mUnityPlayer.pause();
	}

	// Resume Unity
	@Override protected void onResume()
	{
		super.onResume();
		mUnityPlayer.resume();
	}

	// This ensures the layout will be correct.
	@Override public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mUnityPlayer.configurationChanged(newConfig);
	}

	// Notify Unity of the focus change.
	@Override public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mUnityPlayer.windowFocusChanged(hasFocus);
	}

	// For some reason the multiple keyevent type is not supported by the ndk.
	// Force event injection by overriding dispatchKeyEvent().
	@Override public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
			return mUnityPlayer.injectEvent(event);
		return super.dispatchKeyEvent(event);
	}

	// Pass any events not handled by (unfocused) views straight to UnityPlayer
	@Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
	/*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
