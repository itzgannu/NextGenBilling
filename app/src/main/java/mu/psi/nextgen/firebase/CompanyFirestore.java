package mu.psi.nextgen.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import mu.psi.nextgen.models.company.Admin;
import mu.psi.nextgen.models.company.Branch;
import mu.psi.nextgen.models.company.Cashier;
import mu.psi.nextgen.models.company.Staff;

public class CompanyFirestore {

    private final FirebaseFirestore fStore;
    private final FirebaseAuth auth;

    private final String FIRST_COLLECTION_NAME = "MuPsiCorp";
    String FIRST_DOCUMENT_NAME = "CompanyName";
    private final String SECOND_COLLECTION_NAME_USERS = "WorkForce";
    private final String SECOND_COLLECTION_NAME_BRANCHES = "Branches";

    public CompanyFirestore() {
        this.fStore = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public void createBranchRegistration(String company_name) {
        if(auth.getCurrentUser() != null) {
            FIRST_DOCUMENT_NAME = company_name;

            Branch branch = new Branch(FIRST_DOCUMENT_NAME, "NR", "NR");
            Map<String, Object> branchMap = branch.toMap();
            String branch_name = branch.getBranch_name();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_NAME_BRANCHES)
                    .document(branch_name)
                    .set(branchMap);
        }
    }

    public void deleteBranchRegistration() {
        if(auth.getCurrentUser() != null) {
            FIRST_DOCUMENT_NAME = auth.getCurrentUser().getDisplayName();
            String branch_name = "NR";

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_NAME_BRANCHES)
                    .document(branch_name)
                    .delete();
        }
    }

    public void createAdmin(Admin admin) {
        if(auth.getCurrentUser() != null) {
            String uuid = auth.getCurrentUser().getUid();
            Map<String, Object> adminMap = admin.toMap();
            FIRST_DOCUMENT_NAME = admin.getCompany_name();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_NAME_USERS)
                    .document(uuid)
                    .set(adminMap);
        }
    }

    public void createCashier(Cashier cashier) {
        if(auth.getCurrentUser() != null) {
            String uuid = auth.getCurrentUser().getUid();
            Map<String, Object> cashierMap = cashier.toMap();
            FIRST_DOCUMENT_NAME = cashier.getCompany_name();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_NAME_USERS)
                    .document(uuid)
                    .set(cashierMap);
        }
    }

    public void createWorker(Staff staff) {
        if(auth.getCurrentUser() != null) {
            String uuid = auth.getCurrentUser().getUid();
            Map<String, Object> staffMap = staff.toMap();
            FIRST_DOCUMENT_NAME = staff.getCompany_name();

            fStore.collection(FIRST_COLLECTION_NAME)
                    .document(FIRST_DOCUMENT_NAME)
                    .collection(SECOND_COLLECTION_NAME_USERS)
                    .document(uuid)
                    .set(staffMap);
        }
    }

    public void readWorkForce() {
        if(auth.getCurrentUser() != null) {

        }

    }

    public void createBranch(Branch branch) {
        if(auth.getCurrentUser() != null) {

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
}
