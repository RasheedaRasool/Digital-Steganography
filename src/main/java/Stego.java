//package Dig_Stego;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner; 

public class Stego extends JFrame implements ActionListener
{
	BufferedImage originalImageText = null;
	BufferedImage coverImageText = null;
	
	JLabel l;  
    JButton en,de;  
    String ecode,dcode,fl,st=null;
    int flag=0,flag1=0;
   
    public static BufferedImage embedText(BufferedImage originalImage, String text)
    {
        int x = 0, y = 0, asciiValue;//x= x coordinate, y=y coordinate of image starting from top left .
        final int EXTRACTOR = 0x00000001;//BitMask to extract last bit of character.
        final int ZEROATLAST=0xfffffffe;
        for (int i = 0; i <= text.length(); i++)
            {
                if (i < text.length())
                	asciiValue = text.charAt(i);
               else
            	   asciiValue = 0;// Will be used at the end to mark the end of text.
                for (int j = 0; j < 8; j++)//8 bits forms a character.
                    {
                        int bitValue = asciiValue & EXTRACTOR;//extracts single bit from the character

                        if (bitValue == 1)
                            originalImage.setRGB(x, y, originalImage.getRGB(x, y) | EXTRACTOR);//Replaces least significant value of the blue color of the pixel with 1.
                        else
                        	originalImage.setRGB(x, y, originalImage.getRGB(x, y) & ZEROATLAST);//Replaces least significant value of the blue color of the pixel with 0.
                        x++;
                        if (x > originalImage.getWidth())
                        {
                                x = 0;
                                y++;
                        }
                        asciiValue = asciiValue >> 1;
                    }
            }
        return originalImage;
    }
	
