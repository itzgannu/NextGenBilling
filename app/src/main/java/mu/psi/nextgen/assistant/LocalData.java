package mu.psi.nextgen.assistant;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {

    SharedPreferences sharedPreferences;
    static final String preferenceName = "MuPsiBilling";
    static final String branchName = "BranchName";

    public void setBranchName(Context context, String name) {
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(branchName, name);
        editor.commit();
    }

    public String getBranchName(Context context) {
        sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(branchName)) {
            return sharedPreferences.getString(branchName, "");
        } else {
            return "";
        }
    }
}
