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

package com.mnubo.platform.android.sdk.api.operations.impl;

import com.mnubo.platform.android.sdk.api.operations.AbstractOperationsTest;
import com.mnubo.platform.android.sdk.api.services.cache.impl.MnuboSmartObjectFileCachingServiceImpl;
import com.mnubo.platform.android.sdk.internal.services.SmartObjectService;
import com.mnubo.platform.android.sdk.internal.tasks.MnuboResponse;
import com.mnubo.platform.android.sdk.internal.tasks.Task;
import com.mnubo.platform.android.sdk.internal.tasks.TaskFactory;
import com.mnubo.platform.android.sdk.internal.tasks.impl.TaskWithRefreshImpl;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSampleOnPublicSensorTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.AddSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.CreateObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.FindObjectTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.SearchSamplesTask;
import com.mnubo.platform.android.sdk.internal.tasks.impl.smartobjects.UpdateObjectTask;
import com.mnubo.platform.android.sdk.models.common.IdType;
import com.mnubo.platform.android.sdk.models.common.SdkId;
import com.mnubo.platform.android.sdk.models.smartobjects.SmartObject;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Sample;
import com.mnubo.platform.android.sdk.models.smartobjects.samples.Samples;

import org.junit.Before;
import org.junit.Test;

import static com.mnubo.platform.android.sdk.api.MnuboApi.CompletionCallBack;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@SuppressWarnings("unchecked")
public class SmartObjectOperationsImplTest extends AbstractOperationsTest {

    private final SmartObjectService mockedSmartObjectService = mock(SmartObjectService.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<SmartObject> mockedSmartObjectCallback = mock(CompletionCallBack.class);

    @SuppressWarnings("unchecked")
    private final CompletionCallBack<Samples> mockedSamplesCallback = mock(CompletionCallBack.class);


    private final MnuboSmartObjectFileCachingServiceImpl mockedCacheService = mock(MnuboSmartObjectFileCachingServiceImpl.class);


    private SmartObjectOperationsImpl smartObjectOperations;


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(mockedUserApiConnection.getApi()).thenReturn(mockedUserApi);
        when(mockedUserApi.objectService()).thenReturn(mockedSmartObjectService);

        smartObjectOperations = new SmartObjectOperationsImpl(mockedConnectionOperations, mockedClientApiConnection,
                mockedUserApiConnection, null, false);
        smartObjectOperations.setMnuboSmartObjectFileCachingService(mockedCacheService);
    }

