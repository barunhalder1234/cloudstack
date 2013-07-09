// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.api.command.admin.swift;

import java.util.HashMap;
import java.util.Map;

import org.apache.cloudstack.api.APICommand;
import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.ApiErrorCode;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.command.admin.storage.AddImageStoreCmd;
import org.apache.cloudstack.api.response.ImageStoreResponse;
import org.apache.log4j.Logger;

import com.cloud.exception.DiscoveryException;
import com.cloud.storage.ImageStore;
import com.cloud.user.Account;

@APICommand(name = "addSwift", description = "Adds Swift.", responseObject = ImageStoreResponse.class, since="3.0.0")
public class AddSwiftCmd extends BaseCmd {
    public static final Logger s_logger = Logger.getLogger(AddSwiftCmd.class.getName());
    private static final String s_name = "addswiftresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name = ApiConstants.URL, type = CommandType.STRING, required = true, description = "the URL for swift")
    private String url;

    @Parameter(name = ApiConstants.ACCOUNT, type = CommandType.STRING, description = "the account for swift")
    private String account;

    @Parameter(name = ApiConstants.USERNAME, type = CommandType.STRING, description = "the username for swift")
    private String username;

    @Parameter(name = ApiConstants.KEY, type = CommandType.STRING, description = " key for the user for swift")
    private String key;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getUrl() {
        return url;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    public String getAccount() {
        return account;
    }

    public String getUsername() {
        return username;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public long getEntityOwnerId() {
        return Account.ACCOUNT_ID_SYSTEM;
    }

    @Override
    public void execute(){
        AddImageStoreCmd cmd = new AddImageStoreCmd();
        cmd.setProviderName("Swift");
        cmd.setUrl(this.getUrl());
        Map<String, String> details = new HashMap<String, String>();
        details.put(ApiConstants.ACCOUNT, this.getAccount());
        details.put(ApiConstants.USERNAME, this.getUsername());
        details.put(ApiConstants.KEY, this.getKey());


        try{
            ImageStore result = _storageService.discoverImageStore(cmd);
            ImageStoreResponse storeResponse = null;
            if (result != null ) {
                    storeResponse = _responseGenerator.createImageStoreResponse(result);
                    storeResponse.setResponseName(getCommandName());
                    storeResponse.setObjectName("secondarystorage");
                    this.setResponseObject(storeResponse);
            } else {
                throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, "Failed to add secondary storage");
            }
        } catch (DiscoveryException ex) {
            s_logger.warn("Exception: ", ex);
            throw new ServerApiException(ApiErrorCode.INTERNAL_ERROR, ex.getMessage());
        }
    }
}
