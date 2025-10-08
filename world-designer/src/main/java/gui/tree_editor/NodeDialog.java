package gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;

import javax.swing.JDialog;
import javax.swing.JFrame;

import gui.tree_editor.ProjectNode;

public class NodeDialog extends JDialog
{
	private ProjectNode node;
	
	public NodeDialog(JFrame frm, String title) {
		super(frm, title, ModalityType.APPLICATION_MODAL);
		
		Container cPane = this.getContentPane();
		cPane.setLayout(new BorderLayout());
		
//		ReminderField name = new ReminderField("New " + (()));
	}
}