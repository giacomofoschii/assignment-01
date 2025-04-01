package pcd.ass01.taskexecutor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

public class BoidsView implements ChangeListener {

	private final JFrame frame;
	private final BoidsPanel boidsPanel;
	private final JSlider cohesionSlider, separationSlider, alignmentSlider;
	private boolean isRunning;
	private final BoidsController boidsController;
	private final int width, height;
	
	public BoidsView(final BoidsController boidsController, final int width, final int height) {
		this.boidsController = boidsController;
		this.width = width;
		this.height = height;
		this.isRunning = true;
		
		frame = new JFrame("Boids Simulation");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);

        boidsPanel = new BoidsPanel(this, this.boidsController.getModel());
		cp.add(BorderLayout.CENTER, boidsPanel);

        JPanel controlPanel = getNewControlPanel();

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

		startPanel();
	}


	private JPanel getNewControlPanel() {
		JPanel controlPanel = new JPanel();

		JButton pauseButton = new JButton("Pause");
		JButton stopButton = new JButton("Stop");

		stopButton.addActionListener(e -> {
			boidsController.getSimulator().stopSimulation();
			startPanel();
			boidsController.getSimulator().newSimulation();
			if(!isRunning) {
				pauseButton.setText("Pause");
				isRunning = true;
			}
		});

		pauseButton.addActionListener(e -> {
			if (isRunning) {
				this.boidsController.getSimulator().pauseSimulation();
				pauseButton.setText("Resume");
			} else {
				this.boidsController.getSimulator().resumeSimulation();
				pauseButton.setText("Pause");
			}
			isRunning = !isRunning;
		});

		controlPanel.add(stopButton);
		controlPanel.add(pauseButton);
		return controlPanel;
	}

	private void startPanel(){
		boolean starting = false;
		while(!starting) {
			String input = JOptionPane.showInputDialog(frame, "Insert boids' number",
					"Boids' number", JOptionPane.QUESTION_MESSAGE);
			if(input == null) {
				this.boidsController.getSimulator().stopExecutor();
				frame.dispose();
				System.exit(0);
			}
			try {
				int nBoids = Integer.parseInt(input);
				if (nBoids > 0) {
					this.boidsController.getModel().setBoidsNumber(nBoids);
					starting = true;
				} else {
					JOptionPane.showMessageDialog(frame, "Boids' number must be positive",
							"Errore di input", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Letters or symbols not allowed, insert a valid number!",
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
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(0, new JLabel("0"));
		labelTable.put(10, new JLabel("1"));
		labelTable.put(20, new JLabel("2"));
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
			this.boidsController.getModel().setSeparationWeight(0.1*val);
		} else if (e.getSource() == cohesionSlider) {
			var val = cohesionSlider.getValue();
			this.boidsController.getModel().setCohesionWeight(0.1*val);
		} else {
			var val = alignmentSlider.getValue();
			this.boidsController.getModel().setAlignmentWeight(0.1*val);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
