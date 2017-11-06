import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Random;

public class Ipv6Client {
	 public static void main(String[] args) {
	        try {
	            Socket socket = new Socket("18.221.102.182", 38004);
	            System.out.println("Connected to server.");
	            
	            InputStream is = socket.getInputStream();
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            OutputStream os = socket.getOutputStream();
	            DataInputStream dis = new DataInputStream(is);
	            DataOutputStream dos = new DataOutputStream(os);
	            
	           
	            
	            for(int i = 1; i <= 12; i++) {
	            	int size = (int) Math.pow(2,i);
	        		byte[] packet = new byte[(40 + size)];

	        		//		First Row
	        		//Version & Traffic & Flow Label
	        		packet[0] = (byte) (6 * 16);
	        		packet[1] = 0;
	        		packet[2] = 0;
	        		packet[3] = 0;

	        		//		Second Row
	        		//Payload length + Next Header + hop limit
	        		packet[4] = (byte) ((size & 0xFF00) >> 8);
	        		packet[5] = (byte) (size & 0x00FF);
	        		packet[6] = (byte) (17);
	        		packet[7] = (byte) (20);

	        	

	        		
	        		byte[] ipv4Source = new byte[4];
					new Random().nextBytes(ipv4Source); //a random IPv4 address
					for(int j = 8; j < 18; j++) //80 0s for sourceAddr
						packet[j] = 0;
					for(int j = 18; j < 20; j++) //16 1s for sourceAddress
						packet[j] = (byte)0xFF;
					for(int j = 0; j < 4; j++) //4 byte IPv4 address
						packet[j+20] = ipv4Source[j];
					byte[] ipv4Dest = socket.getInetAddress().getAddress();
					for(int j = 24; j < 34; j++) //80 0s for destAddr
						packet[j] = 0;
					for(int j = 34; j < 36; j++) //16 1s for destAddr
						packet[j] = (byte)0xFF;
					for(int j = 0; j < 4; j++) //4 byte IPv4 address
						packet[j+36] = ipv4Dest[j];
				
	        		
	        		
	        		//		Data Row(s)
	        		//Adds random numbers between 0-255
	        		for (int j = 0; j < size; j++) 
	        			packet[j + 40] = 0;
	                
	        		
	        		os.write(packet);
	        		
	        		
	        	       
	            	byte[] byteArray = new byte[4];
	        		
	        		for(int p = 0; p < 4; p++)
	        		{
	        			byteArray[p] = (byte)(dis.readUnsignedByte());
	        		}
	        		System.out.println("Data Size: " + size +  "\n0x" + bytesToHex(byteArray));
	        		
	        	
	            }
	        }
	        catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	 
	 
	 	/**
		 * Converts bytes to Hex. 
		 * Returns string with Hex values.
		 */
		private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
		public static String bytesToHex(byte[] bytes) 
		{
		    char[] hexChars = new char[bytes.length * 2];
		    for ( int j = 0; j < bytes.length; j++ ) {
		        int v = bytes[j] & 0xFF;
		        hexChars[j * 2] = hexArray[v >>> 4];
		        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		    }
		    return new String(hexChars);
		}
}
