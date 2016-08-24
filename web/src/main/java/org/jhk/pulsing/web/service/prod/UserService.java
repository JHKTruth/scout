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
package org.jhk.pulsing.web.service.prod;

import java.util.Optional;

import javax.inject.Inject;

import org.jhk.pulsing.serialization.avro.records.User;
import org.jhk.pulsing.serialization.avro.records.UserId;
import org.jhk.pulsing.shared.util.CommonConstants;
import org.jhk.pulsing.web.common.Result;
import static org.jhk.pulsing.web.common.Result.CODE.*;
import org.jhk.pulsing.web.dao.prod.db.redis.RedisUserDao;
import org.jhk.pulsing.web.dao.prod.db.sql.MySqlUserDao;
import org.jhk.pulsing.web.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Note that if transactional bean is implementing an interface, by default the proxy will be a Java Dynamic Proxy. 
 * This means that only external method calls that come in through the proxy will be intercepted � any self-invocation calls 
 * will not start any transaction � even if the method is annotated with @Transactional.
 * 
 * Also rollback only occur during RuntimeException so paradigm is to throw a RuntimeException when you wish for a 
 * rollback
 * 
 * @author Ji Kim
 */
@Transactional
@Service
public class UserService extends AbstractStormPublisher
                            implements IUserService {
    
    @Inject
    private MySqlUserDao mySqlUserDao;
    
    @Inject
    private RedisUserDao redisUserDao;
    
    @Override
    public Result<User> getUser(UserId userId) {
        Result<User> result = new Result<>(FAILURE, "Unable to find " + userId);
        //TODO: remove later as holding this data in redis useless, but for docing 
        //for other data
        Optional<User> user = redisUserDao.getUser(userId);
        
        if(!user.isPresent()) {
            user = mySqlUserDao.getUser(userId);
        }
        
        if(user.isPresent()) {
            result = new Result<>(SUCCESS, user.get());
        }
        
        return result;
    }

    @Override
    public Result<User> createUser(User user) {
        Result<User> cUser = new Result<User>(FAILURE, "Failed in creating " + user); 
        
        if(cUser.getCode() == SUCCESS) {
            redisUserDao.createUser(cUser.getData());
            getStormPublisher().produce(CommonConstants.TOPICS.USER_CREATE.toString(), cUser.getData());
        }
        
        return cUser;
    }

    @Override
    public Result<User> validateUser(String email, String password) {
        Optional<User> user = mySqlUserDao.validateUser(email, password);
        
        return user.isPresent() ? new Result<>(SUCCESS, user.get()) : new Result<>(FAILURE, "Failed in validating " + email + " : " + password);
    }
    
}
