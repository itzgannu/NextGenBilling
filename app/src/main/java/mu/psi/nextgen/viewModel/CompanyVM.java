package mu.psi.nextgen.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import mu.psi.nextgen.firebase.CompanyFirestore;
import mu.psi.nextgen.models.company.Admin;
import mu.psi.nextgen.models.company.Branch;

public class CompanyVM extends AndroidViewModel {

    CompanyFirestore companyFirestore = new CompanyFirestore();

    private static CompanyVM instance;

    public static CompanyVM getInstance(Application application) {
        if(instance == null) {
            instance =new CompanyVM(application);
        }
        return instance;
    }

    public CompanyVM(@NonNull Application application) {
        super(application);
    }

    public void writeAdminToCFS(Admin admin) {
        companyFirestore.createAdmin(admin);
        companyFirestore.createBranchRegistration(admin.getCompany_name());
    }

    public void writeFirstBranchToCFS(Branch branch) {
        companyFirestore.deleteBranchRegistration();
        companyFirestore.createBranch(branch);
    }

    public void writeBranchToCFS(Branch branch) {
        companyFirestore.createBranch(branch);
    }
}