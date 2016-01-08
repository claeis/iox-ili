package ch.interlis.iom_j.itf.impl;

import java.io.IOException;

import jdbm.PrimaryTreeMap;
import jdbm.RecordManager;
import ch.interlis.iom.IomObject;
import ch.interlis.iox.IoxException;

public class ObjectPoolManager<T> {

	private RecordManager recman = null;
	private String cacheFileBasename = null;

	public ObjectPoolManager() {
		cacheFileBasename = JdbmUtility.getCacheTmpFilename();
	}

	public java.util.Map<String, T> newObjectPool() {
		try {
			if (recman != null) {
				recman.close();
				JdbmUtility.removeRecordManagerFiles(cacheFileBasename);
			}
			recman = JdbmUtility.createRecordManager(cacheFileBasename);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		PrimaryTreeMap<String, T> m = recman.treeMap("hugemap");
		return m;

	}

	public void close() {
		if (recman != null) {
			try {
				recman.close();
				JdbmUtility.removeRecordManagerFiles(cacheFileBasename);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		recman = null;
	}

}
