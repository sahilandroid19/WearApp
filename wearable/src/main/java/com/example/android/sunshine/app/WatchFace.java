package com.example.android.sunshine.app;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class WatchFace {

    private static final Typeface BOLD_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
    private static final Typeface NORMAL_TYPEFACE =
            Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);

    private final Paint hourPaint;
    private final Paint minutePaint;
    private final Paint secondsPaint;
    private final Paint ampmPaint;
    private final Paint datePaint;
    private final Paint linePaint;
    private final Paint maxPaint;
    private final Paint minPaint;
    private final Paint iconPaint;

    private static int color;

    private static Context contexts;

    private String maxTemp;
    private String minTemp;
    private int iconId;

    private static boolean shouldShowSeconds = true;

    public WatchFace(Paint hourPaint, Paint minutePaint, Paint secondsPaint, Paint ampmPaint, Paint datePaint, Paint linePaint, Paint maxPaint, Paint minPaint, Paint iconPaint) {
        this.hourPaint = hourPaint;
        this.minutePaint = minutePaint;
        this.secondsPaint = secondsPaint;
        this.ampmPaint = ampmPaint;
        this.datePaint = datePaint;
        this.linePaint = linePaint;
        this.maxPaint = maxPaint;
        this.minPaint = minPaint;
        this.iconPaint = iconPaint;
    }

    public static WatchFace newInstance(Context context) {
        contexts = context;
        color = context.getResources().getColor(R.color.watch_background);

        Paint hourPaint = new Paint();
        hourPaint.setColor(Color.WHITE);

        Paint minutePaint = new Paint();
        minutePaint.setColor(Color.WHITE);

        Paint ampmPaint = new Paint();
        ampmPaint.setColor(context.getResources().getColor(R.color.date_color));

        Paint secondsPaint = new Paint();
        secondsPaint.setColor(context.getResources().getColor(R.color.date_color));

        if(shouldShowSeconds) {
            hourPaint.setTextSize(context.getResources().getDimension(R.dimen.interactive_time_size_round));
            minutePaint.setTextSize(context.getResources().getDimension(R.dimen.interactive_time_size_round));
            secondsPaint.setTextSize(context.getResources().getDimension(R.dimen.seconds_size_round));
        }else {
            hourPaint.setTextSize(context.getResources().getDimension(R.dimen.digital_time_size_round));
            minutePaint.setTextSize(context.getResources().getDimension(R.dimen.digital_time_size_round));
            ampmPaint.setTextSize(context.getResources().getDimension(R.dimen.digital_ampm_size_round));
        }

        hourPaint.setAntiAlias(true);
        minutePaint.setAntiAlias(true);
        secondsPaint.setAntiAlias(true);
        ampmPaint.setAntiAlias(true);

        Paint datePaint = new Paint();
        datePaint.setColor(context.getResources().getColor(R.color.date_color));
        datePaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        datePaint.setAntiAlias(true);

        Paint linePaint = new Paint();
        linePaint.setColor(context.getResources().getColor(R.color.date_color));
        linePaint.setAntiAlias(true);

        Paint maxPaint = new Paint();
        maxPaint.setColor(Color.WHITE);
        maxPaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        maxPaint.setAntiAlias(true);

        Paint minPaint = new Paint();
        minPaint.setColor(context.getResources().getColor(R.color.date_color));
        minPaint.setTextSize(context.getResources().getDimension(R.dimen.date_size));
        minPaint.setAntiAlias(true);

        Paint iconPaint = new Paint();

        return new WatchFace(hourPaint, minutePaint, secondsPaint, ampmPaint, datePaint, linePaint, maxPaint, minPaint, iconPaint);
    }

    public void draw(Canvas canvas, Rect bounds){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));

        Calendar calendar = Calendar.getInstance();
        if(shouldShowSeconds) {
            canvas.drawColor(color);

            String hourText = String.format(Locale.ENGLISH, "%02d:", cal.get(Calendar.HOUR));
            float timeXOffset = contexts.getResources().getDimension(R.dimen.interactive_x_offset_round);
            float timeYOffset = contexts.getResources().getDimension(R.dimen.interactive_y_offset_round);
            canvas.drawText(hourText, timeXOffset, timeYOffset, hourPaint);

            String minuteText = String.format(Locale.ENGLISH, "%02d:", cal.get(Calendar.MINUTE));
            float timeXOffset1 = contexts.getResources().getDimension(R.dimen.interactive_minute_x_offset_round);
            canvas.drawText(minuteText, hourPaint.measureText(hourText) + timeXOffset1, timeYOffset, minutePaint);

            String secondsText = String.format(Locale.ENGLISH, "%02d", cal.get(Calendar.SECOND));
            float timeXOffset2 = contexts.getResources().getDimension(R.dimen.interactive_second_x_offset_round);
            canvas.drawText(secondsText,
                    hourPaint.measureText(hourText) + minutePaint.measureText(minuteText) + timeXOffset2, timeYOffset, secondsPaint);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM dd yyyy", Locale.ENGLISH);
            String dateText = dateFormat.format(calendar.getTime());
            float dateXOffset = contexts.getResources().getDimension(R.dimen.interactive_date_x_offset_round);
            float dateYOffset = contexts.getResources().getDimension(R.dimen.interactive_date_y_offset_round);
            canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);

            float lineStartX = contexts.getResources().getDimension(R.dimen.line_start_x);
            float lineStopX = contexts.getResources().getDimension(R.dimen.line_stop_x);
            float lineY = contexts.getResources().getDimension(R.dimen.line_y);
            float lineYOffset = timeYOffset + dateYOffset + lineY;
            canvas.drawLine(lineStartX, lineYOffset, lineStopX, lineYOffset, linePaint);

            float tempY = contexts.getResources().getDimension(R.dimen.temp_y);
            float maxX = contexts.getResources().getDimension(R.dimen.max_x);
            if(maxTemp!=null) {
                canvas.drawText(maxTemp, bounds.left + maxX, lineYOffset + tempY, maxPaint);
                float minX = contexts.getResources().getDimension(R.dimen.min_x);
                canvas.drawText(minTemp, bounds.exactCenterX() + minX, lineYOffset + tempY, minPaint);

                float iconX = contexts.getResources().getDimension(R.dimen.icon_x);
                float iconY = contexts.getResources().getDimension(R.dimen.icon_y);
                Bitmap bitmap = BitmapFactory.decodeResource(contexts.getResources(), getIconResourceForWeatherCondition(iconId));
                canvas.drawBitmap(bitmap, bounds.exactCenterX() - iconX, lineYOffset + iconY, iconPaint);
            }

        }else {
            canvas.drawColor(Color.BLACK);

            String hourText = String.format(Locale.ENGLISH, "%02d:", cal.get(Calendar.HOUR));
            float timeXOffset = contexts.getResources().getDimension(R.dimen.digital_x_offset_round);
            float timeYOffset = contexts.getResources().getDimension(R.dimen.digital_y_offset);
            canvas.drawText(hourText, timeXOffset, timeYOffset, hourPaint);

            String minuteText = String.format(Locale.ENGLISH, "%02d", cal.get(Calendar.MINUTE));
            float timeXOffset1 = contexts.getResources().getDimension(R.dimen.digital_minute_x_offset_round);
            canvas.drawText(minuteText, hourPaint.measureText(hourText) + timeXOffset1, timeYOffset, minutePaint);

            String ampmText;
            if(cal.get(Calendar.AM_PM)==Calendar.AM){
                ampmText = "AM";
            }else {
                ampmText = "PM";
            }
            float timeXOffset2 = contexts.getResources().getDimension(R.dimen.digital_ampm_x_offset_round);
            canvas.drawText(ampmText,
                    hourPaint.measureText(hourText) + minutePaint.measureText(minuteText) + timeXOffset2, timeYOffset, ampmPaint);

            SimpleDateFormat dateFormat = new SimpleDateFormat("EE, MMM dd yyyy", Locale.ENGLISH);
            String dateText = dateFormat.format(calendar.getTime());
            float dateXOffset = contexts.getResources().getDimension(R.dimen.digital_date_x_offset_round);
            float dateYOffset = contexts.getResources().getDimension(R.dimen.digital_date_y_offset_round);
            canvas.drawText(dateText, dateXOffset, timeYOffset + dateYOffset, datePaint);

            float lineStartX = contexts.getResources().getDimension(R.dimen.line_start_x);
            float lineStopX = contexts.getResources().getDimension(R.dimen.line_stop_x);
            float lineY = contexts.getResources().getDimension(R.dimen.line_y);
            float lineYOffset = timeYOffset + dateYOffset + lineY;
            canvas.drawLine(lineStartX, lineYOffset, lineStopX, lineYOffset, linePaint);
        }
    }

    public void setAntiAlias(boolean antiAlias) {
        hourPaint.setAntiAlias(antiAlias);
        minutePaint.setAntiAlias(antiAlias);
        secondsPaint.setAntiAlias(antiAlias);
        ampmPaint.setAntiAlias(antiAlias);
        datePaint.setAntiAlias(antiAlias);
        linePaint.setAntiAlias(antiAlias);
        maxPaint.setAntiAlias(antiAlias);
        minPaint.setAntiAlias(antiAlias);
    }

    public void setColor(int color) {
        hourPaint.setTypeface(BOLD_TYPEFACE);
        minutePaint.setTypeface(BOLD_TYPEFACE);
        secondsPaint.setTypeface(NORMAL_TYPEFACE);
        maxPaint.setTypeface(BOLD_TYPEFACE);
        hourPaint.setColor(color);
        minutePaint.setColor(color);
        secondsPaint.setColor(color);
        ampmPaint.setColor(color);
        datePaint.setColor(color);
        linePaint.setColor(color);
        maxPaint.setColor(color);
        minPaint.setColor(color);
    }

    public void setShowSeconds(boolean showSeconds) {
        shouldShowSeconds = showSeconds;
    }

    public void setMaxTemp(Double maxTemp){
        this.maxTemp = formatTemperature(contexts, maxTemp);
    }

    public void setMinTemp(Double minTemp){
        this.minTemp = formatTemperature(contexts, minTemp);
    }

    public void setIconId(int iconId){ this.iconId = iconId; }

    public static String formatTemperature(Context context, double temperature) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        return String.format(context.getString(R.string.format_temperature), temperature);
    }

    public static int getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.ic_rain;
        } else if (weatherId == 511) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.ic_storm;
        } else if (weatherId == 800) {
            return R.drawable.ic_clear;
        } else if (weatherId == 801) {
            return R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.ic_cloudy;
        }
        return -1;
    }
}
