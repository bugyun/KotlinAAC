package vip.ruoyun.baselib;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * Created by ruoyun on 2019-09-10.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction: textview span 的帮助类
 */
public class TextSpanHelper {

    private void test(final Context context) {    //使用方法
        TextView textView = new TextView(context);
        TextSpanHelper.with("我已阅读并同意《用户注册及服务协议》")
                .addSpan(TextSpanHelper.newSpan().setStart(7).setEnd(18)
                        .setClickableSpan(new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull final View view) {

                            }

                            @Override
                            public void updateDrawState(@NonNull TextPaint ds) {
//                                ds.setColor(context.getResources().getColor(R.color.color_549CFF));
                                ds.setUnderlineText(false);
                            }
                        }))
                .addSpan(TextSpanHelper.newSpan().setStart(2).setEnd(1))
                .setHighlightColor(Color.TRANSPARENT)//默认值
                .build(textView);
    }


    public static class SpanBuilder {

        private SpanBuilder() {
        }

        private int start;

        private int end;

        private ClickableSpan clickableSpan;

        public SpanBuilder setClickableSpan(ClickableSpan clickableSpan) {
            this.clickableSpan = clickableSpan;
            return this;
        }

        public SpanBuilder setStart(int start) {
            this.start = start;
            return this;
        }

        public SpanBuilder setEnd(int end) {
            this.end = end;
            return this;
        }
    }

    public static class Builder {

        private SpannableString spannableString;

        private int highlightColor = Color.TRANSPARENT;

        private Builder(String text) {
            spannableString = new SpannableString(text);
        }

        public Builder addSpan(SpanBuilder spanBuilder) {
            spannableString.setSpan(spanBuilder.clickableSpan, spanBuilder.start, spanBuilder.end,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            return this;
        }

        public Builder setHighlightColor(int highlightColor) {
            this.highlightColor = highlightColor;
            return this;
        }

        public void build(final TextView textView) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setHighlightColor(highlightColor);
            textView.setText(spannableString);
        }
    }

    public static Builder with(String text) {
        return new Builder(text);
    }

    public static SpanBuilder newSpan() {
        return new SpanBuilder();
    }
}
