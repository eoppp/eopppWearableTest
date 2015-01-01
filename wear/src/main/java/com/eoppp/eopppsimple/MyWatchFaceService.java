package com.eoppp.eopppsimple;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;


public class MyWatchFaceService extends CanvasWatchFaceService {

  /**
   * provide your watch face implementation
   * CanvasWatchFaceService.Engineを継承したクラスを返す
   */
  @Override
  public Engine onCreateEngine() {
    return new Engine();
  }

  /**
   * implement Engine
   */

  private class Engine extends CanvasWatchFaceService.Engine {

    //graphic objects
    Bitmap mBackgroundBitmap;
    Bitmap mBackgroundScaledBitmap;
    Paint mHourPaint;
    Paint mMinutePaint;
//    Paint mSecondPaint;

    //time object
    Time mTime;


    /**
     * initialize
     * WallpaperService.Engineのmethodである、onCreateをOverride
     * <p/>
     * https://developer.android.com/training/wearables/watch-faces/drawing.html#SystemUI
     */
    //wearable-support-docs/reference/android/support/wearable/watchface/WatchFaceService.Engine.html#onCreate(android.view.SurfaceHolder)
    @Override
    public void onCreate(SurfaceHolder holder) {
      super.onCreate(holder);

      /* configure the system UI */

      /**
       * Sets the watch face style. This affects how UI elements such as the battery indicator are drawn on top of the watch face.
       * Typically called in onCreate(SurfaceHolder) but may be called at any time,
       * e.g. in response to the user changing the watch face configuration. Must be called from the wallpaper thread.
       *
       * ここに、wearable用のwatch faceのためのServiceを指定する。
       */
      //https://developer.android.com/training/wearables/watch-faces/drawing.html#SystemUI
      //wearable-support-docs/reference/android/support/wearable/watchface/WatchFaceService.Engine.html#setWatchFaceStyle(android.support.wearable.watchface.WatchFaceStyle)

      setWatchFaceStyle(new WatchFaceStyle.Builder(MyWatchFaceService.this)
          //card（通知につかわれるやつ）の高さを決める。PEEK_MODE_VARIABLE と PEEK_MODE_SHORT のどちらかを設定する。
          //wearable-support-docs/reference/android/support/wearable/watchface/WatchFaceStyle.Builder.html#setCardPeekMode(int)
          .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
              //BACKGROUND_VISIBILITY_INTERRUPTIVE と BACKGROUND_VISIBILITY_PERSISTENT のどちらかを設定する。
              //INTERRUPTIVE→cardの背景？がすぐ消える。PERSISTENT→cardの背景？がずっと表示される。
              //wearable-support-docs/reference/android/support/wearable/watchface/WatchFaceStyle.Builder.html#setBackgroundVisibility(int)
          .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
              //システムによる時間を、画面上に表示するかどうかを決定する？。自分で時間を表示するようにしている場合は、不要
              //とりあえずtrueにしたら、デジタル時計が表示された
              //wearable-support-docs/reference/android/support/wearable/watchface/WatchFaceStyle.Builder.html#setShowSystemUiTime(boolean)
          .setShowSystemUiTime(false)
              //wearable-support-docs/reference/android/support/wearable/watchface/WatchFaceStyle.Builder.html#build()
              //読み取り専用のWatchFaceStyle objectを作成する。
          .build());

      /* load the background image */
      //mBackgroundBitmapに、black_background.pngを入れる。
      Resources resources = MyWatchFaceService.this.getResources();
      Drawable backgroundDrawable = resources.getDrawable(R.drawable.icon);
      mBackgroundBitmap = ((BitmapDrawable) backgroundDrawable).getBitmap();


      /* create graphic styles */
      //時針用の設定をする。
      mHourPaint = new Paint();
      //縁線の幅を決める
      mHourPaint.setStrokeWidth(10f);
      mHourPaint.setAntiAlias(true);
      mHourPaint.setColor(Color.LTGRAY);
      //縁線の角の形を決める
      mHourPaint.setStrokeCap(Paint.Cap.ROUND);

      //分針用の設定をする。
      mMinutePaint = new Paint();
      //縁線の幅を決める
      mMinutePaint.setStrokeWidth(3f);
      mMinutePaint.setAntiAlias(true);
      mMinutePaint.setColor(Color.LTGRAY);
      //縁線の角の形を決める
      mMinutePaint.setStrokeCap(Paint.Cap.ROUND);

      /* allocate an object to hold the time */
      mTime = new Time();

    }

