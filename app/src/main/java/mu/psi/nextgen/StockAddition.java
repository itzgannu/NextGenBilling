package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.databinding.ActivityStockAdditionBinding;
import mu.psi.nextgen.models.inventory.StockNoURL;
import mu.psi.nextgen.viewModel.InventoryVM;

public class StockAddition extends AppCompatActivity implements View.OnClickListener {

    ActivityStockAdditionBinding binding;

    FirebaseAuth auth;
    InventoryVM viewModel;
    DisplayName displayName;

    String company_name;

    int RESULT_LOAD_IMAGE = 200;
    Uri url;
    byte[] uploadByte;

    String name, description, pack, sPrice, sInStock;
    double price;
    int inStock;

    StockNoURL newStock;

    boolean selectedPic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityStockAdditionBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        auth = FirebaseAuth.getInstance();

        String d_name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        displayName = new DisplayName(d_name);
        company_name = displayName.getCompanyName();

        viewModel = InventoryVM.getInstance(getApplication());

        this.binding.stockAdditionClose.setOnClickListener(this);
        this.binding.stockAdditionSave.setOnClickListener(this);
        this.binding.choosePic.setOnClickListener(this);
    }

    private void saveToDB() {
        if (validateField()) {
            checkSelectPic();
            getByteArrayData();
            name = getName();
            description = getDescription();
            pack = getPack();
            price = getPrice();
            inStock = getInStock();

            newStock = new StockNoURL(name, description, pack, price, inStock);

            viewModel.writeNewProduct(company_name, uploadByte, newStock);

            clearTextField();
        }
    }

    private boolean validateField() {
        String localName = getName();
        String localDesc = getDescription();
        String localPack = getPack();
        String localPrice = getsPrice();
        String localStock = getsInStock();

        String emptyError = getString(R.string.error_field_empty);
        String lessError = getString(R.string.error_field_minimum_six);

        setNullErrors();

        if (localName.isEmpty() && localDesc.isEmpty() && localPack.isEmpty() && localPrice.isEmpty() && localStock.isEmpty()) {
            setNameError(emptyError);
            setDescError(emptyError);
            setPackError(emptyError);
            setPriceError(emptyError);
            setInStockError(emptyError);
            return false;
        } else if (localName.isEmpty()) {
            setNameError(emptyError);
            setDescError(null);
            setPackError(null);
            setPriceError(null);
            setInStockError(null);
            return false;
        } else if (localDesc.isEmpty()) {
            setNameError(null);
            setDescError(emptyError);
            setPackError(null);
            setPriceError(null);
            setInStockError(null);
            return false;
        } else if (localPack.isEmpty()) {
            setNameError(null);
            setDescError(null);
            setPackError(emptyError);
            setPriceError(null);
            setInStockError(null);
            return false;
        } else if (localPrice.isEmpty()) {
            setNameError(null);
            setDescError(null);
            setPackError(null);
            setPriceError(emptyError);
            setInStockError(null);
            return false;
        } else if (localStock.isEmpty()) {
            setNameError(null);
            setDescError(null);
            setPackError(null);
            setPriceError(null);
            setInStockError(emptyError);
            return false;
        } else if (localDesc.length() < 6 && localPack.length() < 6) {
            setNameError(null);
            setDescError(lessError);
            setPackError(lessError);
            setPriceError(null);
            setInStockError(null);
            return false;
        } else if (localDesc.length() < 6) {
            setNameError(null);
            setDescError(lessError);
            setPackError(null);
            setPriceError(null);
            setInStockError(null);
            return false;
        } else if (localPack.length() < 6) {
            setNameError(null);
            setDescError(null);
            setPackError(lessError);
            setPriceError(null);
            setInStockError(null);
            return false;
        } else {
            setNullErrors();
            return true;
        }
    }

    private String getName() {
        name = Objects.requireNonNull(this.binding.name.getEditText()).getText().toString();
        return name;
    }

    private String getDescription() {
        description = Objects.requireNonNull(this.binding.description.getEditText()).getText().toString();
        return description;
    }

    private String getPack() {
        pack = Objects.requireNonNull(this.binding.pack.getEditText()).getText().toString();
        return pack;
    }

    public String getsPrice() {
        sPrice = Objects.requireNonNull(this.binding.price.getEditText()).getText().toString();
        return sPrice;
    }

    public String getsInStock() {
        sInStock = Objects.requireNonNull(this.binding.inStock.getEditText()).getText().toString();
        return sInStock;
    }

    private double getPrice() {
        price = Double.parseDouble(Objects.requireNonNull(this.binding.price.getEditText()).getText().toString());
        return price;
    }

    private int getInStock() {
        inStock = Integer.parseInt(Objects.requireNonNull(this.binding.inStock.getEditText()).getText().toString());
        return inStock;
    }

    private void clearTextField() {
        Objects.requireNonNull(this.binding.name.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.description.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.pack.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.price.getEditText()).getText().clear();
        Objects.requireNonNull(this.binding.inStock.getEditText()).getText().clear();
        this.binding.uploadPic.setImageResource(R.drawable.basket);
        selectedPic = false;
    }

    private void setNullErrors() {
        setDescError(null);
        setPackError(null);
        setPriceError(null);
        setInStockError(null);
    }

    private void setNameError(String error) {
        this.binding.name.setError(error);
    }

    private void setDescError(String error) {
        this.binding.description.setError(error);
    }

    private void setPackError(String error) {
        this.binding.pack.setError(error);
    }

    private void setPriceError(String error) {
        this.binding.price.setError(error);
    }

    private void setInStockError(String error) {
        this.binding.inStock.setError(error);
    }

    private void checkSelectPic() {
        if (!selectedPic) {
            String message = getString(R.string.sa_dialog_message);
            String title = getString(R.string.sa_dialog_title);
            String okay = getString(R.string.okay);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setTitle(title);
            builder.setPositiveButton(okay, (dialog, id) -> dialog.dismiss());
            builder.create();
            builder.show();
        }
    }

    private void getByteArrayData() {
        this.binding.uploadPic.setDrawingCacheEnabled(true);
        this.binding.uploadPic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) this.binding.uploadPic.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        uploadByte = byteArrayOutputStream.toByteArray();
    }

    private void selectPic() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                Uri selectedImageUri = data.getData();
                url = selectedImageUri;
                if (null != selectedImageUri) {
                    this.binding.uploadPic.setImageURI(selectedImageUri);
                    selectedPic = true;
                }
            }
        }
    }

    private void goBack() {
        Intent goToBack = new Intent(StockAddition.this, HomeStocks.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(goToBack);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.choosePic) {
            selectPic();
        } else if (id == R.id.stock_addition_close) {
            goBack();
        } else if (id == R.id.stock_addition_save) {
            saveToDB();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }
}