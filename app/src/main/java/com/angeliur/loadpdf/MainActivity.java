package com.angeliur.loadpdf;

import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private WebView wv;
    private PDFView pdfView;
    String pdfUrl = "http://file.chmsp.com.cn/colligate/file/00100000224821.pdf";
    private InputStream is;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*webview在线加载PDF不成功*/
//        wv = findViewById(R.id.wv);
//        wv.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);

        initView();
//        getInputStream();
        getLocalFile();
        loadPDF();
    }

    //加载sd卡中的PDF文件
    private void getLocalFile() {
        File sdCard = Environment.getExternalStorageDirectory();
        file = new File(sdCard + "/阿里巴巴Android开发手册(正式版).pdf");

    }


    private void initView() {
        pdfView = findViewById(R.id.pdfView);
    }

    private void getInputStream() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(pdfUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//POST 可能报错
                    connection.setDoInput(true);
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    //实现连接
                    connection.connect();
                    if (connection.getResponseCode() == 200){
                        is = connection.getInputStream();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void loadPDF() {
        pdfView
                //加载assets资源目录下的PDF
//                .fromAsset("Android 开发技巧新整理.pdf")
                .fromFile(file)
//                .fromStream(is)
//                .fromUri();
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .onDraw(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        Toast.makeText(getApplicationContext(), "loadComplete", Toast.LENGTH_SHORT).show();
                    }
                })
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })
                .onPageScroll(new OnPageScrollListener() {
                    @Override
                    public void onPageScrolled(int page, float positionOffset) {

                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                })
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .load();

    }

    //启动外部PDF软件来打开PDF文件
    public void openPdf(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //officeFile：本地文档；type：文档MIMEType类型，可以使用文件格式后缀
        String type = "application/pdf";
        intent.setDataAndType(Uri.fromFile(file), type);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
