package com.doc.bpm.consultation;

import static org.camunda.spin.Spin.JSON;
import static org.camunda.spin.Spin.XML;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringEscapeUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.xml.SpinXmlElement;

import org.camunda.spin.json.SpinJsonNode;

public class MergeContributionDelegate implements JavaDelegate {
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	private final String mergePayloadTemplate = "{\"contribution\": \"%s\", \"answers\": %s}";
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		SpinJsonNode answers = (SpinJsonNode) execution.getVariable("answers");
		SpinXmlElement contribution = (SpinXmlElement) execution.getVariable("contribution");
		String escapedContribution = StringEscapeUtils.escapeJson(contribution.toString());
		
		String mergePayload =  String.format(mergePayloadTemplate, escapedContribution, answers.toString());

		HttpConnector http = Connectors.getConnector(HttpConnector.ID);
		HttpResponse response = http.createRequest()
				.post()
				.url("http://3.89.218.153:6969/merge/contribution")
				.header("Content-Type", "application/json")
				.header("Accept", "*/*")
				.payload(mergePayload)
				.execute();

		// Create XML element
		execution.setVariable("mergeResponse", Variables.stringValue(response.getResponse(), true));

	}

}
