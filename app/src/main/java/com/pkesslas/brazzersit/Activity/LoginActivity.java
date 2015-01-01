package com.pkesslas.brazzersit.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.pkesslas.brazzersit.R;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {
	private EditText username, password;
	private TextView createAccountButton, skip, passwordForgot;
	private ActionProcessButton signInButton;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		context = this;

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		passwordForgot = (TextView) findViewById(R.id.password_forgot);
		createAccountButton = (TextView) findViewById(R.id.btn_create_account);
		skip = (TextView) findViewById(R.id.btn_skip);
		signInButton = (ActionProcessButton) findViewById(R.id.btn_login);
		signInButton.setMode(ActionProcessButton.Mode.ENDLESS);

		passwordForgot.setOnClickListener(this);
		signInButton.setOnClickListener(this);
		createAccountButton.setOnClickListener(this);
		skip.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_create_account) {
			startActivity(new Intent(this, CreateAccountActivity.class));
		} else if (v.getId() == R.id.btn_login) {
			if (formValid()) {
				signInButton.setProgress(1);
				ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							signInButton.setProgress(100);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException ignored) {
							}
							startActivity(new Intent(context, MainActivity.class));
						} else {
							signInButton.setProgress(-1);
							Toast.makeText(context, e.getMessage().toUpperCase(), Toast.LENGTH_LONG).show();						}
						}
				});
			}
		} else if (v.getId() == R.id.btn_skip) {
			ParseAnonymousUtils.logIn(new LogInCallback() {
				public void done(ParseUser user, ParseException e) {
					if (e == null) {
						Log.d("MyApp", "Anonymous login failed.");
						startActivity(new Intent(context, MainActivity.class));
					}
				}
			});
		} else if (v.getId() == R.id.password_forgot) {
			final EditText email = new EditText(this);
			email.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			email.setHint("Your email Address");
			email.setTextColor(getResources().getColor(R.color.primary_text));
			new AlertDialog.Builder(this)
					.setTitle("Recover password")
					.setMessage("Enter your email address and we'll send you a link that'll allow you to change your password")
					.setView(email)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							String value = email.getText().toString();
							ParseUser.requestPasswordResetInBackground(value,
									new RequestPasswordResetCallback() {
										public void done(ParseException e) {
											if (e == null) {
												Toast.makeText(context, "An email was successfully sent with reset instructions.", Toast.LENGTH_LONG).show();
											} else {
												Toast.makeText(context, e.getMessage().toUpperCase(), Toast.LENGTH_LONG).show();
											}
										}
									});
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Do nothing.
				}
			}).show();

		}
	}

	private boolean formValid() {
		boolean valid = true;
		if (!isUsernameValid(username.getText().toString())) {
			username.setError("Username too short");
			valid = false;
		}
		if (!isPasswordValid(password.getText().toString())) {
			password.setError("Password too short");
			valid = false;
		}
		return valid;
	}

	private boolean isUsernameValid(String username) {
		//return username.length() >= 4;
		return username.length() >= 1;
	}

	private boolean isPasswordValid(String password) {
		//return password.length() >= 6;
		return password.length() >= 1;
	}
}
