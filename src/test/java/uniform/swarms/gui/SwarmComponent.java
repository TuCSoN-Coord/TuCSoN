/*
 * Copyright 1999-2019 Alma Mater Studiorum - Universita' di Bologna
 *
 * This file is part of MoK <http://mok.apice.unibo.it>.
 *
 *    MoK is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    MoK is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with MoK.  If not, see <https://www.gnu.org/licenses/lgpl.html>.
 *
 */
package alice.tucson.examples.uniform.swarms.gui;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Line2D;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * @author Stefano Mariani (mailto: s [dot] mariani [at] unibo [dot] it)
 *
 */
public class SwarmComponent extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JButton food;
    private JButton long2;
    private JButton _short;
    private JButton long1;
    private JButton anthill;
    private JButton update;

    /**
     * 
     */
    public SwarmComponent() {
        super();
        final GridBagLayout myLayout = new GridBagLayout();
        this.setLayout(myLayout);
        final GridBagConstraints c = new GridBagConstraints();

        this.food = new JButton("[TC-food]");
        this.food.setName("food");
        this.food.setFont(new Font("Serif", Font.BOLD, 20));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        c.ipady = 40; // make this component tall
        c.insets = new Insets(20, 20, 60, 20); // padding
        this.add(this.food, c);

        this.long2 = new JButton("[TC-long2]");
        this.long2.setName("long2");
        this.long2.setFont(new Font("Serif", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(20, 20, 20, 20); // padding
        this.add(this.long2, c);

        this._short = new JButton("[TC-short]");
        this._short.setName("short");
        this._short.setFont(new Font("Serif", Font.BOLD, 20));
        c.gridx = 2;
        c.gridy = 2;
        this.add(this._short, c);

        this.long1 = new JButton("[TC-long1]");
        this.long1.setName("long1");
        this.long1.setFont(new Font("Serif", Font.BOLD, 20));
        c.gridx = 0;
        c.gridy = 3;
        this.add(this.long1, c);

        this.anthill = new JButton("[TC-anthill]");
        this.anthill.setName("anthill");
        this.anthill.setFont(new Font("Serif", Font.BOLD, 20));
        c.gridx = 1;
        c.gridy = 4;
        c.insets = new Insets(60, 20, 0, 20); // padding
        this.add(this.anthill, c);

        this.update = new JButton("Update view");
        this.update.setName("update");
        c.gridx = 1;
        c.gridy = 5;
        c.ipady = 0;
        c.insets = new Insets(20, 0, 0, 0); // padding
        this.add(this.update, c);
        this.update.addActionListener(new SwarmListener(this));
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        int x1 = this.food.getX();
        int y1 = this.food.getY() + (this.food.getHeight() / 2) + 20;
        int x2 = this.long2.getX() + (this.long2.getWidth() / 2);
        int y2 = this.long2.getY() + 20;
        Line2D line = new Line2D.Double(x1, y1, x2, y2);
        g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g2.draw(line);

        x1 = this.food.getX() + this.food.getWidth();
        y1 = this.food.getY() + (this.food.getHeight() / 2) + 20;
        x2 = this._short.getX() + (this._short.getWidth() / 2) + 10;
        y2 = this._short.getY() + 20;
        line = new Line2D.Double(x1, y1, x2, y2);
        g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g2.draw(line);

        x1 = this.long2.getX() + (this.long2.getWidth() / 2);
        y1 = this.long2.getY() + this.long2.getHeight() + 20;
        x2 = this.long1.getX() + (this.long1.getWidth() / 2);
        y2 = this.long1.getY() + 20;
        line = new Line2D.Double(x1, y1, x2, y2);
        g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g2.draw(line);

        x1 = this._short.getX() + (this._short.getWidth() / 2) + 10;
        y1 = this._short.getY() + this._short.getHeight() + 20;
        x2 = this.anthill.getX() + this.anthill.getWidth();
        y2 = this.anthill.getY() + (this.anthill.getHeight() / 2) + 20;
        line = new Line2D.Double(x1, y1, x2, y2);
        g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g2.draw(line);

        x1 = this.long1.getX() + (this.long1.getWidth() / 2);
        y1 = this.long1.getY() + this.long1.getHeight() + 20;
        x2 = this.anthill.getX();
        y2 = this.anthill.getY() + (this.anthill.getHeight() / 2) + 20;
        line = new Line2D.Double(x1, y1, x2, y2);
        g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        g2.draw(line);
    }

}