    /**
     * Invalidate the canvas when the time changes
     * <p/>
     * The system calls the Engine.onTimeTick() method every minute.
     * In ambient mode, it is usually sufficient to update your watch face once per minute.
     * To update your watch face more often while in interactive mode, you provide a custom timer as described in Initialize the custom timer.
     * Most watch face implementations just invalidate the canvas to redraw the watch face when the time changes:
     * <p/>
     * アンビエントモード時に、1分ごとに呼ばれる。
     * 通常は、次の画面（1分後の時計）を再描画するための前提として、前回までの時計の描画を無効化する。（canvasをinvalidateする）
     */
    //wearable-support-docs/reference/android/support/wearable/watchface/WatchFaceService.Engine.html#onTimeTick()
    @Override
    public void onTimeTick() {
      super.onTimeTick();

      invalidate();
    }

    /**
     * Called when the device enters or exits ambient mode.
     * The watch face should switch to a black and white display in ambient mode.
     * If the watch face displays seconds, it should hide them in ambient mode.
     * <p/>
     * アンビエントモードとインタラクティブモードが切り替わった時に呼ばれる。
     * 通常は、次の画面（アンビエント⇔インタラクティブ）を再描画するための前提として、前回までの画面の描画を無効化する。（canvasをinvalidateする）
     */
    //https://developer.android.com/training/wearables/watch-faces/drawing.html#Modes
    //wearable-support-docs/reference/android/support/wearable/watchface/WatchFaceService.Engine.html#onAmbientModeChanged(boolean)
    @Override
    public void onAmbientModeChanged(boolean inAmbientMode) {
      super.onAmbientModeChanged(inAmbientMode);

      invalidate();
    }

    /**
     * To draw a custom watch face, the system calls the Engine.onDraw() method with a Canvas instance and
     * the bounds in which you should draw your watch face. The bounds account for any inset areas,
     * such as the "chin" on the bottom of some round devices. You can use this canvas to draw your watch face directly as follows:
     * <p/>
     * 1. If this is the first invocation of the onDraw() method, scale your background to fit.
     * 2. Check whether the device is in ambient mode or interactive mode.
     * 3. Perform any required graphic computations.
     * 4. Draw your background bitmap on the canvas.
     * 5. Use the methods in the Canvas class to draw your watch face.
     * <p/>
     * Parameters
     * <p/>
     * canvas the canvas to draw into
     * bounds the bounds in which the watch face should be drawn
     */
    //https://developer.android.com/training/wearables/watch-faces/drawing.html#Drawing
    //wearable-support-docs/reference/android/support/wearable/watchface/CanvasWatchFaceService.Engine.html#onDraw(android.graphics.Canvas,%20android.graphics.Rect)
    @Override
    public void onDraw(Canvas canvas, Rect bounds) {

      //現在の日時を取得し、mTimeに入れる
      mTime.setToNow();

      int width = bounds.width();
      int height = bounds.height();

      // Draw the background, scaled to fit.
      // 用意した背景素材の大きさが、android wearの描画領域との違う場合、合うように調整したものをmBackgroundScaledBitmapに入れる。
      if (mBackgroundScaledBitmap == null
          || mBackgroundScaledBitmap.getWidth() != width
          || mBackgroundScaledBitmap.getHeight() != height) {
        mBackgroundScaledBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap,
            width, height, true /* filter */);
      }
      canvas.drawBitmap(mBackgroundScaledBitmap, 0, 0, null);

      // Find the center. Ignore the window insets so that, on round watches
      // with a "chin", the watch face is centered on the entire screen, not
      // just the usable portion.
      // android wearの描画領域の中心を得て、centerXとcenterYに入れる
      float centerX = width / 2f;
      float centerY = height / 2f;

      // Compute rotations and lengths for the clock hands.
//      float secRot = mTime.second / 30f * (float) Math.PI;
      int minutes = mTime.minute;
      float minRot = minutes / 30f * (float) Math.PI;
      float hrRot = ((mTime.hour + (minutes / 60f)) / 6f) * (float) Math.PI;

//      float secLength = centerX - 20;
      float minLength = centerX - 40;
      float hrLength = centerX - 80;

//      // Only draw the second hand in interactive mode.
//      if (!isInAmbientMode()) {
//        float secX = (float) Math.sin(secRot) * secLength;
//        float secY = (float) -Math.cos(secRot) * secLength;
//        canvas.drawLine(centerX, centerY, centerX + secX, centerY +
//            secY, mSecondPaint);
//      }

      // Draw the minute and hour hands.
      float minX = (float) Math.sin(minRot) * minLength;
      float minY = (float) -Math.cos(minRot) * minLength;
      canvas.drawLine(centerX, centerY, centerX + minX, centerY + minY,
          mMinutePaint);
      float hrX = (float) Math.sin(hrRot) * hrLength;
      float hrY = (float) -Math.cos(hrRot) * hrLength;
      canvas.drawLine(centerX, centerY, centerX + hrX, centerY + hrY,
          mHourPaint);

    }

  }

}