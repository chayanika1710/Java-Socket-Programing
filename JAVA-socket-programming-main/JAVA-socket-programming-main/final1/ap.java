import java.applet.*;

import java.awt.*;

import java.awt.event.*;

/*

<applet code="ap.class" width=600 height=700>

</applet>

*/

public class ap extends Applet implements ActionListener

{

Button b1;

Button b2;

TextField t1;

public void init() {

b1=new Button("click bl");

add(b1);

b2=new Button("click b2");

add(b2);

t1=new TextField(20);

add(t1);

b1.addActionListener(this);

b2.addActionListener(this);

}

public void paint(Graphics g)

{

g.setColor(Color.red); 
g.fillOval(120,120,60,60);

}

public void actionPerformed(ActionEvent ae)

{

if(ae.getSource()==b1)

t1.setText("sm");

else

t1.setText(t1.getText()+"pm");

}

}