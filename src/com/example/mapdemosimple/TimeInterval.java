package com.example.mapdemosimple;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class TimeInterval extends Activity {

	SharedPreferences spObject;
	Editor editor;
	public static int selected=1;
	RadioGroup radio_group;
	int selectedId;
	private RadioButton refresh_interval;
	String timeinterval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_interval);
		spObject = this.getSharedPreferences("time_selection", MODE_PRIVATE);
		editor = spObject.edit();

		radio_group = (RadioGroup) findViewById(R.id.interval);
		selectedId = spObject.getInt("selection_id",
				radio_group.getCheckedRadioButtonId());
		RadioButton radio_button = (RadioButton) findViewById(selectedId);
		radio_button.setChecked(true);

		radio_group
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup paramRadioGroup,
							int paramInt) {
						// TODO Auto-generated method stub
						for (int i = 0; i < paramRadioGroup.getChildCount(); i++) {
							refresh_interval = (RadioButton) paramRadioGroup
									.getChildAt(i);
							selectedId = paramInt;
							editor.putInt("selection_id", selectedId);
							editor.commit();
							if (refresh_interval.getId() == paramInt) {
								timeinterval = refresh_interval.getText()
										.toString();

								System.out.println("GGGGGG0000000000"
										+ timeinterval);

								if (timeinterval.equals("1 Second")) {
									System.out.println("interval 2");
									selected = 1;

									TimeInterval.this.finish();

								}

								if (timeinterval.equals("2 Second")) {
									System.out.println("interval 3");
									selected = 2;

									TimeInterval.this.finish();

								}

								if (timeinterval.equals("3 Second")) {
									System.out.println("interval 4");
									selected = 3;

									TimeInterval.this.finish();

								}

								if (timeinterval.equals("4 Second")) {
									selected = 4;
									selectedId = paramInt;
									TimeInterval.this.finish();

								}

								if (timeinterval.equals("5 Second")) {
									selected = 5;

									TimeInterval.this.finish();

								}

								if (timeinterval.equals("6 Second")) {
									selected = 6;

									TimeInterval.this.finish();

								}

								if (timeinterval.equals("7 Second")) {
									selected = 7;

									TimeInterval.this.finish();
									// TimeInterval.this.overridePendingTransition(0,R.anim.slide_down_out);
								}

								if (timeinterval.equals("8 Second")) {
									selected = 8;
									selectedId = paramInt;
									TimeInterval.this.finish();
									// TimeInterval.this.overridePendingTransition(0,R.anim.slide_down_out);
								}

								if (timeinterval.equals("9 Second")) {
									selected = 9;

									TimeInterval.this.finish();
									// TimeInterval.this.overridePendingTransition(0,R.anim.slide_down_out);
								}
								if (timeinterval.equals("10 Second")) {
									selected = 10;

									TimeInterval.this.finish();
									// TimeInterval.this.overridePendingTransition(0,R.anim.slide_down_out);
								}

							}
						}
					}
				});

	}

}
