package com.ljpww72729.rvrl;

import android.databinding.BindingAdapter;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by LinkedME06 on 16/11/26.
 */

public class FrescoBindings {
    /**
     * 该方式类似于自定义视图时自定义相关属性,该方法的强大之处在于<b>可以进行逻辑的处理</b>
     *
     * 此处bind可自定义,无固定要求
     *
     * @param view   需要绑定的组件类型(必选参数)
     * @param imgUrl 图片地址
     */
    @BindingAdapter({"bind:actualImageUri"})
    public static void setActualImageUri(final SimpleDraweeView view, final String imgUrl) {
        view.setImageURI(imgUrl);

// TODO: 16/11/26 需要将该类提出来
    }
    /**
     * 该方式类似于自定义视图时自定义相关属性,该方法的强大之处在于<b>可以进行逻辑的处理</b>
     *
     * 此处bind可自定义,无固定要求
     *
     * @param view   需要绑定的组件类型(必选参数)
     * @param imgUri 图片Uri地址
     */
    @BindingAdapter({"bind:actualImageUri"})
    public static void setActualImageUri(final SimpleDraweeView view, final Uri imgUri) {
        view.setImageURI(imgUri);
// TODO: 16/11/26 需要将该类提出来
    }
}
