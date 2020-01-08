package com.munish.saferfoodproject.View.Ui.Activities;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Constants;
import com.pdftron.common.PDFNetException;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.pdf.PDFViewCtrl;
import com.pdftron.pdf.config.PDFViewCtrlConfig;
import com.pdftron.pdf.config.ViewerConfig;
import com.pdftron.pdf.controls.DocumentActivity;
import com.pdftron.pdf.tools.Tool;
import com.pdftron.pdf.tools.ToolManager;
import com.pdftron.pdf.utils.AppUtils;
import com.pdftron.sdf.SDFDoc;

public class PdfViewActivity extends AppCompatActivity {
    private static final String TAG = "PdfViewActivity";
    private PDFViewCtrl mPdfViewCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        Bundle extras = getIntent().getExtras();

        String uri = extras.getString("url");
        Log.e(TAG, "onCreate: " + uri);
        Constants.checkResume=1;

        mPdfViewCtrl = findViewById(R.id.pdfviewctrl);
        try {
            AppUtils.setupPDFViewCtrl(mPdfViewCtrl);


            Log.e(TAG, "onCreate: dwsd"+AppUtils.getLicenseKey(this) );
        } catch (PDFNetException e) {
            // Handle exception
        }
        openHttpDocument(this, uri);


        finish();
    }

    private void openHttpDocument(Context context, String url) {
        final Uri fileLink = Uri.parse(url);


        // Customize settings in PDFViewCtrlConfig
        PDFViewCtrlConfig pdfViewCtrlConfig = PDFViewCtrlConfig.getDefaultConfig(this)
                .setClientBackgroundColor(Color.YELLOW)
                .setClientBackgroundColorDark(Color.BLUE)
                .setHighlightFields(true)
                .setImageSmoothing(true)
                .setUrlExtraction(true)
                .setMaintainZoomEnabled(true);

        ViewerConfig config = new ViewerConfig.Builder().pdfViewCtrlConfig(pdfViewCtrlConfig).openUrlCachePath(this.getCacheDir().getAbsolutePath()).fullscreenModeEnabled(true)
                .multiTabEnabled(false)
                .documentEditingEnabled(true)
                .longPressQuickMenuEnabled(false)
                .showPageNumberIndicator(true)
                .showBottomNavBar(true)
                .showThumbnailView(true)
                .showBookmarksView(true)
                .toolbarTitle("My Reader")
                .showSearchView(false)
                .showShareOption(true)
                .showDocumentSettingsOption(true)
                .showAnnotationToolbarOption(true)
                .showOpenFileOption(true)
                .showOpenUrlOption(true)
                .showEditPagesOption(false)
                .showPrintOption(true)
                .showCloseTabOption(true)
                .showAnnotationsList(true)
                .showOutlineList(true)
                .showUserBookmarksList(false)
                .showSaveCopyOption(true)
                .build();
        DocumentActivity.openDocument(context, fileLink, config);

        Log.e(TAG, "openHttpDocument: "+config.getSaveCopyExportPath() );

    }

}