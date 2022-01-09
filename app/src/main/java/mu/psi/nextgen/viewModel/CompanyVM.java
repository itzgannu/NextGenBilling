package mu.psi.nextgen.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import mu.psi.nextgen.firebase.CompanyFirestore;
import mu.psi.nextgen.models.company.Branch;

public class CompanyVM extends AndroidViewModel {

    CompanyFirestore companyFirestore = new CompanyFirestore();

    public List<Branch> branchList = new ArrayList<>();
    public MutableLiveData<List<Branch>> branchMLD = new MutableLiveData<>();


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


    public void readBranches(String company_name) {
        this.companyFirestore.readBranches(company_name);
        this.branchMLD = this.companyFirestore.branchMLD;
        this.branchList = this.branchMLD.getValue();
    }

    public void writeBranchToCFS(Branch branch) {
        companyFirestore.createBranch(branch);
    }
}