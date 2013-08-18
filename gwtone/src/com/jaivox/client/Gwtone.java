package com.jaivox.client;

// import com.jaivox.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Gwtone implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create (GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad () {
		final Button sendButton = new Button ("Send");
		final TextBox nameField = new TextBox ();
		nameField.setText ("App Name");
		final Label errorLabel = new Label ();
		
		// tj: add a new panel and a tree control within that
		SimplePanel dlgPanel = new SimplePanel ();
		TreeItem dlg1 = new TreeItem ();
		dlg1.setText ("WP NN JJ-P");
		dlg1.addTextItem ("is NNP JJP?");
		dlg1.addTextItem ("are the NN JJP?");
		TreeItem dlg2 = new TreeItem ();
		dlg2.setText ("are NNS JJ-N");
		dlg2.addTextItem ("do the NNS get JJ-N at this time");
		dlg2.addTextItem ("do the NNS get JJ-N");
		dlg2.addTextItem ("do you think the NNS are JJ-N");
		TreeItem dlg3 = new TreeItem ();
		dlg3.setText ("is NNP DT NN");
		dlg3.addTextItem ("is NNP JJR-P");
		dlg3.addTextItem ("is NNP JJR-N");
		dlg2.addItem (dlg3);
		Tree dlgTree = new Tree ();
		dlgTree.addItem (dlg1);
		dlgTree.addItem (dlg2);
		dlgPanel.add (dlgTree);
