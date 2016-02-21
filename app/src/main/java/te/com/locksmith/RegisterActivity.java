package te.com.locksmith;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import te.com.locksmith.tools.Tools;

public class RegisterActivity extends Activity {
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }

    public void getMainPage(View view) {
        Tools tools = new Tools(RegisterActivity.this);
        tools.setSharedPreference("isMember",true);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
