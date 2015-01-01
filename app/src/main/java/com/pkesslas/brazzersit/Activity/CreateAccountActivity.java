package com.pkesslas.brazzersit.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.dd.processbutton.FlatButton;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.pkesslas.brazzersit.R;

public class CreateAccountActivity extends ActionBarActivity implements View.OnClickListener {
	private ActionProcessButton createAccountButton;
	private EditText username, password, repeatedPassword, email;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_account);

		context = this;
		createAccountButton = (ActionProcessButton) findViewById(R.id.btn_create_account);
		createAccountButton.setMode(ActionProcessButton.Mode.ENDLESS);

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		repeatedPassword = (EditText) findViewById(R.id.repeat_password);
		email = (EditText) findViewById(R.id.email);

		createAccountButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_create_account) {
			Log.i("Account Creation", "click");
			if (formValid()) {
				Log.i("Account Creation", "form valid");
				createAccountButton.setProgress(1);
				final ParseUser user = new ParseUser();
				user.setUsername(username.getText().toString());
				user.setPassword(password.getText().toString());
				user.setEmail(email.getText().toString());

				user.signUpInBackground(new SignUpCallback() {
					public void done(ParseException e) {
					if (e == null) {
						Log.i("Account Creation", "Account created");
						createAccountButton.setProgress(100);
						try {
								Thread.sleep(1000);
						} catch (InterruptedException ignored) {
						}
						startActivity(new Intent(context, MainActivity.class));
					} else {
						Log.i("Account Creation", e.getMessage());
						createAccountButton.setProgress(-1);
						Toast.makeText(context, e.getMessage().toUpperCase(), Toast.LENGTH_LONG).show();						}
					}
				});
			}
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
		if (!repeatedPasswordValid()) {
			repeatedPassword.setError("Doesn't match");
			valid = false;
		}
		if (!isEmailValid(email.getText().toString())) {
			email.setError("Email not valid");
			valid = false;
		}
		return valid;
	}

	private boolean repeatedPasswordValid() {
		return password.getText().toString().equals(repeatedPassword.getText().toString());
	}

	private boolean isEmailValid(String email) {
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {
		//return password.length() >= 6;
		return password.length() >= 1;
	}

	private boolean isUsernameValid(String username) {
		//return username.length() >= 4;
		return username.length() >= 1;
	}
}
