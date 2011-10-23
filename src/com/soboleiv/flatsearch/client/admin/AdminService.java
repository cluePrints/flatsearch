package com.soboleiv.flatsearch.client.admin;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.soboleiv.flatsearch.shared.AdminResponse;

@RemoteServiceRelativePath("admin")
public interface AdminService extends RemoteService{
	public AdminResponse checkDataSources();
}
