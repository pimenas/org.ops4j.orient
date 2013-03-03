/*
 * Copyright 2013 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ops4j.orient.spring.tx;

import javax.annotation.PostConstruct;

import com.orientechnologies.orient.core.db.ODatabaseComplex;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;

/**
 * Abstract base class for OrientDB factories. Each concrete implementation is responsible 
 * for a given OrientDB type, i.e. document, object or graph database.
 * @author Harald Wellmann
 * 
 */
public abstract class AbstractOrientDatabaseFactory {
    
    public static final String DEFAULT_USERNAME = "admin";
    public static final String DEFAULT_PASSWORD = "admin";

    private String url;

    private String username = DEFAULT_USERNAME;

    private String password = DEFAULT_PASSWORD;
    
    private int minPoolSize = 1;
    
    private int maxPoolSize = 20;

    @PostConstruct
    public void init() {
        if (url == null) {
            throw new IllegalArgumentException("url property must not be null");
        }
        createPool();
        ODatabaseComplex<?> db = newDatabase();
        createDatabase(db);
        openDatabase();        
    }
    
    protected abstract void createPool();
    protected abstract ODatabaseComplex<?> openDatabase();
    protected abstract ODatabaseComplex<?> newDatabase();

    public ODatabaseComplex<?> db() {
        return ODatabaseRecordThreadLocal.INSTANCE.get().getDatabaseOwner();
    }
    

    protected void createDatabase(ODatabaseComplex<?> db) {
        if (getUrl().startsWith("memory:")) {
            if (!db.exists()) {
                db.create();
            }
            else {
                db.open(getUsername(), getPassword());
            }
        }
        else {
            if (!db.exists()) {
                db.create();
                db.close();
            }
        }
    }
    
    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    

    
    /**
     * @return the minPoolSize
     */
    public int getMinPoolSize() {
        return minPoolSize;
    }

    
    /**
     * @param minPoolSize the minPoolSize to set
     */
    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    
    /**
     * @return the maxPoolSize
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    
    /**
     * @param maxPoolSize the maxPoolSize to set
     */
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
}