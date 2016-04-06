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

    // Intent request codes
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SELECT_PHOTO = 2;

    public static Bitmap originalBitmap;
    public static Bitmap currentCanvasBitmap;

    public static boolean inCanvasEditMode = false;

    private CoordinatorLayout coordinatorLayout;
    private CustomViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private FloatingActionButton plusButton;
    private FloatingActionButton editButton;
    private FloatingActionButton photoButton;
    private FloatingActionButton openButton;
    private ProgressDialog progressDialog;
    private boolean canvasButtonsDisplayed = false;
    private File currentPhotoFile;

    private MenuItem doneMenuItem;
    private boolean displayDoneMenuItem = false;
    private MenuItem cancelMenuItem;
    private boolean displayCancelMenuItem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the layout for the activity
        setContentView(R.layout.activity_main);

        // initialize the toolbar
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
                        if (!inCanvasEditMode) {
                            plusButton.show();
                        }

                        String currentFilePath = ((CanvasFragment) mPagerAdapter.getItem(1)).getOriginalFilePath();
                        if (currentFilePath != null) {
                            // the Canvas fragment has an image displayed, the edit button should be visible
                            editButton.show();
                        } else {
                            editButton.hide();
                        }

                        CollectionFragment collectionFragment = (CollectionFragment) mPagerAdapter.getItem(0);
                        collectionFragment.closeMultiChoiceMode();

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // initialize the floating action buttons
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        plusButton = (FloatingActionButton) findViewById(R.id.plus_button);
        plusButton.setOnClickListener(this);
        editButton = (FloatingActionButton) findViewById(R.id.edit_button);
        editButton.setOnClickListener(this);
        photoButton = (FloatingActionButton) findViewById(R.id.photo_button);
        photoButton.setOnClickListener(this);
        openButton = (FloatingActionButton) findViewById(R.id.open_button);
        openButton.setOnClickListener(this);

        // make sure that canvas items can be saved
        if (!Environment.getExternalStorageDirectory().canWrite()) {
            displayStoragePermissionSnackBar();
        }
    }

    /**
     * Create and start the "Take Picture" intent
     */
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

    /**
     * Display a message that asks the user to enable Storage Permission for the application
     */
    private void displayStoragePermissionSnackBar() {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.provide_storage_permission, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // start an intent that redirects the user to the application's settings
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
        // Inflate the menu - this adds items to the action bar if it is present.
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

    /**
     * The Canvas is no longer in edit mode - update the UI and all necessary flags.
     */
    private void finishCanvasEdit() {
        inCanvasEditMode = false;

        setViewPagerEnabled(true);
        displayDoneAndCancelMenuItems(false);
        ((CanvasFragment) mPagerAdapter.getItem(1)).hideControllersFrameContainer();

        plusButton.show();
        editButton.show();
    }

    public void hideAllFloatingActionButtons() {
        plusButton.hide();
        editButton.hide();
        photoButton.hide();
        openButton.hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plus_button:
                if (canvasButtonsDisplayed) {
                    photoButton.hide();
                    openButton.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                        @Override
                        public void onHidden(FloatingActionButton fab) {
                            String currentFilePath = ((CanvasFragment) mPagerAdapter.getItem(1)).getOriginalFilePath();
                            if (currentFilePath != null) {
                                // the Canvas fragment has an image displayed, the edit button should be visible
                                editButton.show();
                                editButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    canvasButtonsDisplayed = false;
                } else {
                    editButton.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                        @Override
                        public void onHidden(FloatingActionButton fab) {
                            editButton.setVisibility(View.GONE);
                            openButton.show();
                            photoButton.show();
                        }
                    });
                    canvasButtonsDisplayed = true;
                }
                break;
            case R.id.edit_button:
                String currentFilePath = ((CanvasFragment) mPagerAdapter.getItem(1)).getOriginalFilePath();
                if (currentFilePath != null) {
                    prepareForCanvasEdit(currentFilePath);
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

                // start an image picker intent
                // this will allow selecting an image from the Android device
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
            // returned from "Take photo" intent
            prepareForCanvasEdit(currentPhotoFile.getAbsolutePath());
        } else if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            // returned from "Select photo" intent
            // extract the selected image's path and start editing it
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

    /**
     * Canvas UI should be updated for editing mode.
     * Update all the views and necessary flags.
     */
    public void prepareForCanvasEdit(String filePath) {
        inCanvasEditMode = true;

        if (mPager.getCurrentItem() != 1) {
            mPager.setCurrentItem(1, true);
        }

        hideAllFloatingActionButtons();
        setViewPagerEnabled(false);
        displayDoneAndCancelMenuItems(true);

        CanvasFragment canvasFragment = (CanvasFragment) mPagerAdapter.getItem(1);
        canvasFragment.setOriginalFilePath(filePath);
        canvasFragment.attachEffectsFragment();
    }

    /**
     * This method fills the Canvas fragment with the selected item from the Collection fragment.
     */
    public void fillCanvas(String filePath) {
        inCanvasEditMode = false;

        if (mPager.getCurrentItem() != 1) {
            mPager.setCurrentItem(1, true);
        }

        CanvasFragment canvasFragment = (CanvasFragment) mPagerAdapter.getItem(1);
        canvasFragment.setOriginalFilePath(filePath);
        finishCanvasEdit();
    }

    public void onTransformationProgress(float progress) {
        Log.d("MainActivity", "progress: " + progress);
    }

    public void onTransformationComplete(Bitmap bitmap) {
        // set the transformed image as the canvas image
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

    public void onCanvasSaveSuccess(final String canvasFileName) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, R.string.canvas_saved_successfully, Toast.LENGTH_SHORT).show();
                finishCanvasEdit();

                // refresh the Collection in order to add the saved canvas
                CollectionFragment collectionFragment = (CollectionFragment) mPagerAdapter.getItem(0);
                collectionFragment.refreshCollection();

                // update the canvas's file path to the newly created file
                File canvasFile = new File(Helper.getCanvasStorageDirectory(), canvasFileName);
                ((CanvasFragment) mPagerAdapter.getItem(1)).setOriginalFilePath(canvasFile.getPath());
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
                String originalFilePath = ((CanvasFragment) mPagerAdapter.getItem(1)).getOriginalFilePath();
                if (originalFilePath.contains(Helper.getCanvasStorageDirectory())) {
                    displayOverwriteDialog();
                } else {
                    progressDialog.show();
                    SaveCanvasTask saveCanvasTask = new SaveCanvasTask(currentCanvasBitmap, MainActivity.this, null);
                    saveCanvasTask.execute(new String[]{});
                }
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

    private void displayOverwriteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.overwrite_dialog_title);
        builder.setMessage(R.string.overwrite_dialog_message);
        builder.setPositiveButton(R.string.overwrite, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                String originalFilePath = ((CanvasFragment) mPagerAdapter.getItem(1)).getOriginalFilePath();
                String currentCanvasFilename = originalFilePath.substring(originalFilePath.lastIndexOf("/") + 1);
                SaveCanvasTask saveCanvasTask = new SaveCanvasTask(currentCanvasBitmap, MainActivity.this, currentCanvasFilename);
                saveCanvasTask.execute(new String[]{});
            }
        });
        builder.setNegativeButton(R.string.new_item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                SaveCanvasTask saveCanvasTask = new SaveCanvasTask(currentCanvasBitmap, MainActivity.this, null);
                saveCanvasTask.execute(new String[]{});
            }
        });
        builder.setNeutralButton(R.string.cancel, null);
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

    public void attachSepiaController() {
        CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
        canvasFragment.attachSepiaController();
    }

    public void attachBrightnessController() {
        CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
        canvasFragment.attachBrightnessController();
    }

    public void attachHueController() {
        CanvasFragment canvasFragment = ((CanvasFragment) mPagerAdapter.getItem(1));
        canvasFragment.attachHueController();
    }

    public void displayProgressDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
