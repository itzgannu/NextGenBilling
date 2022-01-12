package mu.psi.nextgen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.databinding.ActivityStockUpdationBinding;
import mu.psi.nextgen.models.inventory.Stock;
import mu.psi.nextgen.models.inventory.StockUpdateNoURL;
import mu.psi.nextgen.viewModel.InventoryVM;

public class StockUpdation extends AppCompatActivity implements View.OnClickListener {

    ActivityStockUpdationBinding binding;

    FirebaseAuth auth;
    InventoryVM viewModel;
    DisplayName displayName;

    String company_name;

    Stock toDisplay = new Stock();
    String name, description, pack, price, inStock, pic_url;

    int RESULT_LOAD_IMAGE = 200;
    Uri url;
    byte[] uploadByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = ActivityStockUpdationBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        Intent i = getIntent();
        toDisplay = (Stock) i.getSerializableExtra("stock");

        auth = FirebaseAuth.getInstance();

        String d_name = Objects.requireNonNull(auth.getCurrentUser()).getDisplayName();
        displayName = new DisplayName(d_name);
        company_name = displayName.getCompanyName();

        viewModel = InventoryVM.getInstance(getApplication());

        assignValues();

        setNullErrors();

        this.binding.stockUpdateClose.setOnClickListener(this);
        this.binding.update.setOnClickListener(this);
        this.binding.delete.setOnClickListener(this);
        this.binding.choosePic.setOnClickListener(this);
    }

    private void assignValues() {
        name = toDisplay.getName();
        description = toDisplay.getDescription();
        pack = toDisplay.getPack();
        price = String.valueOf(toDisplay.getPrice());
        inStock = String.valueOf(toDisplay.getInStock());
        pic_url = toDisplay.getPic_url();

        Glide.with(this)
                .load(pic_url)
                .placeholder(R.drawable.basket)
                .into(this.binding.uploadPic);
        this.binding.stockUpdateTitle.setText(name);
        Objects.requireNonNull(this.binding.description.getEditText()).setText(description);
        Objects.requireNonNull(this.binding.pack.getEditText()).setText(pack);
        Objects.requireNonNull(this.binding.price.getEditText()).setText(price);
        Objects.requireNonNull(this.binding.inStock.getEditText()).setText(inStock);
    }

    private boolean validateFields(String newDescription, String newPack, String newPrice, String newInStock) {
        String emptyError = getString(R.string.error_field_empty);
        String lessError = getString(R.string.error_field_minimum_six);

        setNullErrors();

        if (newDescription.isEmpty() && newPack.isEmpty() && newPrice.isEmpty() && newInStock.isEmpty()) {
            setDescError(emptyError);
            setPackError(emptyError);
            setPriceError(emptyError);
            setInStockError(emptyError);
            return false;
        } else if (newDescription.isEmpty()) {
            setDescError(emptyError);
            setPackError(null);
            setPriceError(null);
            setInStockError(null);
            return false;
        } else if (newPack.isEmpty()) {
            setDescError(null);
            setPackError(emptyError);
            setPriceError(null);
            setInStockError(null);
            return false;
        } else if (newPrice.isEmpty()) {
            setDescError(null);
            setPackError(null);
            setPriceError(emptyError);
            setInStockError(null);
            return false;
        } else if (newInStock.isEmpty()) {
            setDescError(null);
            setPackError(null);
            setPriceError(null);
            setInStockError(emptyError);
            return false;
        } else if (newDescription.length() < 6 && newPack.length() < 6) {
            setDescError(lessError);
            setPackError(lessError);
            setPriceError(null);
            setInStockError(null);
            return false;
        }
        else if (newDescription.length() < 6) {
            setDescError(lessError);
            setPackError(null);
            setPriceError(null);
            setInStockError(null);
            return false;
        }
        else if (newPack.length() < 6) {
            setDescError(null);
            setPackError(lessError);
            setPriceError(null);
            setInStockError(null);
            return false;
        }else {
            setNullErrors();
            return true;
        }
    }

    private void setNullErrors() {
        setDescError(null);
        setPackError(null);
        setPriceError(null);
        setInStockError(null);
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

    private void getByteArrayData() {
        this.binding.uploadPic.setDrawingCacheEnabled(true);
        this.binding.uploadPic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) this.binding.uploadPic.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        uploadByte = byteArrayOutputStream.toByteArray();
    }

    private void updateDB() {
        String newDescription, newPack, newPrice, newInStock;
        newDescription = Objects.requireNonNull(this.binding.description.getEditText()).getText().toString();
        newPack = Objects.requireNonNull(this.binding.pack.getEditText()).getText().toString();
        newPrice = Objects.requireNonNull(this.binding.price.getEditText()).getText().toString();
        newInStock = Objects.requireNonNull(this.binding.inStock.getEditText()).getText().toString();
        getByteArrayData();

        if (validateFields(newDescription, newPack, newPrice, newInStock)) {
            double newP = Double.parseDouble(newPrice);
            int newIn = Integer.parseInt(newInStock);
            StockUpdateNoURL newStock = new StockUpdateNoURL(name, newDescription, newPack, newP, newIn);

            viewModel.updateExistingProduct(company_name, uploadByte, newStock);
        }
    }

    private void deleteDB() {
        viewModel.deleteExistingProduct(company_name, name);
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
                }
            }
        }
    }

    private void goBack() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goBack();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.update) {
            updateDB();
        } else if (id == R.id.delete) {
            deleteDB();
        } else if (id == R.id.choosePic) {
            selectPic();
        } else if (id == R.id.stock_update_close) {
            goBack();
        }
    }
}