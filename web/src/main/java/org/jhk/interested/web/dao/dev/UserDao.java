/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jhk.interested.web.dao.dev;

import org.jhk.interested.serialization.avro.records.User;
import org.jhk.interested.serialization.avro.records.UserId;
import org.jhk.interested.web.dao.IUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Ji Kim
 */
@Component
public class UserDao implements IUserDao {
    
    private static final Logger _LOGGER = LoggerFactory.getLogger(UserDao.class);

    @Override
    public User getUser(UserId userId) {
        _LOGGER.info("getUser", userId);
        
        return null;
    }

    @Override
    public void createUser(User user) {
        _LOGGER.info("createUser", user);
        
    }
    
}