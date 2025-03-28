package pcd.ass01.multithreading;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

public class BoidsView implements ChangeListener {

    private final JFrame frame;
    private final BoidsPanel boidsPanel;
    private final JSlider cohesionSlider, separationSlider, alignmentSlider;
    private final JButton stopButton, pauseButton;
    private boolean isRunning;
    private final BoidsModel model;
    private final BoidsController boidsController;
    private final int width, height;

    public BoidsView(final BoidsModel model, final int width, final int height) {
        this.model = model;
        this.width = width;
        this.height = height;
        this.isRunning = true;
        this.boidsController = new BoidsController();

        frame = new JFrame("Boids Simulation");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel cp = new JPanel();
        cp.setLayout(new BorderLayout());

        boidsPanel = new BoidsPanel(this, model);
        cp.add(BorderLayout.CENTER, boidsPanel);

        JPanel controlPanel = new JPanel();

        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");

        stopButton.addActionListener(e -> boidsController.stopAndRestartSimulation(this, this.model));

        pauseButton.addActionListener(e -> {
            if (isRunning) {
                boidsController.getSimulator().pauseSimulation();
                pauseButton.setText("Resume");
            } else {
                boidsController.getSimulator().resumeSimulation();
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

        startPanel();
    }

    private void startPanel() {
        boolean starting = false;
        while (!starting) {
            String input = JOptionPane.showInputDialog(frame, "Inserisci il numero di boids:",
                    "Numero di Boids", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                frame.dispose();
                System.exit(0);
            }
            try {
                int nBoids = Integer.parseInt(input);
                if (nBoids > 0) {
                    model.setBoidsNumber(nBoids);
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
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("0"));
        labelTable.put(10, new JLabel("1"));
        labelTable.put(20, new JLabel("2"));
        slider.setLabelTable(labelTable);
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
            model.setSeparationWeight(0.1 * separationSlider.getValue());
        } else if (e.getSource() == cohesionSlider) {
            model.setCohesionWeight(0.1 * cohesionSlider.getValue());
        } else {
            model.setAlignmentWeight(0.1 * alignmentSlider.getValue());
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSimulator(BoidsSimulator simulator) {
        this.boidsController.setBoidsSimulator(simulator);
    }
}
