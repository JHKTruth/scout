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
package org.jhk.pulsing.web.service;

import java.util.List;

import org.jhk.pulsing.client.payload.Result;
import org.jhk.pulsing.web.pojo.light.Invitation;
import org.jhk.pulsing.serialization.avro.records.UserId;
import org.jhk.pulsing.shared.util.RedisConstants.INVITATION_ID;

/**
 * @author Ji Kim
 */
public interface IInvitationService {
    
    Result<List<Invitation>> getAlertList(UserId userId);

    /**
     * Creates an expiring invitationId for the userId (i.e. for /chatLobbyInvite action)
     * 
     * @param userId
     * @param prefix
     * @param expiration in seconds
     * @return
     */
    String createInvitationId(long toUserId, long fromUserId, INVITATION_ID prefix, int expiration);
    
    boolean removeInvitationId(long userId, String invitationId);
    
}
