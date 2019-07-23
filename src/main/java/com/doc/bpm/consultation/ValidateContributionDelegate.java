package com.doc.bpm.consultation;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.xml.SpinXmlElement;

import static org.camunda.spin.Spin.JSON;
import static org.camunda.spin.Spin.XML;

public class ValidateContributionDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		SpinJsonNode mergeResponse = JSON(execution.getVariable("mergeResponse"));
		String jsonContribution =  mergeResponse.jsonPath("$.data.contribution").stringValue();
		SpinXmlElement mergedContribution = XML(jsonContribution);
		
		HttpConnector http = Connectors.getConnector(HttpConnector.ID);
		HttpResponse response = http.createRequest()
				.post()
				.url("http://3.89.218.153:6969/validate/instance")
				.header("Content-Type", "application/xml")
				.header("Accept", "*/*")
				.payload(mergedContribution.toString())
				.execute();
		
		execution.setVariable("validateResponse", response.getResponse());
	}

}
