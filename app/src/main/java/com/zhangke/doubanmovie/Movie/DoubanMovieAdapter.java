package com.zhangke.doubanmovie.Movie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhangke.doubanmovie.R;
import com.zhangke.doubanmovie.base.BaseRecyclerAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZhangKe on 2018/1/3.
 */

public class DoubanMovieAdapter extends BaseRecyclerAdapter<DoubanMovieAdapter.ViewHolder, DoubanMovieBean> {

    private static final String TAG = "DoubanMovieAdapter";

    private String IMAGE_PATH;

    public DoubanMovieAdapter(Context context, List<DoubanMovieBean> listData) {
        super(context, listData);

        IMAGE_PATH = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_douanmovie, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String fileDiskPath = String.format("%s/%s.jpg", IMAGE_PATH, listData.get(position).getMovieName());
        if(new File(fileDiskPath).exists()){
            holder.imgView.setImageBitmap(BitmapFactory.decodeFile(fileDiskPath));
        }else {
            SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    holder.imgView.setImageBitmap(resource);
                    saveBitmapToDisk(fileDiskPath, resource);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    holder.imgView.setVisibility(View.GONE);
                }
            };
            Glide.with(context)
                    .load(listData.get(position).getMovieImageUrl())
                    .asBitmap()
                    .into(target);
        }
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder{

        @BindView(R.id.img_view)
        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * 将图片保存到本地
     */
    private void saveBitmapToDisk(String filePath, Bitmap originBitmap) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                BufferedOutputStream bos = null;
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    originBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                } catch (IOException e) {
                    Log.e(TAG, "saveBitmapToDisk: ", e);
                } finally {
                    if (bos != null) {
                        try {
                            bos.flush();
                            bos.close();
                        } catch (IOException e) {
                            Log.e(TAG, "saveBitmapToDisk: ", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "saveBitmapToDisk: ", e);
        }
    }

    private void downloadImage(String imgUrl, String filePath) {
        new Thread(() -> {
            File f = new File(filePath);
            if (f.exists()) {
                f.delete();
            }
            InputStream is = null;
            OutputStream os = null;
            try {
                URL url = new URL(imgUrl);
                URLConnection con = url.openConnection();
                is = con.getInputStream();
                byte[] bs = new byte[1024];
                int len;
                os = new FileOutputStream(filePath);
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    os.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
