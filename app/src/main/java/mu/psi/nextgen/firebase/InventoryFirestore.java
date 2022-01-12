package mu.psi.nextgen.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mu.psi.nextgen.models.inventory.Stock;
import mu.psi.nextgen.models.inventory.StockNoURL;
import mu.psi.nextgen.models.inventory.StockUpdateNoURL;

public class InventoryFirestore {

    private final FirebaseFirestore fStore;
    private final FirebaseStorage fStorage;

    private final String FIRST_COLLECTION_NAME = "MuPsiCorp";
    String FIRST_DOCUMENT_NAME = "CompanyName";
    private final String SECOND_COLLECTION_NAME_STOCK = "Stock";
    String SECOND_DOCUMENT_NAME = "ProductName";

    String STORAGE_FIRST_FOLDER_NAME = "CompanyName";
    String STORAGE_FILE_NAME = "ProductName";

    public List<Stock> stockList = new ArrayList<>();
    public MutableLiveData<List<Stock>> stockMLD = new MutableLiveData<>();

    public InventoryFirestore() {
        this.fStore = FirebaseFirestore.getInstance();
        this.fStorage = FirebaseStorage.getInstance();
        stockList.clear();
    }

    public void addNewStock(String company_name, byte[] uploadByte, StockNoURL product) {
        try {
            FIRST_DOCUMENT_NAME = company_name;
            SECOND_DOCUMENT_NAME = product.getName();

            STORAGE_FIRST_FOLDER_NAME = company_name;

            Map<String, Object> productMap = product.toMap();

            StorageReference storageRef = fStorage.getReference();
            STORAGE_FILE_NAME = product.getName();
            StorageReference imagesRef = storageRef.child(STORAGE_FIRST_FOLDER_NAME).child(STORAGE_FILE_NAME);

            UploadTask uploadTask = imagesRef.putBytes(uploadByte);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            productMap.put("pic_url", uri.toString());

                            fStore.collection(FIRST_COLLECTION_NAME)
                                    .document(FIRST_DOCUMENT_NAME)
                                    .collection(SECOND_COLLECTION_NAME_STOCK)
                                    .document(SECOND_DOCUMENT_NAME)
                                    .set(productMap);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void updateStock(String company_name, byte[] uploadByte, StockUpdateNoURL product) {
        try {
            FIRST_DOCUMENT_NAME = company_name;
            SECOND_DOCUMENT_NAME = product.getName();

            STORAGE_FIRST_FOLDER_NAME = company_name;

            Map<String, Object> productMap = product.toMap();

            StorageReference storageRef = fStorage.getReference();
            STORAGE_FILE_NAME = product.getName();
            StorageReference imagesRef = storageRef.child(STORAGE_FIRST_FOLDER_NAME).child(STORAGE_FILE_NAME);

            UploadTask uploadTask = imagesRef.putBytes(uploadByte);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            productMap.put("pic_url", uri.toString());

                            fStore.collection(FIRST_COLLECTION_NAME)
                                    .document(FIRST_DOCUMENT_NAME)
                                    .collection(SECOND_COLLECTION_NAME_STOCK)
                                    .document(SECOND_DOCUMENT_NAME)
                                    .set(productMap);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void deleteStock(String company_name, String product_name) {
        try {
            FIRST_DOCUMENT_NAME = company_name;
            SECOND_DOCUMENT_NAME = product_name;

            STORAGE_FIRST_FOLDER_NAME = company_name;

            StorageReference storageRef = fStorage.getReference();
            STORAGE_FILE_NAME = product_name;
            StorageReference imagesRef = storageRef.child(STORAGE_FIRST_FOLDER_NAME).child(STORAGE_FILE_NAME);

            imagesRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    fStore.collection(FIRST_COLLECTION_NAME)
                            .document(FIRST_DOCUMENT_NAME)
                            .collection(SECOND_COLLECTION_NAME_STOCK)
                            .document(SECOND_DOCUMENT_NAME)
                            .delete();
                }
            });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void getAllStocks(String company_name) {
        try {
            FIRST_DOCUMENT_NAME = company_name;
            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(company_name)
                    .collection(SECOND_COLLECTION_NAME_STOCK)
                    .addSnapshotListener((value, error) -> {
                        try {
                            if (value != null && ! value.isEmpty()) {
                                for (DocumentChange documentChange : value.getDocumentChanges()) {
                                    switch (documentChange.getType()) {
                                        case ADDED:
                                            Stock fetchedStock = documentChange.getDocument().toObject(Stock.class);
                                            boolean found = false;
                                            for (int i = 0; i < stockList.size(); i++) {
                                                if (fetchedStock.getName().equalsIgnoreCase(stockList.get(i).getName())) {
                                                    found = true;
                                                }
                                            }
                                            if(!found) {
                                                stockList.add(fetchedStock);
                                            }
                                        case REMOVED:
                                            Stock removedStock = documentChange.getDocument().toObject(Stock.class);
                                            stockList.remove(removedStock);
                                        case MODIFIED:
                                            Stock updatedStock = documentChange.getDocument().toObject(Stock.class);
                                            if (updatedStock.getName().equalsIgnoreCase(documentChange.getDocument().getId())) {
                                                for (int i = 0; i < stockList.size(); i++) {
                                                    if (updatedStock.getName().equalsIgnoreCase(stockList.get(i).getName())) {
                                                        stockList.set(i, updatedStock);
                                                    }
                                                }
                                            }
                                    }
                                }
                            }
                            stockMLD.postValue(stockList);
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }

                    });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}
