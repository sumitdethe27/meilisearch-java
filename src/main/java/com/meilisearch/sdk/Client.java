/*
 * Unofficial Java client for Meilisearch
 */
package com.meilisearch.sdk;

import com.google.gson.Gson;
import com.meilisearch.sdk.exceptions.MeiliSearchApiException;

/** Meilisearch client */
public class Client {
    public Config config;
    public IndexesHandler indexesHandler;
    public Gson gson;
    public DumpHandler dumpHandler;

    /**
     * Calls instance for Meilisearch client
     *
     * @param config Configuration to connect to Meilisearch instance
     */
    public Client(Config config) {
        this.config = config;
        this.gson = new Gson();
        this.indexesHandler = new IndexesHandler(config);
        this.dumpHandler = new DumpHandler(config);
    }

    /**
     * Creates index Refer https://docs.meilisearch.com/reference/api/indexes.html#create-an-index
     *
     * @param uid Unique identifier for the index to create
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Index createIndex(String uid) throws Exception {
        return this.createIndex(uid, null);
    }

    /**
     * Creates index Refer https://docs.meilisearch.com/reference/api/indexes.html#create-an-index
     *
     * @param uid Unique identifier for the index to create
     * @param primaryKey The primary key of the documents in that index
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Index createIndex(String uid, String primaryKey) throws Exception {
        Index index = gson.fromJson(this.indexesHandler.create(uid, primaryKey), Index.class);
        index.setConfig(this.config);
        return index;
    }

    /**
     * Gets all indexes Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return list of indexes in the Meilisearch client
     * @throws Exception if an error occurs
     */
    public Index[] getIndexList() throws Exception {
        Index[] meiliSearchIndexList = gson.fromJson(getRawIndexList(), Index[].class);
        for (Index indexes : meiliSearchIndexList) {
            indexes.setConfig(this.config);
        }
        return meiliSearchIndexList;
    }

    /**
     * Gets all indexes https://docs.meilisearch.com/reference/api/indexes.html#list-all-indexes
     *
     * @return Meilisearch API response as String
     * @throws Exception if an error occurs
     */
    public String getRawIndexList() throws Exception {
        return this.indexesHandler.getAll();
    }

    /**
     * Creates a local reference to an index identified by `uid`, without doing an HTTP call.
     * Calling this method doesn't create an index by itself, but grants access to all the other
     * methods in the Index class.
     *
     * @param uid Unique identifier of the index
     * @return Index instance
     * @throws Exception if an error occurs
     */
    public Index index(String uid) throws Exception {
        Index index = new Index();
        index.uid = uid;
        index.setConfig(this.config);
        return index;
    }

    /**
     * Gets single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#get-one-index
     *
     * @param uid Unique identifier of the index to get
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Index getIndex(String uid) throws Exception {
        Index indexes = gson.fromJson(getRawIndex(uid), Index.class);
        indexes.setConfig(this.config);
        return indexes;
    }

    /**
     * Gets single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#get-one-index
     *
     * @param uid Unique identifier of the index to get
     * @return Meilisearch API response as String
     * @throws Exception if an error occurs
     */
    public String getRawIndex(String uid) throws Exception {
        return this.indexesHandler.get(uid);
    }

    /**
     * Updates single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#update-an-index
     *
     * @param uid Unique identifier of the index to update
     * @param primaryKey Primary key of the documents in the index
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public Index updateIndex(String uid, String primaryKey) throws Exception {
        Index index =
                gson.fromJson(this.indexesHandler.updatePrimaryKey(uid, primaryKey), Index.class);
        index.setConfig(this.config);
        return index;
    }

    /**
     * Deletes single index by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#get-one-index
     *
     * @param uid Unique identifier of the index to delete
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public String deleteIndex(String uid) throws Exception {
        return this.indexesHandler.delete(uid);
    }

    /**
     * Deletes single index if exists by uid Refer
     * https://docs.meilisearch.com/reference/api/indexes.html#get-one-index
     *
     * @param uid Unique identifier of the index to delete
     * @return Meilisearch API response
     * @throws Exception if an error occurs
     */
    public boolean deleteIndexIfExists(String uid) throws Exception {
        return this.indexesHandler.deleteIfExists(uid);
    }

    /**
     * Gets single index by uid or if it does not exists, Create index
     *
     * @param uid Unique identifier for the index to create
     * @param primaryKey The primary key of the documents in that index
     * @return Index instance
     * @throws Exception if an error occurs
     */
    public Index getOrCreateIndex(String uid, String primaryKey) throws Exception {
        try {
            return this.getIndex(uid);
        } catch (MeiliSearchApiException e) {
            if (e.getErrorCode().equals("index_not_found")) {
                return this.createIndex(uid, primaryKey);
            }
            throw e;
        }
    }

    /**
     * Gets single index by uid or if it does not exists, Create index
     *
     * @param uid Unique identifier for the index to create
     * @return Index instance
     * @throws Exception if an error occurs
     */
    public Index getOrCreateIndex(String uid) throws Exception {
        return getOrCreateIndex(uid, null);
    }

    /**
     * Triggers the creation of a Meilisearch dump. Refer
     * https://docs.meilisearch.com/reference/api/dump.html#create-a-dump
     *
     * @return Dump instance
     * @throws Exception if an error occurs
     */
    public Dump createDump() throws Exception {
        return this.dumpHandler.createDump();
    }

    /**
     * Gets the status of a Meilisearch dump.
     * https://docs.meilisearch.com/reference/api/dump.html#get-dump-status
     *
     * @param uid Unique identifier for correspondent dump
     * @return String with dump status
     * @throws Exception if an error occurs
     */
    public String getDumpStatus(String uid) throws Exception {
        return this.dumpHandler.getDumpStatus(uid);
    }
}
