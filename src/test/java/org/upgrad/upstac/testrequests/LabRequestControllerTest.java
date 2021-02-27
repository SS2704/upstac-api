package org.upgrad.upstac.testrequests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.lab.CreateLabResult;
import org.upgrad.upstac.testrequests.lab.LabRequestController;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.testrequests.lab.TestStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Slf4j
class LabRequestControllerTest {

    @Autowired
    LabRequestController labRequestController;

    @Autowired
    TestRequestQueryService testRequestQueryService;


    @Test
    @WithUserDetails(value = "tester02")
    public void calling_assignForLabTest_with_valid_test_request_id_should_update_the_request_status(){

        //Implement this method

        //Create another object of the TestRequest method and explicitly assign this object for Lab Test using assignForLabTest() method
        // from labRequestController class. Pass the request id of testRequest object.

        //Use assertThat() methods to perform the following two comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'INITIATED'
        // make use of assertNotNull() method to make sure that the lab result of second object is not null
        // use getLabResult() method to get the lab result

        //Arrange
        TestRequest testRequest1 = getTestRequestByStatus(RequestStatus.INITIATED);
        TestRequest testRequest2 = getTestRequestByStatus(RequestStatus.INITIATED);

        //Act
        TestRequest testRequest3 = labRequestController.assignForLabTest(testRequest2.getRequestId());

        //Assert
        assertThat("Comparing Request Id for 2 TestRequests", testRequest1.getRequestId() == testRequest2.getRequestId());
        assertThat("Checking Request Status of TestRequest2 ", testRequest2.getStatus()==RequestStatus.INITIATED);

        assertNotNull(testRequest3 != null);
    }

    public TestRequest getTestRequestByStatus(RequestStatus status) {
        return testRequestQueryService.findBy(status).stream().findFirst().get();
    }

    @Test
    @WithUserDetails(value = "tester02")
    public void calling_assignForLabTest_with_valid_test_request_id_should_throw_exception(){

        Long InvalidRequestId= -34L;

        //Implement this method


        // Create an object of ResponseStatusException . Use assertThrows() method and pass assignForLabTest() method
        // of labRequestController with InvalidRequestId as Id

        AppException responseStatusException = assertThrows(AppException.class, ()->{
            labRequestController.assignForLabTest(InvalidRequestId);
        });
        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"

        assertThat(responseStatusException.getMessage(), containsString("Invalid ID"));
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_valid_test_request_id_should_update_the_request_status_and_update_test_request_details(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method
        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

        CreateLabResult createLabResult = getCreateLabResult(testRequest);

        //Create another object of the TestRequest method and explicitly update the status of this object
        // to be 'LAB_TEST_IN_PROGRESS'. Make use of updateLabTest() method from labRequestController class (Pass the previously created two objects as parameters)

        TestRequest testRequest1 = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);
        TestRequest testRequest2 = labRequestController.updateLabTest(testRequest1.getRequestId(), createLabResult);


        //Use assertThat() methods to perform the following three comparisons
        //  1. the request ids of both the objects created should be same
        //  2. the status of the second object should be equal to 'LAB_TEST_COMPLETED'
        // 3. the results of both the objects created should be same. Make use of getLabResult() method to get the results.

        assertThat("Comparison of Request ID's of the two TestRequest objects",testRequest.getRequestId() == testRequest2.getRequestId());
        assertThat("Check STATUS of second LabTestRequest", testRequest2.getStatus() == RequestStatus.LAB_TEST_COMPLETED);
        assertThat("Check LabTestResults of both TestResults are same", testRequest.getLabResult() == testRequest2.getLabResult());

    }


    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_test_request_id_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);


        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter

        CreateLabResult createLabResult = getCreateLabResult(testRequest);

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with a negative long value as Id and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()->{
            labRequestController.updateLabTest(-1L, createLabResult);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "Invalid ID"

        assertThat(responseStatusException.getMessage(), containsString("Invalid ID"));
    }

    @Test
    @WithUserDetails(value = "tester")
    public void calling_updateLabTest_with_invalid_empty_status_should_throw_exception(){

        TestRequest testRequest = getTestRequestByStatus(RequestStatus.LAB_TEST_IN_PROGRESS);

        //Implement this method

        //Create an object of CreateLabResult and call getCreateLabResult() to create the object. Pass the above created object as the parameter
        // Set the result of the above created object to null.
        CreateLabResult createLabResult = getCreateLabResult(testRequest);

        // Create an object of ResponseStatusException . Use assertThrows() method and pass updateLabTest() method
        // of labRequestController with request Id of the testRequest object and the above created object as second parameter
        //Refer to the TestRequestControllerTest to check how to use assertThrows() method

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, ()->{
            labRequestController.updateLabTest(testRequest.getRequestId(), createLabResult);
        });

        //Use assertThat() method to perform the following comparison
        //  the exception message should be contain the string "ConstraintViolationException"
        assertThat(responseStatusException.getMessage(), containsString("ConstraintViolationException"));
    }

    public CreateLabResult getCreateLabResult(TestRequest testRequest) {

        //Create an object of CreateLabResult and set all the values
        // Return the object

        CreateLabResult createLabResult = new CreateLabResult();
        createLabResult.setBloodPressure("90");
        createLabResult.setHeartBeat("90");
        createLabResult.setOxygenLevel("90");
        createLabResult.setTemperature("90");
        createLabResult.setComments("Negative");
        createLabResult.setResult(TestStatus.NEGATIVE);

        LabResult labResult = new LabResult();
        labResult.setBloodPressure("90");
        labResult.setHeartBeat("90");
        labResult.setOxygenLevel("90");
        labResult.setTemperature("90");
        labResult.setComments("Negative");
        labResult.setResult(TestStatus.NEGATIVE);

        testRequest.setLabResult(labResult);

        return createLabResult; // Replace this line with your code
    }

}