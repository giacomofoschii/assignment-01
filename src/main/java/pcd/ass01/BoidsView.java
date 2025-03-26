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
	private JButton startStopbutton;
	private JTextField boidsNumberField;
	private boolean isRunning;
	private BoidsModel model;
	private BoidsSimulator simulator;
	private int width, height;
	
	public BoidsView(BoidsModel model, int width, int height) {
		this.model = model;
		this.width = width;
		this.height = height;
		this.isRunning = false;
		
		frame = new JFrame("Boids Simulation");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);

        boidsPanel = new BoidsPanel(this, model);
		cp.add(BorderLayout.CENTER, boidsPanel);

		JPanel controlPanel = new JPanel();

		startStopbutton = new JButton("Start");
		boidsNumberField = new JTextField(10);

		startStopbutton.addActionListener(e -> {
            /*if(isRunning) {
                //simulator.stopSimulation();
                startStopbutton.setText("Start");
            } else {
                simulator.runSimulation();
                startStopbutton.setText("Stop");
            }*/
            isRunning = !isRunning;
        });

		boidsNumberField = new JTextField("1500", 10);
		boidsNumberField.addActionListener(e -> {
            try {
				int nBoids = Integer.parseInt(boidsNumberField.getText());
				if(nBoids > 0) {
					model.setBoidsNumber(nBoids);
				} else {
					JOptionPane.showMessageDialog(frame, "Boids number must be positive", "Input Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(frame, "Insert a number without letters or symbols" , "Input Error", JOptionPane.ERROR_MESSAGE);
			}
        });

		controlPanel.add(startStopbutton);
		controlPanel.add(new JLabel("Number of boids:"));
		controlPanel.add(boidsNumberField);

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

	public void setSimulator(BoidsSimulator simulator) {
		this.simulator = simulator;
	}

}
