package com.soboleiv.flatsearch.client;

 import java.util.Collection;
import java.util.Date;

import org.mortbay.log.Log;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.soboleiv.flatsearch.client.admin.AdminService;
import com.soboleiv.flatsearch.client.admin.AdminServiceAsync;
import com.soboleiv.flatsearch.client.gui.Notification;
import com.soboleiv.flatsearch.shared.AdminResponse;		
import com.soboleiv.flatsearch.shared.FieldVerifier;
import com.soboleiv.flatsearch.shared.Interval;
import com.soboleiv.flatsearch.shared.Location;
import com.soboleiv.flatsearch.shared.Place;
import com.soboleiv.flatsearch.shared.SearchRequest;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Flatsearch implements EntryPoint {

	public void onModuleLoad() {
		/*
		 * Asynchronously loads the Maps API.
		 * 
		 * The first parameter should be a valid Maps API Key to deploy this
		 * application on a public server, but a blank key will work for an
		 * application served from localhost.
		 */
		Maps.loadMapsApi("notsupplied", "2", false, new Runnable() {
			public void run() {
				buildUi();
			}
		});
	}

	MapWidget map;

	private void buildUi() {
		MapWidget map = new MapWidget();
		map.setSize("100%", "100%");
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		// Add the map to the HTML host page
		RootPanel.get("mapsTutorial").add(map);

		hahaha();
	}

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final SearchServiceAsync greetingService = GWT.create(SearchService.class);
	private final AdminServiceAsync adminService = GWT.create(AdminService.class);

	final Notification notification = new Notification();

	/**
	 * This is the entry point method.
	 */
	private void hahaha() {		
		final Button searchButton = new Button("Search");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();
		
		// We can add style names to widgets
		searchButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(searchButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				// sendButton.setEnabled(false);
				SearchRequest request = new SearchRequest();
				Date twoMinsAgo = new Date(new Date().getTime() - 1000*60*2);
				request.setFetchTime(Interval.after(twoMinsAgo));
				greetingService.greetServer(request,
						new AsyncCallback<Collection<Place>>() {
							public void onFailure(Throwable caught) {
								notification.handleFailure(caught);
							}

							public void onSuccess(Collection<Place> result) {
								// TODO: sign of utter stupidity
								final MapWidget map = (MapWidget) RootPanel
										.get("mapsTutorial").getWidget(0);
								map.clearOverlays();

								LatLng markerPos = null;
								for (final Place place : result) {
									Location coordinates = place.getCoordinates();
									markerPos = LatLng.newInstance(
											coordinates.getLatitude(), coordinates.getLongitude());

									// Add a marker
									MarkerOptions options = MarkerOptions
											.newInstance();
									options.setTitle(place.getAddress());
									Marker marker = new Marker(markerPos,
											options);
									final LatLng currMarkerPos = markerPos;
									marker.addMarkerClickHandler(new MarkerClickHandler() {

										public void onClick(
												MarkerClickEvent event) {
											PlaceFormatter places = new PlaceFormatter();
											InfoWindowContent wnd = new InfoWindowContent(places.format(place));
											wnd.setMaxWidth(200);
											map.getInfoWindow().open(
													currMarkerPos,
													wnd);
										}
									});

									map.addOverlay(marker);
								}

								if (markerPos != null) {
									map.setCenter(markerPos);
									map.setZoomLevel(12);
								}
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		searchButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
		
		final Button startFetching = new Button("Fetch");
		RootPanel.get("startFetchingContainer").add(startFetching);
		startFetching.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				adminService.checkDataSources(new AsyncCallback<AdminResponse>() {
					public void onFailure(Throwable caught) {
						notification.handleFailure(caught);						
					}
					
					public void onSuccess(AdminResponse result) {
						notification.show("Coolio", "Everything is cool", "");
					};
				});
			}
		});
	}
}
