package com.pkesslas.brazzersit.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.pkesslas.brazzersit.R;
import com.pkesslas.brazzersit.adapter.GalleryAdapter;
import com.pkesslas.brazzersit.helper.FileHelper;
import com.pkesslas.brazzersit.helper.NetworkHelper;
import com.pkesslas.brazzersit.interfaces.FirstPageFragmentListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class LocalGalleryFragment extends Fragment implements View.OnClickListener {
	private static final int RELOAD = 1;

	private FirstPageFragmentListener listener;
	private ImageView selectedImage;
	private TextView leftButton, rightButton, shareButton, deleteButton, uploadButton;
	private Gallery gallery;
	private Bitmap selectedBitmap;
	private GalleryAdapter galleryAdapter;
	private RelativeLayout rootView;

	private ArrayList<String> picturePath;
	public int picturePosition = -1;

	public LocalGalleryFragment() {}

	public LocalGalleryFragment(FirstPageFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("postion", picturePosition);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d("LocalGalleryFragment", "onCreateView");
		rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_gallery, container, false);

		picturePath = FileHelper.getAllFinalPicturePath();
		if (picturePath.size() == 0) {
			Log.d("LocalGalleryFragment", "picturePath.size() == 0");
			// TODO
			return rootView;
		}

		Log.d("LocalGalleryFragment", "buildGallery");
		gallery = (Gallery) rootView.findViewById(R.id.gallery1);
		selectedImage = (ImageView) rootView.findViewById(R.id.picture);
		rightButton = (TextView) rootView.findViewById(R.id.btn_right);
		leftButton = (TextView) rootView.findViewById(R.id.btn_left);
		shareButton = (TextView) rootView.findViewById(R.id.btn_share);
		deleteButton = (TextView) rootView.findViewById(R.id.btn_delete);
		uploadButton = (TextView) rootView.findViewById(R.id.btn_upload);

		uploadButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		leftButton.setOnClickListener(this);
		shareButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);

		//this.picturePosition = getActivity().getIntent().getExtras().getInt("position");

		if (picturePosition == -1) {
			this.picturePosition = getArguments().getInt("position");
		}


//		if (ParseUser.getCurrentUser() != null && NetworkHelper.isInternetAvailable(getActivity()) && !ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
//			uploadButton.setVisibility(View.VISIBLE);
//		}

		selectedBitmap = BitmapFactory.decodeFile(picturePath.get(picturePosition));
		selectedImage.setImageBitmap(selectedBitmap);

		buildGallery();

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null && picturePosition == -1) {
			this.picturePosition = savedInstanceState.getInt("position");
		}
	}

	private void buildGallery() {
		picturePath = FileHelper.getAllFinalPicturePath();
		gallery.setSpacing(1);
		galleryAdapter = new GalleryAdapter(getActivity(), picturePath);
		gallery.setAdapter(galleryAdapter);
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				picturePosition = position;
				Bitmap bmp = BitmapFactory.decodeFile(picturePath.get(position));
				selectedImage.setImageBitmap(bmp);
			}
		});
		gallery.setSelection(picturePosition);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_left) {
			if (picturePosition == 0) {
				picturePosition = galleryAdapter.getCount() - 1;
			} else {
				picturePosition--;
			}
			reloadImageView();
		} else if (v.getId() == R.id.btn_right) {
			if (picturePosition == galleryAdapter.getCount() - 1) {
				picturePosition = 0;
			} else {
				picturePosition++;
			}
			reloadImageView();
		} else if (v.getId() == R.id.btn_share) {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			shareIntent.setType("image/*");

			Uri uri = Uri.fromFile(new File(picturePath.get(picturePosition)));
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(shareIntent, "Share via"));
		} else if (v.getId() == R.id.btn_delete) {
			FileHelper.deleteFile(picturePath.get(picturePosition));

			if (picturePosition >= picturePath.size() - 1) {
				picturePosition--;
			}
			buildGallery();
			selectedBitmap = BitmapFactory.decodeFile(picturePath.get(picturePosition));
			selectedImage.setImageBitmap(selectedBitmap);
//		} else if (v.getId() == R.id.btn_upload) {
//			if (NetworkHelper.isInternetAvailable(getActivity())) {
//				getTitle(null);
//			} else {
//				Toast.makeText(getActivity(), "An internet connection is required.", Toast.LENGTH_LONG).show();
//			}
		}
	}
/*
	private void getTitle(String error) {
		final EditText title = new EditText(getActivity());
		if (error != null) {
			title.setError(error);
		}
		title.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		title.setHint("Your picture title");
		title.setTextColor(getResources().getColor(R.color.primary_text));
		new AlertDialog.Builder(getActivity())
				.setTitle("Name your picture")
				.setView(title)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, int whichButton) {
						if (title.getText().length() >= 3 && title.getText().length() < 120) {
							final ProgressDialog loadingDialog = ProgressDialog.show(getActivity(), "Uploading", "Uploading. Please wait...", true);
							uploadPicture(loadingDialog, title);
						} else {
							getTitle("Invalid title name, too short or too long.");
						}
					}
				}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		}).show();
	}

	private void uploadPicture(final DialogInterface dialog, EditText title) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();

		ParseFile file = new ParseFile("image.png", byteArray);
		ParseObject imageObject = new ParseObject("Image");

		imageObject.put("user", ParseUser.getCurrentUser());
		imageObject.put("title", title.getText().toString());
		imageObject.put("point", 0);
		imageObject.put("image", file);
		imageObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				dialog.dismiss();
				if (e == null) {
					Toast.makeText(getActivity(), "Upload succed", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
*/
	private void reloadImageView() {
		selectedBitmap = BitmapFactory.decodeFile(picturePath.get(picturePosition));
		selectedImage.setImageBitmap(selectedBitmap);
		galleryAdapter.getItemId(picturePosition);
		gallery.setSelection(picturePosition);
	}

	public void backPressed() {
		listener.onSwitchToNextHomeFragment();
	}
}