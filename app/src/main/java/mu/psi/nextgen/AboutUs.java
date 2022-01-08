package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import mu.psi.nextgen.databinding.ActivityAboutUsBinding;

public class AboutUs extends AppCompatActivity implements View.OnClickListener {

    ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.aboutUsClose.setOnClickListener(this);
        this.binding.aboutUsWriteToUs.setOnClickListener(this);
        this.binding.aboutUsCallUs.setOnClickListener(this);
        this.binding.aboutUsPrivacyPolicy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.about_us_close) {
            Intent goToHomeMore = new Intent(AboutUs.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(goToHomeMore);
            finish();
        }
        else if(id == R.id.about_us_write_to_us) {
            Intent openEmail = new Intent(Intent.ACTION_SEND);
            openEmail.setType("plain/text");
            openEmail.putExtra(Intent.EXTRA_EMAIL, new String[] { "mu.psi.corp@gmail.com" });
            openEmail.putExtra(Intent.EXTRA_SUBJECT, "Customer Feedback");
            startActivity(Intent.createChooser(openEmail, ""));
        }
        else if(id == R.id.about_us_call_us) {
            Intent openDialer = new Intent(Intent.ACTION_DIAL);
            openDialer.setData(Uri.parse("tel:+12499990449"));
            startActivity(openDialer);
        }
        else if(id == R.id.about_us_privacy_policy) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.termsfeed.com/live/6614714f-d25b-47c1-9cb0-45f8f419a813"));
            startActivity(browserIntent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goToHomeMore = new Intent(AboutUs.this, HomeMore.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToHomeMore);
        finish();
    }
}