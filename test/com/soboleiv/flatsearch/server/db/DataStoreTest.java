package com.soboleiv.flatsearch.server.db;

import java.util.Collection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataStoreTest {
	private Object TEST_OBJ = new Obj("aaa", "bbb");

	@Test
	public void inMemoryModeShouldBeCleanEachTime() {
		DataStore<Object> first = DataStore.forTests();
		first.save(TEST_OBJ);

		DataStore<Object> second = DataStore.forTests();
		Assert.assertEquals(0, second.getAllByExample(TEST_OBJ).size());
	}

	@Test
	public void onDiskDataSourceShouldBeSameForTwoCalls() {
		DataStore<Object> first = DataStore.persistent();
		first.save(TEST_OBJ);

		DataStore<Object> second = DataStore.persistent();
		Assert.assertEquals(1, second.getAllByExample(TEST_OBJ).size());
	}

	@Test
	public void shouldSearchObjectsByExample() {
		DataStore<Obj> first = DataStore.persistent();
		first.save(new Obj("111", "222"));
		first.save(new Obj("111", "ccc"));
		first.save(new Obj("ccc", "222"));

		Collection<Obj> results = first.getAllByExample(new Obj(
				null, "222"));
		for (Obj res : results) {
			Assert.assertEquals("222", res.field2);
		}
	}

	@Test
	public void shouldReturnEmptySetIfNoResultsFound() {
		DataStore<Obj> first = DataStore.persistent();
		first.save(new Obj("111", "222"));
		first.save(new Obj("111", "ccc"));
		first.save(new Obj("ccc", "222"));

		Collection<Obj> results = first.getAllByExample(new Obj(
				"aaa", "222"));
		Assert.assertEquals(0, results.size());
	}
	
	@Test
	public void shouldReturnNullIfNoResultsFound() {
		DataStore<Obj> first = DataStore.persistent();
		first.save(new Obj("111", "222"));
		first.save(new Obj("111", "ccc"));
		first.save(new Obj("ccc", "222"));

		Obj result = first.getByExample(new Obj(
				"aaa", "222"));
		Assert.assertNull(result);
	}

	@After
	public void after() {
		clean();
	}

	@Before
	public void before() {
		clean();
	}

	public void clean() {
		DataStore<Obj> db = DataStore.persistent();
		Collection<Obj> results = db.getAllByExample(new Obj(null,
				null));
		for (Obj i : results) {
			db.remove(i);
		}
	}

	static class Obj {
		String field1;
		String field2;

		public Obj(String field1, String field2) {
			super();
			this.field1 = field1;
			this.field2 = field2;
		}
	}
}