package ayyappa.eloksolutions.in.ayyappaap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebActivity extends ActionBarActivity {
    private WebView webView;
    private ProgressDialog progressDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.webview);
        Intent intent = getIntent();
        String url=intent.getStringExtra("uri");
         webView = (WebView)findViewById(R.id.webView1);
        startWebView(url);
    }
    private void startWebView(String url) {

         webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setTextZoom(webView.getSettings().getTextZoom() + 100);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        progressDialog = new ProgressDialog(WebActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        webView.loadUrl(url);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_feedback:
                startActivity(new Intent(this, FeedBackForm.class));
                return true;
            case R.id.action_home:
                startActivity(new Intent(this, CardViewActivity.class));
                // startActivity(new Intent(this, CarousalActivity.class));
                return true;
            case R.id.action_rules:
                startActivity(new Intent(this, DeekshaRules.class));
                return true;
            case R.id.contactus:
                startActivity(new Intent(this, ContactUs.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
