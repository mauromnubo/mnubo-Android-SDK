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

package com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects;

import com.mnubo.platform.android.sdk.internal.connect.connection.MnuboConnectionManager;
import com.mnubo.platform.android.sdk.internal.connect.connection.refreshable.RefreshableConnection;
import com.mnubo.platform.android.sdk.internal.services.impl.SampleOrderResult;
import com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.common.ValueType;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import static com.mnubo.platform.android.sdk.internal.services.impl.SampleOrderResult.ASC;
import static com.mnubo.platform.android.sdk.models.common.ValueType.samples;

public class SearchSamplesTask extends TaskWithRefreshImpl<Samples> {

    private final SdkId objectId;
    private final String sensorName;
    private final String from;
    private final String to;
    private final int limit;
    private final SampleOrderResult order;


    public SearchSamplesTask( SdkId objectId, String sensorName) {

        this.objectId = objectId;
        this.sensorName = sensorName;
        this.limit = 0;
        this.order = ASC;
        this.from = null;
        this.to = null;

    }

    public SearchSamplesTask( SdkId objectId, String sensorName, int limit, SampleOrderResult order) {

        this.objectId = objectId;
        this.sensorName = sensorName;
        this.limit = limit;
        this.order = order;
        this.from = null;
        this.to = null;

    }

    public SearchSamplesTask( SdkId objectId, String sensorName, String from, String to, int limit,
                              SampleOrderResult order) {

        this.objectId = objectId;
        this.sensorName = sensorName;
        this.limit = limit;
        this.order = order;
        this.from = from;
        this.to = to;

    }

    @Override
    protected Samples executeMnuboCall(MnuboConnectionManager connectionManager) {
        return connectionManager.getCurrentConnection().getMnuboSDKApi().objectService()
                                .searchSamples(objectId, sensorName, samples, from, to, limit, order);
    }
}