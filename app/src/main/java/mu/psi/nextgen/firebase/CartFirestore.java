package mu.psi.nextgen.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.models.cart.Item;

public class CartFirestore {

    private final FirebaseFirestore fStore;

    DisplayName displayName;

    private final String FIRST_COLLECTION_NAME = "MuPsiCorp";
    String FIRST_DOCUMENT_NAME = "CompanyName";
    String SECOND_COLLECTION_DISPLAY_NAME = "DisplayName";
    String SECOND_DOCUMENT_NAME = "ProductName";

    public List<Item> itemList = new ArrayList<>();
    public MutableLiveData<List<Item>> itemMLD = new MutableLiveData<>();
    public MutableLiveData<Item> singleItemMLD = new MutableLiveData<>();

    public CartFirestore() {
        this.fStore = FirebaseFirestore.getInstance();
        itemList.clear();
    }

    public void updateCart(String display_name, Item updatedItem) {
        try {
            displayName = new DisplayName(display_name);
            FIRST_DOCUMENT_NAME = displayName.getCompanyName();
            SECOND_COLLECTION_DISPLAY_NAME = displayName.getDisplayName();
            SECOND_DOCUMENT_NAME = updatedItem.getName();

            Map<String, Object> productMap = updatedItem.toMap();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_DISPLAY_NAME)
                    .document(SECOND_DOCUMENT_NAME)
                    .set(productMap);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void deleteCart(String display_name, String product_name) {
        try {
            displayName = new DisplayName(display_name);
            FIRST_DOCUMENT_NAME = displayName.getCompanyName();
            SECOND_COLLECTION_DISPLAY_NAME = displayName.getDisplayName();
            SECOND_DOCUMENT_NAME = product_name;

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_DISPLAY_NAME)
                    .document(SECOND_DOCUMENT_NAME)
                    .delete();
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    public void emptyCart(String display_name) {
        try {
            displayName = new DisplayName(display_name);
            FIRST_DOCUMENT_NAME = displayName.getCompanyName();
            SECOND_COLLECTION_DISPLAY_NAME = displayName.getDisplayName();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_DISPLAY_NAME)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()) {
                                Item item = documentChange.getDocument().toObject(Item.class);
                                SECOND_DOCUMENT_NAME = item.getName();
                                fStore.collection(FIRST_COLLECTION_NAME)
                                        .document(FIRST_DOCUMENT_NAME)
                                        .collection(SECOND_COLLECTION_DISPLAY_NAME)
                                        .document(SECOND_DOCUMENT_NAME)
                                        .delete();
                            }
                        }
                    });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    public void getItems(String display_name) {
        try {
            displayName = new DisplayName(display_name);
            FIRST_DOCUMENT_NAME = displayName.getCompanyName();
            SECOND_COLLECTION_DISPLAY_NAME = displayName.getDisplayName();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_DISPLAY_NAME)
                    .addSnapshotListener((value, error) -> {
                        try {
                            if (value != null && !value.isEmpty()) {
                                for (DocumentChange documentChange : value.getDocumentChanges()) {
                                    switch (documentChange.getType()) {
                                        case ADDED:
                                            Item fetchedStock = documentChange.getDocument().toObject(Item.class);
                                            boolean found = false;
                                            for (int i = 0; i < itemList.size(); i++) {
                                                if (fetchedStock.getName().equalsIgnoreCase(itemList.get(i).getName())) {
                                                    found = true;
                                                }
                                            }
                                            if (!found) {
                                                itemList.add(fetchedStock);
                                            }
                                        case REMOVED:
                                            Item removedStock = documentChange.getDocument().toObject(Item.class);
                                            itemList.remove(removedStock);
                                        case MODIFIED:
                                            Item updatedStock = documentChange.getDocument().toObject(Item.class);
                                            if (updatedStock.getName().equalsIgnoreCase(documentChange.getDocument().getId())) {
                                                for (int i = 0; i < itemList.size(); i++) {
                                                    if (updatedStock.getName().equalsIgnoreCase(itemList.get(i).getName())) {
                                                        itemList.set(i, updatedStock);
                                                    }
                                                }
                                            }
                                    }
                                }
                            }
                            itemMLD.postValue(itemList);
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }

                    });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void getItem(String display_name, String item_name) {
        try {
            displayName = new DisplayName(display_name);
            FIRST_DOCUMENT_NAME = displayName.getCompanyName();
            SECOND_COLLECTION_DISPLAY_NAME = displayName.getDisplayName();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_DISPLAY_NAME)
                    .document(item_name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot.exists()) {
                                    Item getItem = documentSnapshot.toObject(Item.class);
                                    singleItemMLD.setValue(getItem);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}
