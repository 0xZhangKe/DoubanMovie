package com.zhangke.doubanmovie.util;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Http 工具类
 * <p>
 * Created by ZhangKe on 2017/11/21.
 */

public class HttpUtil {

    private static final String TAG = "HttpUtil";

    public static void simpleGetRequest(RequestQueue requestQueue,
                                        String url,
                                        final OnStringDataCallbackListener callbackListener) {
        StringRequest request = new StringRequest(url,
                callbackListener::onSuccess,
                (VolleyError volleyError) -> {
                    if (volleyError instanceof TimeoutError) {
                        callbackListener.onError("连接超时");
                    } else {
                        if (null != volleyError.networkResponse) {
                            callbackListener.onError("网络错误");
                        } else {
                            callbackListener.onError("网络错误");
                        }
                    }
                    String message = volleyError.getMessage();
                    callbackListener.onError(TextUtils.isEmpty(message) ? "网络错误" : message);
                });
        requestQueue.add(request);
    }

    public static void postRequest(RequestQueue requestQueue,
                                   String url,
                                   String postParams,
                                   final OnStringDataCallbackListener onDataCallbackListener) {
        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                (String httpResponse) -> {
                    try {
                        if (TextUtils.isEmpty(httpResponse)) {
                            onDataCallbackListener.onError("网络数据错误");
                        } else {
                            onDataCallbackListener.onSuccess(httpResponse);
                        }
                    } catch (Exception e) {
                        onDataCallbackListener.onError("网络数据错误：" + e.getMessage());
                    }
                },
                (VolleyError volleyError) -> {
                    if (volleyError instanceof TimeoutError) {
                        onDataCallbackListener.onError("连接超时");
                    } else {
                        if (null != volleyError.networkResponse) {
                            onDataCallbackListener.onError("网络错误");
                        } else {
                            onDataCallbackListener.onError("网络错误");
                        }
                    }
                    onDataCallbackListener.onError(volleyError.getMessage());
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return postParams.getBytes();
            }
        };
        requestQueue.add(request);
    }

    public static void postHeaderRequest(RequestQueue requestQueue,
                                         String url,
                                         Map<String, String> postParams,
                                         final OnStringDataCallbackListener onDataCallbackListener) {
        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                (String httpResponse) -> {
                    try {
                        if (TextUtils.isEmpty(httpResponse)) {
                            onDataCallbackListener.onError("网络数据错误");
                        } else {
                            onDataCallbackListener.onSuccess(httpResponse);
                        }
                    } catch (Exception e) {
                        onDataCallbackListener.onError("网络数据错误：" + e.getMessage());
                    }
                },
                (VolleyError volleyError) -> {
                    if (volleyError instanceof TimeoutError) {
                        onDataCallbackListener.onError("连接超时");
                    } else {
                        if (null != volleyError.networkResponse) {
                            onDataCallbackListener.onError("网络错误");
                        } else {
                            onDataCallbackListener.onError("网络错误");
                        }
                    }
                    onDataCallbackListener.onError(volleyError.getMessage());
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return postParams;
            }
        };
        requestQueue.add(request);
    }

    /**
     * 下载文件
     */
    public static void downloadFile(String downloadUrl, String filePath, OnDownLoadListener onDownLoadListener) {
        new Thread(() -> {
            File f = new File(filePath);
            if (f.exists()) {
                f.delete();
            }
            InputStream is = null;
            OutputStream os = null;
            try {
                URL url = new URL(downloadUrl);
                URLConnection con = url.openConnection();
                is = con.getInputStream();
                byte[] bs = new byte[1024];
                int len;
                os = new FileOutputStream(filePath);
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                onDownLoadListener.onDownload(false);
            } finally {
                try {
                    os.close();
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            onDownLoadListener.onDownload(true);
        });
    }

    public interface OnDownLoadListener {
        void onDownload(boolean success);
    }

    /**
     * 上传图片的方法,
     * 将参数作为 header 传过去
     *
     * @param url                    接口地址
     * @param params                 Header 参数
     * @param targetBitmaps          图片
     * @param onDataCallbackListener 监听器
     */
    public static void postImagesWithHeader(final String url,
                                            final Map<String, String> params,
                                            final Map<String, Bitmap> targetBitmaps,
                                            final OnStringDataCallbackListener onDataCallbackListener) {
        new Thread(() -> {
            try {
                String BOUNDARY = java.util.UUID.randomUUID().toString();
                String PREFIX = "--", LINEND = "\r\n";
                String MULTIPART_FROM_DATA = "multipart/form-data";
                String CHARSET = "UTF-8";
                URL uri = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                conn.setReadTimeout(10 * 1000);
                conn.setDoInput(true);// 允许输入
                conn.setDoOutput(true);// 允许输出
                conn.setUseCaches(false);
                conn.setRequestMethod("POST"); // Post方式
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("Charsert", "UTF-8");
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
                if (targetBitmaps != null) {
                    conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                            + ";boundary=" + BOUNDARY);
                }
                DataOutputStream outStream = new DataOutputStream(
                        conn.getOutputStream());

                // 发送文件数据
                if (targetBitmaps != null && targetBitmaps.size() > 0) {
                    for (Map.Entry<String, Bitmap> file : targetBitmaps.entrySet()) {
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append(PREFIX);
                        sb1.append(BOUNDARY);
                        sb1.append(LINEND);
                        sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getKey() + "\"" + LINEND);
//                        sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
                        sb1.append("Content-Type: multipart/form-data; charset="
                                + CHARSET + LINEND);
                        sb1.append(LINEND);
                        outStream.write(sb1.toString().getBytes());

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        file.getValue().compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        InputStream is = new ByteArrayInputStream(baos.toByteArray());

                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            outStream.write(buffer, 0, len);
                        }
                        is.close();
                        outStream.write(LINEND.getBytes());
                    }
                }
                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                outStream.write(end_data);
                outStream.flush();
                // 得到响应码
                final int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    InputStream in = conn.getInputStream();
                    InputStreamReader isReader = new InputStreamReader(in);
                    BufferedReader bufReader = new BufferedReader(isReader);
                    String line = null;
                    String data = "";
                    while ((line = bufReader.readLine()) != null) {
                        data = data + line;
                    }
                    onDataCallbackListener.onSuccess(data);
                } else {
                    onDataCallbackListener.onError("网络错误");
                }
                outStream.close();
                conn.disconnect();
            } catch (IOException e) {
                onDataCallbackListener.onError("网络错误");
            }
        });
    }

    private static MessageDigest messagedigest = null;
    private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getMD5String(byte[] bytes) {
        if (messagedigest == null) {
            try {
                messagedigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
        }
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest(), 0, messagedigest.digest().length);
    }

    public static String bufferToHex(byte bytes[], int m, int n) {
        StringBuilder sb = new StringBuilder(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            char c0 = hexDigits[(bytes[l] & 0xf0) >> 4];
            char c1 = hexDigits[bytes[l] & 0xf];
            sb.append(c0);
            sb.append(c1);
        }
        return sb.toString();
    }

    /**
     * 网络访问的监听器
     */
    public interface OnStringDataCallbackListener {
        void onSuccess(String data);

        void onError(String error);
    }
}
