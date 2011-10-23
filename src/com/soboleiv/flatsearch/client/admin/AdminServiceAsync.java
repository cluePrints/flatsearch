package com.soboleiv.flatsearch.client.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.soboleiv.flatsearch.shared.AdminResponse;

public interface AdminServiceAsync {
	public void checkDataSources(AsyncCallback<AdminResponse> callback);
}
