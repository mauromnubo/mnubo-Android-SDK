/*
 * Copyright (c) 2015 Mnubo. Released under MIT License.
 *
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 */

package com.mnubo.platform.android.sdk.internal.api;


import com.mnubo.platform.android.sdk.internal.connect.MnuboAPIErrorHandler;
import com.mnubo.platform.android.sdk.internal.services.ClientService;
import com.mnubo.platform.android.sdk.internal.services.SmartObjectService;
import com.mnubo.platform.android.sdk.internal.services.UserService;
import com.mnubo.platform.android.sdk.internal.services.impl.ClientServiceImpl;
import com.mnubo.platform.android.sdk.internal.services.impl.SmartObjectServiceImpl;
import com.mnubo.platform.android.sdk.internal.services.impl.UserServiceImpl;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.web.client.RestTemplate;

public class MnuboSDKApiImpl extends AbstractOAuth2ApiBinding implements MnuboSDKApi {

    private final ClientService clientService;
    private final UserService userService;
    private final SmartObjectService smartObjectService;

    public MnuboSDKApiImpl(final String accessToken, final String platformBaseUrl, String path) {
        super(accessToken);
        this.clientService = new ClientServiceImpl(platformBaseUrl, getRestTemplate(), path);
        this.userService = new UserServiceImpl(platformBaseUrl, getRestTemplate(), path);
        this.smartObjectService = new SmartObjectServiceImpl(platformBaseUrl, getRestTemplate(), path);

    }

    @Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new MnuboAPIErrorHandler());
        //Force the use of SNI to fetch the proper certificate
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
    }


    @Override
    public ClientService clientService() {
        return this.clientService;
    }


    @Override
    public UserService userService() {
        return this.userService;
    }

    @Override
    public SmartObjectService objectService() {
        return this.smartObjectService;
    }

}
