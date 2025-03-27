package pcd.ass01;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.util.Hashtable;

public class BoidsView implements ChangeListener {

	private JFrame frame;
	private BoidsPanel boidsPanel;
	private JSlider cohesionSlider, separationSlider, alignmentSlider;
	private JButton stopButton, pauseButton;
	private boolean isRunning;
	private BoidsModel model;
	private BoidsSimulator simulator;
	private int width, height;
	private BoidsController boidsController;
	
	public BoidsView(BoidsModel model, int width, int height) {
		this.model = model;
		this.width = width;
		this.height = height;
		this.isRunning = true;
		this.boidsController = new BoidsController();

		frame = new JFrame("Boids Simulation");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);

        boidsPanel = new BoidsPanel(this, model);
		cp.add(BorderLayout.CENTER, boidsPanel);

		JPanel controlPanel = new JPanel();

		pauseButton = new JButton("Pause");
		stopButton = new JButton("Stop");

		stopButton.addActionListener(e -> {
			simulator.stopSimulation();
			frame.dispose();
			System.exit(0);
        });

		pauseButton.addActionListener(e -> {
			if (isRunning) {
				simulator.pauseSimulation();
				pauseButton.setText("Resume");
			} else {
				simulator.resumeSimulation();
				pauseButton.setText("Pause");
			}
			isRunning = !isRunning;
		});

		controlPanel.add(stopButton);
		controlPanel.add(pauseButton);

		cp.add(BorderLayout.NORTH, controlPanel);

        JPanel slidersPanel = new JPanel();

        cohesionSlider = makeSlider();
        separationSlider = makeSlider();
        alignmentSlider = makeSlider();

        slidersPanel.add(new JLabel("Separation"));
        slidersPanel.add(separationSlider);
        slidersPanel.add(new JLabel("Alignment"));
        slidersPanel.add(alignmentSlider);
        slidersPanel.add(new JLabel("Cohesion"));
        slidersPanel.add(cohesionSlider);

		cp.add(BorderLayout.SOUTH, slidersPanel);

		frame.setContentPane(cp);

        frame.setVisible(true);

		boolean starting = false;
		while(!starting) {
			// Mostra il dialogo di input per il numero di boids
			String input = JOptionPane.showInputDialog(frame, "Inserisci il numero di boids:",
					"Numero di Boids", JOptionPane.QUESTION_MESSAGE);
			if(input == null) {
				frame.dispose();
				System.exit(0);
			}
			try {
				int nBoids = Integer.parseInt(input);
				if (nBoids > 0) {
					this.boidsController.setBoidsNumber(nBoids, model);
					this.simulator = new BoidsSimulator(model);
					this.simulator.attachView(this);
					this.simulator.runSimulation();
					starting = true;
				} else {
					JOptionPane.showMessageDialog(frame, "Il numero di boids deve essere positivo",
							"Errore di input", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Inserisci un numero valido",
						"Errore di input", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private JSlider makeSlider() {
		var slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 10);        
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		Hashtable labelTable = new Hashtable<>();
		labelTable.put( 0, new JLabel("0") );
		labelTable.put( 10, new JLabel("1") );
		labelTable.put( 20, new JLabel("2") );
		slider.setLabelTable( labelTable );
		slider.setPaintLabels(true);
        slider.addChangeListener(this);
		return slider;
	}
	
	public void update(int frameRate) {
		boidsPanel.setFrameRate(frameRate);
		boidsPanel.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == separationSlider) {
			var val = separationSlider.getValue();
			model.setSeparationWeight(0.1*val);
		} else if (e.getSource() == cohesionSlider) {
			var val = cohesionSlider.getValue();
			model.setCohesionWeight(0.1*val);
		} else {
			var val = alignmentSlider.getValue();
			model.setAlignmentWeight(0.1*val);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
