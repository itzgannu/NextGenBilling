package mu.psi.nextgen.firebase;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mu.psi.nextgen.assistant.DisplayName;
import mu.psi.nextgen.models.bills.Invoice;

public class BillsFirestore {

    private final FirebaseFirestore fStore;

    private final String FIRST_COLLECTION_NAME = "MuPsiCorp";
    String FIRST_DOCUMENT_NAME = "CompanyName";
    private final String SECOND_COLLECTION_BILLS = "Bills";
    String SECOND_DOCUMENT_NAME = "OrderNumber";

    String FIRST_BUYER_DOCUMENT_NAME = "BuyerEmail";

    public List<Invoice> invoiceList = new ArrayList<>();
    public MutableLiveData<List<Invoice>> invoiceMLD = new MutableLiveData<>();

    public BillsFirestore() {
        this.fStore = FirebaseFirestore.getInstance();
        invoiceList.clear();
    }

    public void addInvoice(Invoice newInvoice) {
        FIRST_DOCUMENT_NAME = newInvoice.getCompany_name();
        SECOND_DOCUMENT_NAME = newInvoice.getOrder_number();

        try {
            Map<String, Object> productMap = newInvoice.toMap();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_BILLS)
                    .document(SECOND_DOCUMENT_NAME)
                    .set(productMap);
        }
        catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void addBuyerBill(Invoice newInvoice) {
        FIRST_BUYER_DOCUMENT_NAME = newInvoice.getCustomer_email();
        SECOND_DOCUMENT_NAME = newInvoice.getOrder_number();

        try {
            Map<String, Object> productMap = newInvoice.toMap();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_BUYER_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_BILLS)
                    .document(SECOND_DOCUMENT_NAME)
                    .set(productMap);
        }
        catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void shareBuyerBill(Invoice newInvoice, String email) {
        FIRST_BUYER_DOCUMENT_NAME = email;
        SECOND_DOCUMENT_NAME = newInvoice.getOrder_number();

        try {
            Map<String, Object> productMap = newInvoice.toMap();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_BUYER_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_BILLS)
                    .document(SECOND_DOCUMENT_NAME)
                    .set(productMap);
        }
        catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void getAllBills(String display_name) {
        try {
            DisplayName displayName = new DisplayName(display_name);
            FIRST_DOCUMENT_NAME = displayName.getCompanyName();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_BILLS)
                    .orderBy("order_date_time", Query.Direction.DESCENDING)
                    .addSnapshotListener((value, error) -> {
                        try {
                            if (value != null && !value.isEmpty()) {
                                for (DocumentChange documentChange : value.getDocumentChanges()) {
                                    Invoice fetchedInvoice = documentChange.getDocument().toObject(Invoice.class);
                                    invoiceList.add(fetchedInvoice);
                                }
                            }
                            invoiceMLD.postValue(invoiceList);
                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}
