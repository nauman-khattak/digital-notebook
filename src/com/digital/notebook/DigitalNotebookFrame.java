
package com.digital.notebook;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * Main window of the application
 */
public class DigitalNotebookFrame extends JFrame {
	private static final long serialVersionUID = -1926038697567190087L;
	private JMenuItem createNotebook;
	private JMenuItem exitMenuItem;
	private JPanel mainpanel;
	private JMenu menu;
	private JMenuBar menubar;
	private JTree notebookTree;
	private JTextArea pagecontent;
	private JScrollPane pagecontentscrollpane;
	private JPopupMenu.Separator s1;
	private JButton saveBtn;
	private JScrollPane treescrollpane;
	private File selectedFile = null;

	/**
	 * Creates new form DigitalNotebookFrame
	 */
	public DigitalNotebookFrame() {
		initComponents();
	}

	/**
	 * Initialize ui component
	 */
	private void initComponents() {

		mainpanel = new JPanel();
		treescrollpane = new JScrollPane();
		notebookTree = new JTree();
		saveBtn = new JButton();
		pagecontentscrollpane = new JScrollPane();
		pagecontent = new JTextArea();
		menubar = new JMenuBar();
		menu = new JMenu();
		createNotebook = new JMenuItem();
		s1 = new JPopupMenu.Separator();
		exitMenuItem = new JMenuItem();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Digital Notebook");
		setMinimumSize(new Dimension(747, 497));
		setPreferredSize(new Dimension(747, 475));
		setResizable(false);
		getContentPane().setLayout(null);

		mainpanel.setMinimumSize(new Dimension(747, 423));
		mainpanel.setPreferredSize(new Dimension(747, 423));
		mainpanel.setLayout(null);

		notebookTree.setModel(populateNoteBookTreeModel());
		notebookTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent evt) {
				notebookTreeValueChanged(evt);
			}
		});
		treescrollpane.setViewportView(notebookTree);

		mainpanel.add(treescrollpane);
		treescrollpane.setBounds(0, 0, 140, 370);

		saveBtn.setText("Save");
		saveBtn.setToolTipText("");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				saveBtnActionPerformed(evt);
			}
		});
		mainpanel.add(saveBtn);
		saveBtn.setBounds(330, 380, 100, 29);

		pagecontent.setColumns(20);
		pagecontent.setRows(5);
		pagecontentscrollpane.setViewportView(pagecontent);

		mainpanel.add(pagecontentscrollpane);
		pagecontentscrollpane.setBounds(140, 30, 600, 340);

		getContentPane().add(mainpanel);
		mainpanel.setBounds(0, 0, 747, 423);

		menubar.setPreferredSize(new Dimension(35, 30));

		menu.setText("Digital Notebook");

		createNotebook.setText("Create NoteBook");
		createNotebook.setActionCommand("new");
		createNotebook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				createNotebookActionPerformed(evt);
			}
		});
		menu.add(createNotebook);
		menu.add(s1);

		exitMenuItem.setText("Exit");
		exitMenuItem.setActionCommand("exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		menu.add(exitMenuItem);

		menubar.add(menu);

		setJMenuBar(menubar);

		pack();
	}
	//Exit menu action handler
	private void exitMenuItemActionPerformed(ActionEvent evt) {
		this.dispose();
	}
	//create new notebook menu action handler
	private void createNotebookActionPerformed(ActionEvent evt) {
		NewNoteBookDialog dialog = new NewNoteBookDialog(this, true);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}
	//Tree page node selection handler
	private void notebookTreeValueChanged(TreeSelectionEvent evt) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) notebookTree.getLastSelectedPathComponent();

		if (node != null) {
			if (node.getChildCount() == 0) {
				File t_file = new File(node.toString());
				if (!t_file.isDirectory()) {
					BufferedReader br = null;
					try {
						String notebook = node.getParent().toString();
						File newNoteBooks = new File("notebooks");
						File newNoteBook = new File(newNoteBooks, notebook);
						selectedFile = new File(newNoteBook, node.toString() + ".txt");

						br = new BufferedReader(new FileReader(selectedFile));
						StringBuilder sb = new StringBuilder();
						String line = null;
						while (null != (line = br.readLine())) {
							sb.append(line);
							sb.append("\n");
						}
						pagecontent.setLineWrap(true);
						pagecontent.setText(sb.toString());
					} catch (FileNotFoundException ex) {
						JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} finally {
						if (null != br) {
							try {
								br.close();
							} catch (IOException ex) {
								Logger.getLogger(DigitalNotebookFrame.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}

				}
			}
		}
	}
	//Save page content
	private void saveBtnActionPerformed(ActionEvent evt) {

		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(selectedFile));
			bw.write(pagecontent.getText());
			JOptionPane.showMessageDialog(this, "Text added to page", "Success", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
	}
	//Opoulate tree
	private TreeModel populateNoteBookTreeModel() {

		File newNoteBooks = new File("notebooks");
		if (!newNoteBooks.exists()) {
			newNoteBooks.mkdir();
		}
		File[] dns = newNoteBooks.listFiles();
		if (null != dns && dns.length > 0) {
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Notebooks");

			for (File dn : dns) {
				if (null != dn && dn.isDirectory()) {
					DefaultMutableTreeNode notebooknode = new DefaultMutableTreeNode(dn.getName());

					File[] pages = dn.listFiles();

					Arrays.sort(pages, new Comparator<File>() {
						@Override
						public int compare(File o1, File o2) {
							return o1.getName().compareTo(o2.getName());
						}
					});
					for (File page : pages) {
						if (null != page && page.isFile()) {
							String pageNo = page.getName().replace(".txt", "");
							DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode(pageNo, false);
							notebooknode.add(pageNode);

						}
					}
					root.add(notebooknode);
				}
			}

			DefaultTreeModel model = new DefaultTreeModel(root, true);
			return model;
		}
		return null;
	}
	//Update tree on adding new notebook
	public void updateTree() {
		notebookTree.setModel(populateNoteBookTreeModel());
		notebookTree.validate();
		notebookTree.repaint();
	}
}
