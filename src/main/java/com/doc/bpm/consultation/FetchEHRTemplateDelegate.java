package com.doc.bpm.consultation;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpResponse;
import static org.camunda.spin.Spin.XML;
import org.camunda.spin.xml.SpinXmlElement;

public class FetchEHRTemplateDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String token = (String) execution.getVariable("token");
		String templateId = (String) execution.getVariable("templateId");

		HttpConnector http = Connectors.getConnector(HttpConnector.ID);
		HttpResponse response = http.createRequest().get()
		.url("http://3.89.210.6:8090/ehr/api/v1/templates/" + templateId)
		.header("Authorization", "Bearer "+token)
		.header("Accept", "*/*")
		.execute();
		
		// Create XML element
		SpinXmlElement element = XML(response.getResponse());
		execution.setVariable("template", element);

	}

}
