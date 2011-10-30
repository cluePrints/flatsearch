package com.soboleiv.flatsearch.server.db;

import java.util.Collection;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.io.MemoryStorage;
import com.db4o.query.Predicate;
import com.google.common.collect.Lists;

public class DataStore<V> {
	private EmbeddedObjectContainer db;
	
	public V getByExample(V ex){
	    ObjectSet<Object> queryResults = db.queryByExample(ex);
	    if (queryResults.size() == 0)
	    	return null;
	    
		return (V) queryResults.get(0);
	}
	
	public Collection<V> getBy(Predicate<V> arg0) {
		ObjectSet<V> res = db.query(arg0);
		return serializationSafe(res);
	}
	
	public int count(Predicate<V> arg0) {
		return db.query(arg0).size();
	}
	
	public Collection<V> getAllByExample(V ex){
	    ObjectSet<V> results = db.queryByExample(ex);
		return serializationSafe(results);
	}

	private Collection<V> serializationSafe(Collection<V> results) {
		List<V> simpleResults = Lists.newLinkedList();
		simpleResults.addAll(results);
		return simpleResults;
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
	
	// TODO: it's singleton to save some dev time on DI wiring
	private static DataStore store;
	public synchronized static <V> DataStore<V> persistent() {
		if (store != null) {
			return store;
		}
		DataStore<V> res = new DataStore<V>();
		res.db = open(false);
		store = res;
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