/*
do the NNS get JJ-N at this time	do the roads get busy at this time?
 do the NNS get JJ-N at this time	do the roads get congested at this time?
 do the NNS get JJ-N	do the roads get congested?
 are NNS JJ-N	are roads congested?
 are NNS JJ-N at this time	are roads busy at this time?
 do you think the NNS are JJ-N	do you think the roads are congested?
 do you think NNP is JJ-N	do you think Brigham Road is congested?
 do you think NNP is JJ-P	do you think Brigham road is fast?
 is NNP a JJ-P NN	is Adams Street an alternate route?
 is NNP the JJS-P NN	is Adams Street the best option?
 are the NNS JJ-N	are the highways slow?
 are the NNS RBS	are the highways very slow?
 is NNP DT NN	is Adams Street a highway?
 is NNP JJR-P	is Adams Street faster?
 is NNP JJR-N	is Adams Stree slower?
 WP NN JJ-P	what road is fast?
 WP NNS JJ-P	what roads are fast?
 WP is a JJ-P NN	what is a fast road?
 WP are the JJ-P NNS	what are the fast roads?
 are there any JJ-P NNS are there any fast roads?
 which NN is JJR-P than NNP	which road is faster than McLauren Highway?
 which NN is JJR-N than NNP	which highway is slower than Kenworth?
 which NN is JJ-P	which road is easiest?

 */
		
		SimplePanel gramPanel = new SimplePanel ();
		// grammar
	    ListBox lb = new ListBox ();
	    lb.addItem ("do the NNS get JJ-N");
	    lb.addItem ("are NNS JJ-N");
	    lb.addItem ("are the NNS RBS");
	    lb.addItem ("is NNP a JJ-P NN");
	    lb.addItem ("is NNP the JJS-P NN");
	    lb.addItem ("is NNP JJR-P");
	    lb.addItem ("is NNP DT NN");
	    lb.addItem ("WP NN JJ-P");
	    lb.addItem ("WP NNS JJ-P");
	    lb.addItem ("WP is a JJ-P NN");
	    lb.addItem ("WP are the JJ-P NNS");
	    lb.addItem ("are there any JJ-P NNS");
	    lb.addItem ("which NN is JJR-P than NNP");
	    lb.addItem ("which NN is JJR-N than NNP");
	    lb.addItem ("which NN is JJ-P");
	    lb.setVisibleItemCount (10);
	    gramPanel.add (lb);
		
	    /*
	    // nothing visible when using DockPanel 8/16/13
	    // DockPanel did not work
		DockLayoutPanel dictPanel = new DockLayoutPanel (Unit.PCT);
		// dictionary
		Grid dictGrid = new Grid (5, 3);
		for (int row=0; row<5; row++) {
			for (int col=0; col<3; col++) {
				String s = "Row "+row+" Col "+col;
				dictGrid.setText (row, col, s);
			}
		}
		dictGrid.getCellFormatter ().setWidth (0, 2, "50px");
		dictPanel.addNorth (dictGrid, 40);
		Grid doctGrid = new Grid (3, 6);
		for (int row=0; row<3; row++) {
			for (int col=0; col<6; col++) {
				String s = "Row "+row+" Col "+col;
				doctGrid.setText (row, col, s);
			}
		}
		doctGrid.getCellFormatter ().setWidth (0, 2, "20px");
		dictPanel.addSouth (doctGrid, 40);
		*/
	    
	    // LayoutPanel also did not work
	    // LayoutPanel dictPanel = new LayoutPanel ();
	    FlowPanel dictPanel = new FlowPanel ();
	    
		Grid dictGrid = new Grid (2,3);
		
		for (int row=0; row<2; row++) {
			for (int col=0; col<3; col++) {
				String s = "R"+row+"C"+col;
				dictGrid.setText (row, col, s);
				// dictGrid.setText (row, col, "x");
			}
		}
		// dictGrid.getCellFormatter ().setWidth (0, 2, "50px");
		// dictGrid.setVisible (true);
		dictPanel.add (dictGrid);
		
		Grid doctGrid = new Grid (3, 2);
		for (int row=0; row<3; row++) {
			for (int col=0; col<2; col++) {
				String s = "r"+row+"c"+col;
				doctGrid.setText (row, col, s);
				// doctGrid.setText (row, col, "y");
			}
		}
		// doctGrid.getCellFormatter ().setWidth (0, 2, "50px");
		// doctGrid.setVisible (true);
		dictPanel.add (doctGrid);
		
	    
	    FlowPanel dbPanel = new FlowPanel ();
		Grid dataGrid = new Grid (5, 3);
		for (int row=0; row<5; row++) {
			for (int col=0; col<3; col++) {
				String s = "Row "+row+" Col "+col;
				dataGrid.setText (row, col, s);
			}
		}
		dataGrid.getCellFormatter ().setWidth (0, 2, "250px");
		dataGrid.setVisible (true);
		dbPanel.add (dataGrid);
		
		Grid qualGrid = new Grid (3, 6);
		for (int row=0; row<3; row++) {
			for (int col=0; col<6; col++) {
				String s = "Row "+row+" Col "+col;
				qualGrid.setText (row, col, s);
			}
		}
		qualGrid.getCellFormatter ().setWidth (0, 2, "100px");
		qualGrid.setVisible (true);
		dbPanel.add (qualGrid);
		// setWidgetXY is only for LayoutPanel
		// dbPanel.setWidgetLeftWidth (dictGrid, 0, Unit.PCT, 50, Unit.PCT);  // Left panel
		// dbPanel.setWidgetRightWidth (doctGrid, 0, Unit.PCT, 50, Unit.PCT); // Right panel

	    
		// SimplePanel dbPanel = new SimplePanel ();
		// database

		SimplePanel interPanel = new SimplePanel ();
		// interpreter
		
		HorizontalPanel subinter = new HorizontalPanel ();
		
		CheckBox dl = new CheckBox("DataLink");
	    dl.setValue (true);		
		subinter.add (dl);
		
		CheckBox agents = new CheckBox ("Use agents");
		agents.setValue (false);
		subinter.add (agents);
		
		interPanel.add (subinter);
	    
	    
		SimplePanel platPanel = new SimplePanel ();
		// platform
		
	    // Create a panel to hold all of the form widgets.
	    VerticalPanel subplat = new VerticalPanel();
	    // form.setWidget(panel);

	    // Create a TextBox, giving it a name so that it will be submitted.
	    TextBox tb = new TextBox();
	    tb.setName ("Platform");
	    subplat.add (tb);

	    // Create a ListBox, giving it a name and some values to be associated with
	    // its options.
	    ListBox props = new ListBox();
	    props.setName ("Selections");
	    props.addItem ("mode", "Batch");
	    props.addItem ("asr", "Sphinx");
	    props.addItem ("tts", "Google");
	    subplat.add (props);

	    // Create a FileUpload widget.
	    FileUpload upload = new FileUpload ();
	    upload.setName ("SpecFile");
	    subplat.add (upload);
	    
	    platPanel.add (subplat);

		
		
		// We can add style names to widgets
		sendButton.addStyleName ("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get ("nameFieldContainer").add (nameField);
		RootPanel.get ("sendButtonContainer").add (sendButton);
		RootPanel.get ("errorLabelContainer").add (errorLabel);
		
		// tj: add panels
		RootPanel.get ("dialogPanelContainer").add (dlgPanel);
		RootPanel.get ("grammarPanelContainer").add (gramPanel);
		RootPanel.get ("dictionaryPanelContainer").add (dictPanel);
		RootPanel.get ("databasePanelContainer").add (dbPanel);
		// RootPanel.get ("databasePanelContainer").add (dictPanel);
		RootPanel.get ("interpreterPanelContainer").add (interPanel);
		RootPanel.get ("platformPanelContainer").add (platPanel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus (true);
		nameField.selectAll ();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox ();
		dialogBox.setText ("Remote Procedure Call");
		dialogBox.setAnimationEnabled (true);
		final Button closeButton = new Button ("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement ().setId ("closeButton");
		final Label textToServerLabel = new Label ();
		final HTML serverResponseLabel = new HTML ();
		VerticalPanel dialogVPanel = new VerticalPanel ();
		dialogVPanel.addStyleName ("dialogVPanel");
		dialogVPanel.add (new HTML ("<b>Sending name to the server:</b>"));
		dialogVPanel.add (textToServerLabel);
		dialogVPanel.add (new HTML ("<br><b>Server replies:</b>"));
		dialogVPanel.add (serverResponseLabel);
		dialogVPanel.setHorizontalAlignment (VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add (closeButton);
		dialogBox.setWidget (dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler (new ClickHandler () {
			public void onClick (ClickEvent event) {
				dialogBox.hide ();
				sendButton.setEnabled (true);
				sendButton.setFocus (true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick (ClickEvent event) {
				sendNameToServer ();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp (KeyUpEvent event) {
				if (event.getNativeKeyCode () == KeyCodes.KEY_ENTER) {
					sendNameToServer ();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer () {
				// First, we validate the input.
				errorLabel.setText ("");
				String textToServer = nameField.getText ();
				/*
				if (!FieldVerifier.isValidName (textToServer)) {
					errorLabel
							.setText ("Please enter at least four characters");
					return;
				}
				*/
				// Then, we send the input to the server.
				sendButton.setEnabled (false);
				textToServerLabel.setText (textToServer);
				serverResponseLabel.setText ("");
				greetingService.greetServer (textToServer,
						new AsyncCallback<String> () {
							public void onFailure (Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText ("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName ("serverResponseLabelError");
								serverResponseLabel.setHTML (SERVER_ERROR);
								dialogBox.center ();
								closeButton.setFocus (true);
							}

							public void onSuccess (String result) {
								dialogBox.setText ("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName ("serverResponseLabelError");
								serverResponseLabel.setHTML (result);
								dialogBox.center ();
								closeButton.setFocus (true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler ();
		sendButton.addClickHandler (handler);
		nameField.addKeyUpHandler (handler);
	}
}
