package com.soboleiv.flatsearch.server.db;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.io.MemoryStorage;

public class DataStore<V> {
	private EmbeddedObjectContainer db;
	
	public V getByExample(V ex){
	    ObjectSet<Object> queryResults = db.queryByExample(ex);
	    if (queryResults.size() == 0)
	    	return null;
		return (V) queryResults.get(0);
	}
	
	public void save(V val) {		
		try {
		    db.store(val);
		} finally {
		    db.commit();
		}
	}
	
	public void remove(V val) {		
		try {
		    db.delete(val);
		} finally {
		    db.commit();
		}
	}
	
	public static <V> DataStore<V> forTests() {
		DataStore<V> res = new DataStore<V>();
		res.db = open(true);
		res.removeAll();
		return res;
	}
	
	public static <V> DataStore<V> persistent() {
		DataStore<V> res = new DataStore<V>();
		res.db = open(false);
		res.removeAll();
		return res;
	}

	private static EmbeddedObjectContainer open(boolean inMemory) {
		EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
		if (inMemory) {
			MemoryStorage memory = new MemoryStorage();
			configuration.file().storage(memory);
		}
		EmbeddedObjectContainer container = Db4oEmbedded.openFile(configuration, "db4o");
		return container;
	}

	private void removeAll() {
		ObjectSet<?> objects = db.queryByExample(null);
	    for (Object object : objects) {
	        db.delete(object);
	    }
	    db.commit();
	}

}
