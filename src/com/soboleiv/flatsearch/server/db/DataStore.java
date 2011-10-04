package com.soboleiv.flatsearch.server.db;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.io.MemoryStorage;

public class DataStore<V> {
	public static boolean inMemory = false;
	
	public V getByExample(V ex){
		EmbeddedObjectContainer db = open();
		try {
		    ObjectSet<Object> queryResults = db.queryByExample(ex);
		    if (queryResults.size() == 0)
		    	return null;
			return (V) queryResults.get(0);
		} finally {
		    db.close();
		}
	}

	public EmbeddedObjectContainer open() {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		if (inMemory) {
			MemoryStorage memory = new MemoryStorage();
			configuration.file().storage(memory);
		}
		return Db4oEmbedded.openFile(configuration, "database.db4o");
	}
	
	public void save(V val) {
		EmbeddedObjectContainer db = open();
		
		try {
		    db.store(val);
		} finally {
		    db.close();
		}
	}
}
