package com.doc.bpm.consultation;

import static org.camunda.spin.Spin.JSON;
import static org.camunda.spin.Spin.XML;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.xml.SpinXmlElement;

public class ConvertTemplateToHTMLDelegate implements JavaDelegate {

	private final String htmlFormFormat = "{\"html\": %s}";
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		String template = (String) execution.getVariable("template").toString();

		HttpConnector http = Connectors.getConnector(HttpConnector.ID);
		HttpResponse response = http.createRequest().post().url("http://3.89.218.153:6969/opt2bundle")
				.header("Content-Type", "application/xml").header("Accept", "*/*").payload(template).execute();

		SpinJsonNode jsonResponse = JSON(response.getResponse());
		SpinJsonNode htmlJson = JSON(String.format(htmlFormFormat, jsonResponse.jsonPath("$.data.html").element()));
		SpinXmlElement xmlContribution = XML(jsonResponse.jsonPath("$.data.contribution").stringValue());

		execution.setVariable("htmlForm", htmlJson);
		execution.setVariable("contribution", xmlContribution);

	}

}