    @Test
    public void testSyncFindObject() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);

        final SmartObject expectedResult = new SmartObject();
        final FindObjectTask mockedTask = mock(FindObjectTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(expectedResult, null));
        when(TaskFactory.newFindObjectTask(any(Task.ApiFetcher.class), eq(objectId), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        final SmartObject result = smartObjectOperations.findObject(objectId).getResult();

        assertEquals(expectedResult, result);
        verify(mockedTask, only()).executeSync();


    }

    @Test
    public void testAsyncFindObject() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);

        final FindObjectTask mockedTask = mock(FindObjectTask.class);
        when(TaskFactory.newFindObjectTask(any(Task.ApiFetcher.class), eq(objectId), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.findObjectAsync(objectId, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));
    }

    @Test
    public void testAsyncFindObjectWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);

        final FindObjectTask mockedTask = mock(FindObjectTask.class);
        when(TaskFactory.newFindObjectTask(any(Task.ApiFetcher.class), eq(objectId), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.findObjectAsync(objectId, mockedSmartObjectCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSmartObjectCallback));
    }

    @Test
    public void testSyncUpdate() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final SmartObject updatedObject = new SmartObject();

        final UpdateObjectTask mockedTask = mock(UpdateObjectTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newUpdateObjectTask(any(Task.ApiFetcher.class), eq(objectId), eq(updatedObject), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        final Boolean result = smartObjectOperations.update(objectId, updatedObject).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();


    }

    @Test
    public void testAsyncUpdate() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final SmartObject updatedObject = new SmartObject();

        final UpdateObjectTask mockedTask = mock(UpdateObjectTask.class);
        when(TaskFactory.newUpdateObjectTask(any(Task.ApiFetcher.class), eq(objectId), eq(updatedObject), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.updateAsync(objectId, updatedObject, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncUpdateWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final SmartObject updatedObject = new SmartObject();

        final UpdateObjectTask mockedTask = mock(UpdateObjectTask.class);
        when(TaskFactory.newUpdateObjectTask(any(Task.ApiFetcher.class), eq(objectId), eq(updatedObject), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.updateAsync(objectId, updatedObject, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }

    @Test
    public void testSyncSearchSamples() throws Exception {

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";

        final Samples expectedResult = new Samples();
        final SearchSamplesTask mockedTask = mock(SearchSamplesTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(expectedResult, null));
        when(TaskFactory.newSearchSamplesTask(any(Task.ApiFetcher.class), eq(objectId), eq(sensorName), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);


        Samples result = smartObjectOperations.searchSamples(objectId, sensorName).getResult();

        assertEquals(expectedResult, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncSearchSamples() throws Exception {

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";

        final SearchSamplesTask mockedTask = mock(SearchSamplesTask.class);
        when(TaskFactory.newSearchSamplesTask(any(Task.ApiFetcher.class), eq(objectId), eq(sensorName), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.searchSamplesAsync(objectId, sensorName, null);
        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));


    }

    @Test
    public void testAsyncSearchSamplesWithCallback() throws Exception {

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";

        final SearchSamplesTask mockedTask = mock(SearchSamplesTask.class);
        when(TaskFactory.newSearchSamplesTask(any(Task.ApiFetcher.class), eq(objectId), eq(sensorName), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);
        smartObjectOperations.searchSamplesAsync(objectId, sensorName, mockedSamplesCallback);
        verify(mockedTask, only()).executeAsync(eq(mockedSamplesCallback));


    }

    @Test
    public void testSyncAddSamples() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final Samples samples = new Samples();

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newAddSamplesTask(any(Task.ApiFetcher.class), eq(objectId), eq(samples), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);


        Boolean result = smartObjectOperations.addSamples(objectId, samples).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncAddSamples() throws Exception {

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final Samples samples = new Samples();

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(TaskFactory.newAddSamplesTask(any(Task.ApiFetcher.class), eq(objectId), eq(samples), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.addSamplesAsync(objectId, samples, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncAddSamplesWithCallback() throws Exception {

        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final Samples samples = new Samples();

        final AddSamplesTask mockedTask = mock(AddSamplesTask.class);
        when(TaskFactory.newAddSamplesTask(any(Task.ApiFetcher.class), eq(objectId), eq(samples), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.addSamplesAsync(objectId, samples, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));


    }

    @Test
    public void testSyncAddSampleOnPublicSensor() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final Sample sample = new Sample();
        final String sensorName = "sensorName";

        final AddSampleOnPublicSensorTask mockedTask = mock(AddSampleOnPublicSensorTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newAddSamplesOnPublicSensor(any(Task.ApiFetcher.class), eq(objectId), eq(sensorName), eq(sample), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);


        Boolean result = smartObjectOperations.addSampleOnPublicSensor(objectId, sensorName, sample).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();
    }

    @Test
    public void testAsyncAddSampleOnPublicSensor() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";
        final Sample sample = new Sample();

        final AddSampleOnPublicSensorTask mockedTask = mock(AddSampleOnPublicSensorTask.class);
        when(TaskFactory.newAddSamplesOnPublicSensor(any(Task.ApiFetcher.class), eq(objectId), eq(sensorName), eq(sample), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.addSampleOnPublicSensorAsync(objectId, sensorName, sample, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));
    }

    @Test
    public void testAsyncAddSampleOnPublicSensorWithCallback() throws Exception {
        final SdkId objectId = SdkId.build("object-id", IdType.deviceid);
        final String sensorName = "sensorName";
        final Sample sample = new Sample();

        final AddSampleOnPublicSensorTask mockedTask = mock(AddSampleOnPublicSensorTask.class);
        when(TaskFactory.newAddSamplesOnPublicSensor(any(Task.ApiFetcher.class), eq(objectId), eq(sensorName), eq(sample), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.addSampleOnPublicSensorAsync(objectId, sensorName, sample, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }

    @Test
    public void testSyncCreateObject() throws Exception {
        final SmartObject object = new SmartObject();


        final CreateObjectTask mockedTask = mock(CreateObjectTask.class);
        when(mockedTask.executeSync()).thenReturn(new MnuboResponse<>(true, null));
        when(TaskFactory.newCreateObjectTask(any(Task.ApiFetcher.class), eq(object), eq(true), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);


        Boolean result = smartObjectOperations.createObject(object, true).getResult();

        assertEquals(true, result);
        verify(mockedTask, only()).executeSync();

    }

    @Test
    public void testAsyncCreateObject() throws Exception {
        final SmartObject object = new SmartObject();

        final CreateObjectTask mockedTask = mock(CreateObjectTask.class);
        when(TaskFactory.newCreateObjectTask(any(Task.ApiFetcher.class), eq(object), eq(true), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.createObjectAsync(object, true, null);

        verify(mockedTask, only()).executeAsync(isNull(CompletionCallBack.class));

    }

    @Test
    public void testAsyncCreateObjectWithCallback() throws Exception {

        final SmartObject object = new SmartObject();

        final CreateObjectTask mockedTask = mock(CreateObjectTask.class);
        when(TaskFactory.newCreateObjectTask(any(Task.ApiFetcher.class), eq(object), eq(true), any(TaskWithRefreshImpl.ConnectionRefresher.class))).thenReturn(mockedTask);

        smartObjectOperations.createObjectAsync(object, true, mockedSuccessCallback);

        verify(mockedTask, only()).executeAsync(eq(mockedSuccessCallback));

    }
}