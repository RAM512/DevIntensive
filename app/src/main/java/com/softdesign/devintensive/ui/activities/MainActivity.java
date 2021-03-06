package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.managers.PreferencesManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    @BindView(R.id.main_coordinator_container) CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.navigation_drawer) DrawerLayout mDrawerLayout;
    private ImageView mAvatar; // инициализируется через mDrawerLayout

    @BindView(R.id.fab) FloatingActionButton mFab;

    @BindView(R.id.rating_count_text) TextView mUserValueRating;
    @BindView(R.id.code_strings_count_text) TextView mUserValueCodeLines;
    @BindView(R.id.project_count_text) TextView mUserValueProjects;
    private List<TextView> mUserValueViews;

    @BindView(R.id.phone_edit) EditText mPhone;
    @BindView(R.id.email_edit) EditText mEmail;
    @BindView(R.id.vk_edit )EditText mVk;
    @BindView(R.id.github_edit) EditText mGithub;
    @BindView(R.id.about_myself_edit) EditText mBio;

    @BindView(R.id.phone_text_input) TextInputLayout mPhoneTextInput;
    @BindView(R.id.email_text_input) TextInputLayout mEmailTextInput;
    @BindView(R.id.vk_text_input) TextInputLayout mVkTextInput;
    @BindView(R.id.github_text_input) TextInputLayout mGithubTextInput;

    @BindView(R.id.profile_placeholder) RelativeLayout mProfilePlaceholder;
    @BindView(R.id.user_photo_img) ImageView mProfileImage;
    private int mEditMode;
    private int mScrollFlags;

    private List<EditText> mUserInfoViews;
    private DataManager mDataManager;

    @BindView(R.id.appbar_layout) AppBarLayout mAppBarLayout;
    private AppBarLayout.LayoutParams mAppBarParams;

    private File mPhotoFile;
    private Uri mSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate()");

        mDataManager = DataManager.getInstance();

        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.user_bg) // TODO: 06.07.2016 сделать transform + crop
                .into(mProfileImage);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mPhone);
        mUserInfoViews.add(mEmail);
        mUserInfoViews.add(mVk);
        mUserInfoViews.add(mGithub);
        mUserInfoViews.add(mBio);

        mUserValueViews = new ArrayList<>();
        mUserValueViews.add(mUserValueRating);
        mUserValueViews.add(mUserValueCodeLines);
        mUserValueViews.add(mUserValueProjects);

        setupValidCheckOnTextChange();

        setupToolBar();
        setupDrawer();
        initUserFields();
        initUserInfoValue();

        if (savedInstanceState == null) {
//            showSnackBar("Активити запускается впервые");
        } else {
//            showSnackBar("Активити уже запускалось");
            mEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mEditMode);
        }
    }

    /**
     * Настраивает автоматическую проверку при изменении каждом текста в данных
     */
    private void setupValidCheckOnTextChange() {
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPhoneValid();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEmailValid();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        mVk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isVkValid();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        mGithub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isGithubValid();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    @OnClick ({R.id.call_img,
            R.id.send_email_img,
            R.id.open_vk_profile,
            R.id.open_github_repo,
            R.id.profile_placeholder,
            R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
//                showSnackBar("fab");
                if (mEditMode == 0) {
                    changeEditMode(1);
                } else {
                    if (isDataValid()) {
                        changeEditMode(0);
                        saveUserFields();
                    }
                }
                break;

            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

            case R.id.call_img:
                makeCall(mPhone.getText().toString());
                break;

            case R.id.send_email_img:
                sendMailTo(mEmail.getText().toString());
                break;

            case R.id.open_vk_profile:
                showSnackBar("VK");
                openLink(mVk.getText().toString());
                break;

            case R.id.open_github_repo:
                showSnackBar("Github");
                openLink(mGithub.getText().toString());
                break;
        }
    }

    /**
     * Проверяет предоставлено ли требуемое разрешение. Если нет, запрашивает его у пользователя.
     * @param permissions              набор требуемых разрешений для проверки
     * @param requestPermissionCode    идентификатор результата запроса разрешений
     * @return true - если разрешения предоставлены, false - если хоть одно разрешение из списка не предоставлено
     */
    private boolean permissionsCheck(String[] permissions, int requestPermissionCode) {
        boolean permissionsGranted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
                break;
            }
        }

        if (!permissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, requestPermissionCode);
            showPermissionSnackBar();
        }
        return permissionsGranted;
    }

    /**
     * Открывает системные настройки приложения. Позволяет пользователю предоставить необходимые разрешения.
     */
    private void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }

    /**
     * Упрощенный вариант метода {@link #permissionsCheck(String[] permissions, int requestPermissionCode) permissionsCheck}.
     * Проверяет только одно разрешение
     * @param permission               разрешение для проверки
     * @param requestPermissionCode    идентификатор результата запроса разрешения
     * @return true - если разрешение предоставлено, false - если разрешение не предоставлено
     */
    private boolean permissionsCheck(String permission, int requestPermissionCode) {
        return permissionsCheck(new String[] {permission}, requestPermissionCode);
    }

    /**
     * Отображает SnackBar с сообщением о необходимых разрешениях и возможностью перехода к настройкам приложения для предоставления разрешений.
     */
    private void showPermissionSnackBar() {
        Snackbar.make(mCoordinatorLayout, R.string.permission_message,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.allow, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();
                    }
                }).show();
    }

    /**
     * Звонит по номеру, указанному в параметрах
     * @param phoneNumber телефонный номер
     */
    private void makeCall(String phoneNumber) {
        if (permissionsCheck(Manifest.permission.CALL_PHONE, ConstantManager.CALL_PHONE_REQUEST_PERMISSION_CODE)) {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent); // TODO: 06.07.2016 почему красное подчеркивание, если все работает на Android 6?
        }
    }

    /**
     * Открыавет почтовое приложение с вписанным адресом для отправки, указанным в параметрах
     * @param email
     */
    private void sendMailTo(String email) {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        startActivity(Intent.createChooser(sendEmailIntent, getString(R.string.user_profile_choose_email_app)));
    }

    /**
     * Загружает веб страницу по ссылке, указанной в параметрах
     * @param link
     */
    private void openLink(String link) {
        if (!link.startsWith("http://") && !link.startsWith("https://")) {
            link = "http://" + link;
        }
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(openLinkIntent);
    }

    /**
     * Действие, производимое при нажатии кнопки Back на устройстве
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Отображает SnackBar с сообщением, указанным в параметрах
     * @param message
     */
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Настраивает ToolBar (используется при создании активности)
     */
    private void setupToolBar() {
        mCollapsingToolbar.setExpandedTitleTextAppearance(R.style.custom_toolbar);
        mCollapsingToolbar.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        mScrollFlags = mAppBarParams.getScrollFlags();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Настраивает Navigation Drawer (используется при создании активности)
     */
    private void setupDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            mAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
            Bitmap ava = BitmapFactory.decodeResource(getResources(), R.drawable.ava);
            RoundedBitmapDrawable roundedAva = RoundedBitmapDrawableFactory.create(getResources(), ava);
            roundedAva.setCircular(true);
            mAvatar.setImageDrawable(roundedAva);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    showSnackBar(item.getTitle().toString());
                    item.setChecked(true);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }
    }

    /**
     * Обрабатывает результат запроса на получение данных из другой активности (из камеры или галереи)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mEditMode);
    }

    /**
     * Переключает режим редактирования
     *
     * @param mode если 1 - режим редактирования, если 1 - режим просмотра
     */
    private void changeEditMode(int mode) {
        mEditMode = mode;
        boolean enableEdit = (mode == 1);
        for (View edit : mUserInfoViews) {
            edit.setFocusable(enableEdit);
            edit.setEnabled(enableEdit);
            edit.setFocusableInTouchMode(enableEdit);
        }

        if (enableEdit) {
            isDataValid();
            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
            setFocus(mPhone);
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            hideProfilePlaceholder();
            unlockToolbar();
            mCollapsingToolbar.setExpandedTitleColor(Color.WHITE);
            mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        }
    }

    /**
     * Переводит фокус на EditText, выставляет курсор в конец текста и открывает клавиатуру
     * @param editText EditText, на который нужно перевести фокус
     */
    private void setFocus(EditText editText) {
        if(editText.requestFocus()) {
            editText.setSelection(editText.length());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Загружает сохраненные данные, прописывая их в соответствующие поля интерфейса
     */
    private void initUserFields() {
        PreferencesManager pm = mDataManager.getPreferencesManager();
        List<String> userData = pm.loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            ((EditText) mUserInfoViews.get(i)).setText(userData.get(i));
        }
        setTitle(pm.loadUserName());
    }

    /**
     * Сохраняет введенные в поля данные в долгосрочную память
     */
    private void saveUserFields() {
        ArrayList<String> userData = new ArrayList<>();
        for (EditText et : mUserInfoViews) {
            userData.add(et.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    private void initUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++) {
            mUserValueViews.get(i).setText(userData.get(i));
        }
    }

    /**
     * Обращается к камере устройства для создания файла с фотографией
     */
    private void loadPhotoFromCamera() {
        if (permissionsCheck(new String[] {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                ConstantManager.CAMERA_REQUEST_PERMISSION_CODE)) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace(); // TODO: 05.07.2016 обработать ошибку
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        }
    }

    /**
     * Обращается к изображениям, сохраненным на устройстве, для выбора пользователем одного изображения.
     */
    private void loadPhotoFromGalery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent,
                getString(R.string.user_profile_choose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    /**
     * Обрабатывает оезультат запроса на получение разрешений
     * @param requestCode     идентификатор запроса разрешений
     * @param permissions     набор требуемых разрешений
     * @param grantResults    результат обработки запроса
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 06.07.2016 обработка разрешения (разрешение получено)
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 06.07.2016 обработка разрешения (разрешение получено)
            }
        }

        if (requestCode == ConstantManager.CALL_PHONE_REQUEST_PERMISSION_CODE) {
            // TODO: 06.07.2016 обработка разрешения
        }
    }

    /**
     * Скрывает область фотографии пользователя
     */
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    /**
     * Отображает область фотографии пользователя, если она была скрыта
     */
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    /**
     * Запрещает тулбару сворачиваться при прокрутке, оставляя его полностью раскрытым
     */
    private void lockToolbar() {
        // TODO: 04.07.2016 разобраться с дерганой анимацией раскрытия
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
    }

    /**
     * Разрешает тулбару сворачиваться при прокрутке
     */
    public void unlockToolbar() {
        mAppBarParams.setScrollFlags(mScrollFlags);
    }

    /**
     * Создает диалог в зависимости от переданного в параметрах идентификатора
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_galery),
                        getString(R.string.user_profile_dialog_photo),
                        getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title))
                        .setItems(selectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        loadPhotoFromGalery();
//                                        showSnackBar("Загрузить из галереи");
                                        break;
                                    case 1:
                                        loadPhotoFromCamera();
//                                        showSnackBar("Сделать снимок");
                                        break;
                                    case 2:
                                        dialog.cancel();
//                                        showSnackBar("Отмена");
                                }
                            }
                        });
                return builder.create();

            default:
                return null;
        }
    }

    /**
     * Создает пустой файл изображения. Используется для сохранения фотографии с камеры.
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * Назначает изображение в профиль пользователя
     * @param selectedImage путь к изображению пользователя
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(mSelectedImage)
                .into(mProfileImage);

        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }

    /**
     * Проверяет все пользовательские данные на корректность
     * @return
     */
    private boolean isDataValid() {
        boolean result = true;

        if (!isPhoneValid() | !isEmailValid() | !isVkValid() | !isGithubValid()) {
            result = false;
        }

        return result;
    }

    /**
     * Проверяет телефонный номер на корректность
     * @return
     */
    private boolean isPhoneValid() {
        String phone = mPhone.getText().toString();
        Pattern p = Pattern.compile("^(\\+?\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{2}[\\s.-]\\d{2}$");
        if (!p.matcher(phone).matches()) {
            mPhoneTextInput.setError(getString(R.string.user_data_error_message));
            return false;
        } else {
            mPhoneTextInput.setError(null);
            return true;
        }
    }

    /**
     * Проверяет электронную почту на корректность
     * @return
     */
    private boolean isEmailValid() {
        String email = mEmail.getText().toString();
        Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        if (!p.matcher(email).matches()) {
            mEmailTextInput.setError(getString(R.string.user_data_error_message));
            return false;
        } else {
            mEmailTextInput.setError(null);
            return true;
        }
    }

    /**
     * Проверяет ссылку на страницу вконтакте на корректность
     * @return
     */
    private boolean isVkValid() {
        String vkLink = mVk.getText().toString().toLowerCase();
        if (vkLink.startsWith("https://")) {
            vkLink = vkLink.substring(8);
            mVk.setText(vkLink);
        }
        Pattern p = Pattern.compile("^(vk\\.com/)\\w{3,}");
        if (!p.matcher(vkLink).matches()) {
            mVkTextInput.setError(getString(R.string.user_data_error_message));
            return false;
        } else {
            mVkTextInput.setError(null);
            return true;
        }
    }

    /**
     * Проверяет ссылку на репозиторий гитхаба на корректностью
     * @return
     */
    private boolean isGithubValid() {
        String githubLink = mGithub.getText().toString().toLowerCase();
        if (githubLink.startsWith("https://")) {
            githubLink = githubLink.substring(8);
            mGithub.setText(githubLink);
        }
        Pattern p = Pattern.compile("^(github\\.com/\\w{3,}/\\w{3,})");
        if (!p.matcher(githubLink).matches()) {
            mGithubTextInput.setError(getString(R.string.user_data_error_message));
            return false;
        } else {
            mGithubTextInput.setError(null);
            return true;
        }
    }

}
