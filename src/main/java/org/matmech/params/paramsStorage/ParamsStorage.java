package org.matmech.params.paramsStorage;

import java.util.ArrayList;
import java.util.List;

public class ParamsStorage {
    private List<UserParameter> users;

    public ParamsStorage() {
        users = new ArrayList<UserParameter>();
    }

    public void createParameter(int chatID, String tag) throws ParamsStorageException {
        
    }
}