	public static StringBuilder extractText(BufferedImage encryptedImage)
    {
        int x = 0, y = 0, bitValue;
        final int EXTRACTOR = 0x00000001;
        final int ONEATSTART=0x80;
        char chars;
        int asciiCode = 0;
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; ; i++)
            {
                for (int j = 0; j < 8; j++)
                    {
                        bitValue = encryptedImage.getRGB(x, y) & EXTRACTOR;//Extracts last bit from blue color.
                        x++;
                        if (x > encryptedImage.getWidth())
                            {
                                x = 0;
                                y++;
                            }
                        asciiCode = asciiCode >> 1;//Left shift to form the character moving the bits by one place and store a new bit.
                        if (bitValue == 1)
                        	asciiCode = asciiCode | ONEATSTART;//Replaces bit value with 1
                    }
	                if (asciiCode == 0)//Checks for ascii value 1 marking end of the text.
	                    break;
	                chars = (char) asciiCode;	
	                stringBuilder.append(chars);//for appending characters at the end of the previous characters to form a string.
            }
        return stringBuilder;
    }

    Stego()
    {       	
		try {
			originalImageText = ImageIO.read(new File("C:\\Users\\Administrator\\Pictures\\StegoCover.jpg"));
			//coverImageText = ImageIO.read(new File("C:\\Users\\Administrator\\Pictures\\StegoCover.jpg"));
			try 
			{
				 coverImageText = ImageIO.read(new File("C:\\Users\\Administrator\\Pictures\\Desktop12.jpg"));
			}
			catch(IOException e) 
			{
				System.out.println("Exception "+e);
			}
			System.out.print("Embedding ");
	    	JFrame f1= new JFrame("DIGITAL STEGANOGRAPHY");
	    	f1.setSize(400,400); 
	    	f1.setBackground(Color.WHITE);
	    	f1.setLocationRelativeTo(null);
	    	
	    	l=new JLabel("DIGITAL STEGANOGRAPHY");  
	        l.setBounds(125,70,200,80);  
	        l.setBackground(Color.BLACK);
	        
	        en=new JButton("ENCRYPT");
	        en.setBounds(150,150,100,30);  
	        en.setBackground(Color.GREEN);
	        en.addActionListener(new ActionListener() 
	        {
		        public void actionPerformed(ActionEvent e)
		        {  
	           		JFrame f,f2;
	           		JTextArea tf;
	           		JTextField tf1;//,tf;
	           		JPanel panel1,panel2,panel3;
	           		JLabel label,label1,label2,label3,label4,labelO,labelS;
	           		JButton b;
	           		
	           		f= new JFrame("ENCRYPTION");
	    			f.setSize(600,600); 
	    			f.setLocationRelativeTo(null);
	    			
	    			label=new JLabel("ENTER STEGO KEY");
	    			label.setBounds(230,80,200,20); 
	    			label.setBackground(Color.WHITE); 
	    			f.add(label);
	    	        
	    	        tf1=new JTextField();  
				    tf1.setBounds(250,120,80,30);  
					tf1.setBorder(BorderFactory.createLineBorder(Color.BLACK));					
					f.add(tf1);
			
	    			label1 = new JLabel("ENTER SECRET MESSAGE");  
	    		    label1.setBounds(230,160,320,20);  
	    		    f.add(label1);
	    		        		    
	    		    tf=new JTextArea();  
	    		    tf.setBounds(10,180,560,300); 
	    			f.add(tf);
	    				    			    			
	    			b=new JButton("ENCRYPT");
	     	        b.setBounds(250,500,100,30);  
	     	        b.addActionListener(this); 
	     	        b.setBackground(Color.WHITE);
	     	        
	     	        b.addActionListener(new ActionListener() 
	 		        {
	 			        public void actionPerformed(ActionEvent e)
	 			        {  
	 			        	ecode=tf1.getText();
	 			        	System.out.println("Ecode"+ecode);
	 			        	flag1=1;
	 			        	try {
	 			        	st=tf.getText();
	 			        	}catch(Exception ex)
	 			        	{
	 			        		JOptionPane.showInputDialog(this, ex.getMessage());
	 			        	}
	 			        }
	 		        });	     	          		    
	    		    
	    		    if(st!=null)
	    		    	coverImageText = embedText(originalImageText,st);	 
	    		    if(flag1==1)
	     	        {	
	    		    	b.setVisible(false);
	    		    	f= new JFrame("ENCRYPTION");
		    			f.setSize(1100,500); 
		    			f.setLocationRelativeTo(null);	                   
		    		    
		    			label2 = new JLabel("ORIGINAL IMAGE");  
		    		    label2.setBounds(250,70,300,20);  
		    		    f.add(label2);
		    		    
		    		    labelO= new JLabel(new ImageIcon(originalImageText));
		    		    labelO.setBounds(20,110,515,320);
		    		    labelO.setBackground(Color.WHITE);  
		    		    labelO.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		    		    f.add(labelO); 
		    			
		    			label3 = new JLabel("STEGANOGRAPHED IMAGE");  
		    		    label3.setBounds(700,70,200,20);  
		    		    f.add(label3);
		    		    
		    		    labelS=new JLabel(new ImageIcon(coverImageText));  
					    labelS.setBounds(555,110,515,320);
					    labelS.setBackground(Color.WHITE);  
					    labelS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
					    f.add(labelS);    					    
		     	        }
		    	        f.add(b);
		    		    f.setLayout(null);    
		    		    f.setVisible(true); 
		        	}
	        	});
		        de=new JButton("DECRYPT");
		        
		        de.setBounds(150,200,100,30);  
		        de.setBackground(Color.RED);
		        
		        de.addActionListener(new ActionListener() 
		        {
		        	public void actionPerformed(ActionEvent e)
		        	{ 
				        JFrame f;				
						JLabel label1,label2,label3,label4,labelS;
						JPanel panel1,panel2;
						JButton b1;
						JTextField tf2;
										
						f= new JFrame("DECRYPTION");
						f.setSize(500,500); 						
						f.setLocationRelativeTo(null);
						
						label3 = new JLabel("ENTER STEGO KEY");  
		    		    label3.setBounds(180,170,200,30);  
		    		    f.add(label3);
		    		       		    
						tf2=new JTextField();  
					    tf2.setBounds(200,210,80,30);  
						tf2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
						
						b1=new JButton("DECRYPT");
		    	        b1.setBounds(195,270,100,30); 
		    	        b1.addActionListener(this); 
		    	        b1.setBackground(Color.WHITE);
		    	       
						b1.addActionListener(new ActionListener() 
				        {
					        public void actionPerformed(ActionEvent e)
					        {  
					        	System.out.println("Dcode"+dcode);
					        	dcode=tf2.getText();
					        	System.out.println("Dcode"+dcode);
			        	 		if(dcode.compareTo(ecode)==0)
			        	 			flag=1;	
			        	 		else
					        		JOptionPane.showMessageDialog(f, "Incorrect Stego Key");
					        	
					        	
					        }
				        });
						f.add(tf2);
						label4 = new JLabel(fl);  
		    		    label4.setBounds(180,365,320,20);  
		    		    f.add(label4);
						 
			    	    f.add(b1);
						if(flag==1)
						{		
							b1.setVisible(false);
							label3.setVisible(false);
							tf2.setVisible(false);
			    			f.setSize(990,800); 
			    			f.setLocationRelativeTo(null);
							extractText(coverImageText);
							JTextArea tf;
							label1 = new JLabel("STEGANOGRAPHED IMAGE");  
						    label1.setBounds(430,10,320,15);  
						    f.add(label1);
						    
						    labelS=new JLabel(new ImageIcon(coverImageText));  
						    labelS.setBounds(310,30,450,330);    
						    labelS.setBackground(Color.WHITE);  
						    labelS.setBorder(BorderFactory.createLineBorder(Color.BLACK));
						    f.add(labelS); 
						    				   
						    label2 = new JLabel("DECRYPTED MESSSAGE");  
						    label2.setBounds(430,370,180,20);  
						    f.add(label2);
						    
						    tf=new JTextArea(st);  
						    tf.setBounds(15,420,940,200);  
						    tf.setText(st);
							f.add(tf);
												   
						    panel2=new JPanel();  
						    panel2.setBounds(10,400,950,300);    
						    panel2.setBackground(Color.WHITE);  
						    panel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
						    f.add(panel2);  
						}
						f.setLayout(null);    
					    f.setVisible(true); 
		        	}
		        });       	
		         
		        f1.add(en);
		        f1.add(de);
		        f1.add(l);  
		        f1.setLayout(null);  
		        f1.setVisible(true);  
		        setDefaultCloseOperation(EXIT_ON_CLOSE);  
			}
			catch(IOException e) 
			{	
				System.out.println("ERROR OCCURED"+e);
			}
    }  
     
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new Stego();  
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

