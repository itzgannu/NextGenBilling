package mu.psi.nextgen.firebase;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mu.psi.nextgen.models.company.Branch;

public class CompanyFirestore {

    private final FirebaseFirestore fStore;
    private final FirebaseAuth auth;

    private final String FIRST_COLLECTION_NAME = "MuPsiCorp";
    String FIRST_DOCUMENT_NAME = "CompanyName";
    private final String SECOND_COLLECTION_NAME_BRANCHES = "Branches";

    public List<Branch> branchList = new ArrayList<>();
    public MutableLiveData<List<Branch>> branchMLD = new MutableLiveData<>();

    public CompanyFirestore() {
        this.fStore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public void createBranch(Branch branch) {
        if (auth.getCurrentUser() != null) {

            Map<String, Object> branchMap = branch.toMap();
            FIRST_DOCUMENT_NAME = branch.getCompany_name();
            String branch_name = branch.getBranch_name();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_NAME_BRANCHES)
                    .document(branch_name)
                    .set(branchMap);
        }
    }

    public void readBranches(String company_name) {
        if (auth.getCurrentUser() != null) {
            FIRST_DOCUMENT_NAME = company_name;
            try {
                fStore.collection(FIRST_COLLECTION_NAME)
                        .document(FIRST_DOCUMENT_NAME)
                        .collection(SECOND_COLLECTION_NAME_BRANCHES)
                        .addSnapshotListener((value, error) -> {
                            try {
                                if (value != null && !value.isEmpty()) {
                                    branchList.clear();
                                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                                        switch (documentChange.getType()) {
                                            case ADDED:
                                                Branch addBranchForce = documentChange.getDocument().toObject(Branch.class);
                                                branchList.add(addBranchForce);
                                            case REMOVED:
                                                Branch removeBranchForce = documentChange.getDocument().toObject(Branch.class);
                                                branchList.remove(removeBranchForce);
                                        }
                                    }
                                }
                                branchMLD.postValue(branchList);
                            } catch (Exception e){
                                e.getLocalizedMessage();
                            }
                        });
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
    }
}
