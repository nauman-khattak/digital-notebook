package com.digital.notebook;

import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * Client class of the Digital
 */
public class DigitalNoteBookClient {
	/**
	 * Main method of the application
	 * 
	 * @param args
	 *            - Array of command line
	 */
	public static void main(String[] args) {
		DigitalNoteBookClient client = new DigitalNoteBookClient();
		client.setLookAndFeel();
		DigitalNotebookFrame dnf = new DigitalNotebookFrame();
		dnf.setLocationRelativeTo(null);
		dnf.setVisible(true);
	}

	// Set look and feel
	public void setLookAndFeel() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			getLogger(DigitalNotebookFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
