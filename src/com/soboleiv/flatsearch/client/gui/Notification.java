package com.soboleiv.flatsearch.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Notification {
	final Label textToServerLabel = new Label();
	final HTML serverResponseLabel = new HTML();
	final DialogBox dialogBox = new DialogBox();
	final Button closeButton = new Button("Close");
	{
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				// searchButton.setEnabled(true);
				// searchButton.setFocus(true);
			}
		});
	}

	public void show(String caption, String toServer, String fromServer) {
		dialogBox.setText(caption);
		textToServerLabel.setText(toServer);
		serverResponseLabel.setHTML(fromServer);
		dialogBox.center();
		closeButton.setFocus(true);
	}
	
	public void handleFailure(Throwable exception) {
		serverResponseLabel.addStyleName("serverResponseLabelError");
		dialogBox.center();
		closeButton.setFocus(true);
	}
}
