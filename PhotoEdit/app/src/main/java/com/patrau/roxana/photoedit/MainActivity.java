package com.patrau.roxana.photoedit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.patrau.roxana.photoedit.adapters.ScreenSlidePagerAdapter;
import com.patrau.roxana.photoedit.fragments.CanvasFragment;
import com.patrau.roxana.photoedit.fragments.CollectionFragment;
import com.patrau.roxana.photoedit.helper.Helper;
import com.patrau.roxana.photoedit.interfaces.CanvasSaver;
import com.patrau.roxana.photoedit.interfaces.TransformationReceiver;
import com.patrau.roxana.photoedit.tasks.SaveCanvasTask;
import com.patrau.roxana.photoedit.views.CustomViewPager;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TransformationReceiver, CanvasSaver {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SELECT_PHOTO = 2;

    public static Bitmap originalBitmap;
    public static Bitmap currentCanvasBitmap;

    private CoordinatorLayout coordinatorLayout;
    private CustomViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private FloatingActionButton plusButton;
    private FloatingActionButton photoButton;
    private FloatingActionButton openButton;
    private ProgressDialog progressDialog;
    private boolean canvasButtonsDisplayed = false;
    private File currentPhotoFile;

    private MenuItem doneMenuItem;
    private boolean displayDoneMenuItem = false;
    private MenuItem cancelMenuItem;
    private boolean displayCancelMenuItem = false;

    private boolean inCanvasEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.collection));

        // initialize a progress dialog that will be displayed with server requests
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);


        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (CustomViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                canvasButtonsDisplayed = false;
                switch (position) {
                    case 0:
                        setTitle(getString(R.string.collection));
                        hideAllFloatingActionButtons();

                        displayDoneAndCancelMenuItems(false);

                        break;
                    case 1:
                        setTitle(getString(R.string.canvas));
                        plusButton.show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        plusButton = (FloatingActionButton) findViewById(R.id.plus_button);
        plusButton.setOnClickListener(this);
        photoButton = (FloatingActionButton) findViewById(R.id.photo_button);
        photoButton.setOnClickListener(this);
        openButton = (FloatingActionButton) findViewById(R.id.open_button);
        openButton.setOnClickListener(this);

        if (!Environment.getExternalStorageDirectory().canWrite()) {
            displayStoragePermissionSnackBar();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                currentPhotoFile = Helper.createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                displayStoragePermissionSnackBar();
            }
            // Continue only if the File was successfully created
            if (currentPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(currentPhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void displayStoragePermissionSnackBar() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.provide_storage_permission, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0 || inCanvasEditMode) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        doneMenuItem = menu.getItem(0);
        if (displayDoneMenuItem) {
            doneMenuItem.setVisible(true);
        } else {
            doneMenuItem.setVisible(false);
        }

        cancelMenuItem = menu.getItem(1);
        if (displayCancelMenuItem) {
            cancelMenuItem.setVisible(true);
        } else {
            cancelMenuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_done:
                displaySaveDialog();

                break;
            case R.id.action_cancel:
                displayCancelDialog();

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishCanvasEdit() {
        inCanvasEditMode = false;

        setViewPagerEnabled(true);
        displayDoneAndCancelMenuItems(false);
        ((CanvasFragment) mPagerAdapter.getItem(1)).hideControllersFrameContainer();

        plusButton.show();
    }

    public void hideAllFloatingActionButtons() {
        plusButton.hide();
        photoButton.hide();
        openButton.hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_button:
                if (canvasButtonsDisplayed) {
                    photoButton.hide();
                    openButton.hide();
                    canvasButtonsDisplayed = false;
                } else {
                    openButton.show();
                    photoButton.show();
                    canvasButtonsDisplayed = true;
                }
                break;
            case R.id.photo_button:
                photoButton.hide();
                openButton.hide();
                canvasButtonsDisplayed = false;

                dispatchTakePictureIntent();
                break;
            case R.id.open_button:
                if (!Environment.getExternalStorageDirectory().canWrite()) {
                    displayStoragePermissionSnackBar();

                    return;
                }

                photoButton.hide();
                openButton.hide();
                canvasButtonsDisplayed = false;

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
            case R.id.back_button:
                CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
                canvasFragment.attachEffectsFragment();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            prepareForCanvasEdit(currentPhotoFile.getAbsolutePath());
        } else if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            prepareForCanvasEdit(filePath);
        }
    }

    private void prepareForCanvasEdit(String filePath) {
        inCanvasEditMode = true;

        hideAllFloatingActionButtons();
        setViewPagerEnabled(false);
        displayDoneAndCancelMenuItems(true);

        CanvasFragment canvasFragment = (CanvasFragment) mPagerAdapter.getItem(1);
        canvasFragment.setOriginalFilePath(filePath);
        canvasFragment.attachEffectsFragment();
    }

    public void onTransformationProgress(float progress) {
        Log.d("Vlad", "progress: " + progress);
    }

    public void onTransformationComplete(Bitmap bitmap) {
        ((CanvasFragment) mPagerAdapter.getItem(1)).setCanvasImage(bitmap);
    }

    public void onCanvasSaveFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, R.string.canvas_not_saved_successfully, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCanvasSaveSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, R.string.canvas_saved_successfully, Toast.LENGTH_SHORT).show();
                finishCanvasEdit();

                CollectionFragment collectionFragment = (CollectionFragment) mPagerAdapter.getItem(0);
                collectionFragment.refreshCollection();
            }
        });
    }

    public void setViewPagerEnabled(boolean enabled) {
        mPager.setPagingEnabled(enabled);
    }

    public void displayDoneAndCancelMenuItems(boolean display) {
        if (doneMenuItem != null) {
            doneMenuItem.setVisible(display);
        } else {
            displayDoneMenuItem = display;
        }

        if (cancelMenuItem != null) {
            cancelMenuItem.setVisible(display);
        } else {
            displayCancelMenuItem = display;
        }
    }

    private void displaySaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_dialog_title);
        builder.setMessage(R.string.save_dialog_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                SaveCanvasTask saveCanvasTask = new SaveCanvasTask(currentCanvasBitmap, MainActivity.this);
                saveCanvasTask.execute(new String[]{});
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }

    private void displayCancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.cancel_dialog_title);
        builder.setMessage(R.string.cancel_dialog_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
                canvasFragment.setCanvasImage(MainActivity.originalBitmap);
                finishCanvasEdit();
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }

    public void attachGrayScaleController() {
        CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
        canvasFragment.attachGrayScaleController();
    }

    public void attachFlipController() {
        CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
        canvasFragment.attachFlipController();
    }

    public void attachBoostColorController() {
        CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
        canvasFragment.attachBoostColorController();
    }

    public void attachRotateController() {
        CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
        canvasFragment.attachRotateController();
    }
}
