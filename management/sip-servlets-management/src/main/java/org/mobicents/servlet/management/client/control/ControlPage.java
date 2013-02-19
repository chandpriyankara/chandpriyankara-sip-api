/*
 * TeleStax, Open Source Cloud Communications.
 * Copyright 2012 and individual contributors by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.servlet.management.client.control;

import org.mobicents.servlet.management.client.router.Console;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;

public class ControlPage extends Panel {
	
	TextField timeToWait;
	
	
	private void addLabeledControl(String label, Widget component, Panel panel) {
		Panel regionLabel = new Panel();
		regionLabel.setPaddings(0, 0, 0, 1);
		regionLabel.setBorder(false);
		regionLabel.setHtml(label);
		panel.add(regionLabel);
		panel.add(component);
	}
	
	public ControlPage() {
		final FormPanel formPanel = new FormPanel();  

//		formPanel.setTitle("Concurrency and Congestion Control");  

		formPanel.setWidth(900);  
		formPanel.setFrame(true); 
		formPanel.setLabelWidth(75);

		timeToWait = new TextField();  
		timeToWait.setAllowBlank(false); 
		timeToWait.setHideLabel(true);
		addLabeledControl("Time To Wait:", timeToWait, formPanel);
		
		//Save button
		Button save = new Button("Stop Gracefully", new ButtonListenerAdapter(){

			public void onClick(Button button, EventObject e) {
				ControlService.Util.getInstance().stopGracefully(
						Long.parseLong(timeToWait.getValueAsString()), new AsyncCallback<Void>() {

							public void onFailure(Throwable caught) {
								Console.error("Error while trying to stop the Server Gracefully.");
							}

							public void onSuccess(Void result) {
								result = result;
							}
							
						});
			}
			
		});
		
		formPanel.add(save);

		add(formPanel);
		
		DeferredCommand.addCommand(new Command() {

			public void execute() {
				
			}
		});
	}

}
