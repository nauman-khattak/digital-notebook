package com.digital.notebook;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * Dialog to create new book
 */
public class NewNoteBookDialog extends JDialog {

	private DigitalNotebookFrame dnf;
	private JPanel datapanel;
	private JPanel mainPanel;
	private JLabel namelabel;
	private JTextField notebookNameField;
	private JLabel pagecntlabel;
	private JFormattedTextField pagesField;
	private JButton saveBtn;

	/**
	 * Creates new form NewNoteBookDialog
	 */
	public NewNoteBookDialog(DigitalNotebookFrame parent, boolean modal) {
		super(parent, modal);
		dnf = parent;
		initComponents();
	}

	// Initialize ui component
	private void initComponents() {

		mainPanel = new JPanel();
		datapanel = new JPanel();
		namelabel = new JLabel();
		notebookNameField = new JTextField();
		pagecntlabel = new JLabel();
		pagesField = new JFormattedTextField();
		saveBtn = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("New NoteBook");
		setMinimumSize(new Dimension(459, 150));
		setPreferredSize(new Dimension(459, 150));
		setResizable(false);
		getContentPane().setLayout(null);

		mainPanel.setToolTipText("");
		mainPanel.setLayout(null);

		datapanel.setLayout(new GridLayout(2, 2, 5, 0));

		namelabel.setFont(new Font("Arial", 1, 14));
		namelabel.setText("NoteBook Name:");
		datapanel.add(namelabel);
		datapanel.add(notebookNameField);

		pagecntlabel.setFont(new Font("Arial", 1, 14));
		pagecntlabel.setText("Number of Pages:");
		datapanel.add(pagecntlabel);

		pagesField.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0"))));
		datapanel.add(pagesField);

		mainPanel.add(datapanel);
		datapanel.setBounds(0, 0, 460, 75);

		saveBtn.setText("Save");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveBtnActionPerformed(evt);
			}
		});
		mainPanel.add(saveBtn);
		saveBtn.setBounds(170, 90, 100, 29);

		getContentPane().add(mainPanel);
		mainPanel.setBounds(0, 0, 460, 200);

		pack();
	}

	// Save button action handler
	private void saveBtnActionPerformed(ActionEvent evt) {
		StringBuilder sb = new StringBuilder();
		String noteBookName = notebookNameField.getText();
		String numberOfPages = pagesField.getText();

		if (null == noteBookName || noteBookName.trim().isEmpty()) {
			sb.append("Invalid name of notebook");
		}

		int pages = 0;
		try {
			pages = Integer.parseInt(numberOfPages);
		} catch (NumberFormatException ex) {

		}
		if (pages <= 0) {
			sb.append("Invalid number of pages");
		}

		if (sb.toString().isEmpty()) {
			File newNoteBooks = new File("notebooks");
			if (!newNoteBooks.exists()) {
				newNoteBooks.mkdir();
			}
			File newNoteBook = new File("notebooks", noteBookName);
			if (newNoteBook.exists()) {
				JOptionPane.showMessageDialog(this, "Notebook is already exist", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				newNoteBook.mkdir();
				File coverpage = new File(newNoteBook, "Cover Page" + ".txt");
				BufferedWriter bw = null;
				try {
					bw = new BufferedWriter(new FileWriter(coverpage));
					bw.write("Digital Notebook");
				} catch (FileNotFoundException ex) {
				} catch (IOException ex) {
				} finally {
					if (null != bw) {
						try {
							bw.flush();
							bw.close();
						} catch (IOException ex) {
							Logger.getLogger(DigitalNotebookFrame.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
				}

				for (int i = 0; i < pages; i++) {
					File page = new File(newNoteBook, "page" + (i + 1) + ".txt");
					try {
						page.createNewFile();
					} catch (IOException ex) {
						Logger.getLogger(NewNoteBookDialog.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
				JOptionPane.showMessageDialog(this, "Notebook is created successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
				this.dnf.updateTree();
			}

		} else {
			JOptionPane.showMessageDialog(this, sb.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}
