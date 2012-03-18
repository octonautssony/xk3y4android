package xk3y.dongle.android.utils;

import xk3y.dongle.android.R;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PaginateButton extends Button implements OnClickListener {

	public PaginateButton(Context context, String text) {
		super(context);
		this.setBackgroundResource(R.drawable.rounded_button);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(15, 15, 15, 15);
		this.setLayoutParams(lp);
		this.setPadding(25, 0, 25, 0);
		this.setText(text);
		this.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(v.getContext(), "Button = " + this.getText(), Toast.LENGTH_SHORT).show();
	}

}
