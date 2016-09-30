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
package org.jhk.pulsing.web.dao.prod.db.redis;

import static org.jhk.pulsing.shared.util.RedisConstants.REDIS_KEY.*;

import java.util.Optional;

import org.jhk.pulsing.serialization.avro.records.UserId;
import org.jhk.pulsing.shared.util.RedisConstants;
import org.jhk.pulsing.web.dao.prod.db.AbstractRedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author Ji Kim
 */
@Repository
public class RedisUserDao extends AbstractRedisDao {
    
    private static final Logger _LOGGER = LoggerFactory.getLogger(RedisUserDao.class);
    
    public void storeUserPicturePath(UserId userId, String path) {
        _LOGGER.debug("RedisUserDao.storeUserPicturePath: " + userId + " - " + path);
        
        getJedis().setex(USER_PICTURE_PATH_.toString() + userId.getId(), RedisConstants.CACHE_EXPIRE_DAY, path);
    }
    
    public Optional<String> getUserPicturePath(UserId userId) {
        _LOGGER.debug("RedisUserDao.getUserPicturePath: " + userId);
        
        return Optional.of(getJedis().get(USER_PICTURE_PATH_.toString() + userId.getId()));
    }
    
}
