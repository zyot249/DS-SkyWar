package com.zyot.fung.shyn.ui;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.awt.Font;
import java.awt.Component;
import java.awt.Dimension;

public class GameOverDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton okButton;

	private List<JComponent> contentComponent;
	private OnDialogClose onCloseAction;

	public static void main(String[] args) {
		try {
//			JFrame frame = new JFrame();

			ArrayList<JComponent> components = new ArrayList<>(Arrays.asList(
//					new JLabel(String.format("_%-100s_ :    _%-7s_", "Player", "Score"))
//					, new JLabel(String.format("_%-100s_ :    _%-7s_", "Player", "Score"))
//					, new JLabel(String.format("_%-100s_ :    _%-7s_", "Player dqdqw", "35"))
//					, new JLabel(String.format("_%-100s_ :    _%-7s_", "Player cggrqqdeqw", "35"))
//					, new JLabel(String.format("_%-100s_ :    _%-7s_", "Player ht6hjdd", "35"))

//					new JLabel(String.format("_%-30s_:    _%-7s_", "Player", "Score").replace(' ', '*'))
//					, new JLabel(String.format("_%-30s_:    _%-7s_", "Player", "Score").replace(' ', '*'))
//					, new JLabel(String.format("_%-30s_:    _%-7s_", "Player dqdqw", "35").replace(' ', '*'))
//					, new JLabel(String.format("_%-30s_:    _%-7s_", "Player cggrqqdeqw", "35").replace(' ', '*'))
//					, new JLabel(String.format("_%-30s_:    _%-7s_", "Player cggrqqdeqw", "11").replace(' ', '*'))
//					, new JLabel(String.format("_%-30s_:    _%-7s_", "Player ht6hjdd", "35").replace(' ', '*'))

					  new JLabel(String.format("_%s_:    _%-7s_", "Pla er", "Score"))
					, new JLabel(String.format("_%s_:    _%-7s_", "Player", "Score"))
					, new JLabel(String.format("_%s_:    _%-7s_", "Player", "35"))
			));

			GameOverDialog dialog = new GameOverDialog(null, components,
					null);
			dialog.showDialog();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GameOverDialog(JFrame parentFrame, List<JComponent> contentComponent, OnDialogClose onCloseAction) {
		super(parentFrame, ModalityType.APPLICATION_MODAL);		// block the parent window after showing up

		setUndecorated(true);		// remove close button
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.onCloseAction = onCloseAction;

		setBounds(100, 100, 450, 450);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblGameOver = new JLabel("GAME OVER");
		lblGameOver.setBounds(158, 11, 106, 25);
		lblGameOver.setForeground(Color.RED);
		lblGameOver.setFont(new Font("Tahoma", Font.PLAIN, 20));
		contentPanel.add(lblGameOver);
		
		JPanel panelScore = new JPanel();
		panelScore.setBounds(10, 65, 414, 278);
		contentPanel.add(panelScore);
		panelScore.setLayout(new BoxLayout(panelScore, BoxLayout.Y_AXIS));
		
		
		this.contentComponent = contentComponent;
		
		this.contentComponent.forEach((component) -> {
//			component.setAlignmentX(Component.CENTER_ALIGNMENT);
			component.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			panelScore.add(component);
		});

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(10, 380, 414, 52);
			contentPanel.add(buttonPane);
			{
				okButton = new JButton("OK");
				okButton.setMaximumSize(new Dimension(100, 100));
				okButton.setFocusable(false);
				okButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
				okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
				okButton.setBorderPainted(false);
				okButton.setBackground(Color.GREEN);
				okButton.setForeground(Color.WHITE);
				okButton.setActionCommand("OK");

				okButton.addActionListener( actionEvent -> {
					if (this.onCloseAction != null) {
						this.onCloseAction.onClose();
					}
					dispose();
				});

				getRootPane().setDefaultButton(okButton);
			}
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
			buttonPane.add(okButton);
		}

		setLocationRelativeTo(parentFrame);

	}

	public void showDialog() {
		setVisible(true);
	}

	@Override
	public void dispose() {
		super.dispose();

		System.out.println("dispose");
	}

	public interface OnDialogClose {
		public void onClose();
	}
}
