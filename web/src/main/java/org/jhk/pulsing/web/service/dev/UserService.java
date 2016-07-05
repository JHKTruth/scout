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
package org.jhk.pulsing.web.service.dev;

import javax.inject.Inject;

import org.jhk.pulsing.serialization.avro.records.User;
import org.jhk.pulsing.serialization.avro.records.UserId;
import org.jhk.pulsing.web.common.Result;
import org.jhk.pulsing.web.dao.IUserDao;
import org.jhk.pulsing.web.service.IUserService;

/**
 * @author Ji Kim
 */
public class UserService implements IUserService {
    
    @Inject
    private IUserDao userDao;
    
    @Override
    public Result<User> getUser(UserId userId) {
        return userDao.getUser(userId);
    }

    @Override
    public Result<User> createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public Result<User> validateUser(String email, String password) {
        return userDao.validateUser(email, password);
    }
    
}