package org.dhcp4java;
import java.net.*;
import java.util.logging.Logger;
import java.io.*;

public class serverproyecto {
	
	private static final Logger logger = Logger.getLogger("org.dhcp4java.examples.dhcpsnifferservlet");
	static byte[] buffer = new byte[1500];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		
		int xid = 0;
		byte[] macAddress = {
		        (byte) 0x08, (byte) 0x00, (byte) 0x27, (byte) 0xB8, (byte) 0x28, (byte) 0xFD};
		
		byte[] macAddressrecivida ;
		
		byte[] ipprestar = {(byte)198,(byte)60,(byte)70,(byte)8};
		
		byte[] ipmia = {(byte)199,(byte)10,(byte)10,(byte)8};
		
		byte[] broad = {(byte)255,(byte)255,(byte)255,(byte)255};
		//crear 
		DHCPPacket offer = new DHCPPacket();
		
		/*offer.setOp((byte) 2);
		offer.setHtype((byte) 1);
		offer.setHlen((byte) 6);
		offer.setHops((byte) 0);
		offer.setXid(xid);
		offer.setSecs((short) 0);
		offer.setFlags((short) 0);
		offer.setChaddr(macAddress);*/
		
		
		
		//crear respuesta
		DHCPPacket res = new DHCPPacket();
		res.setOp((byte) 2);
        res.setHtype((byte) 1);
        res.setHlen((byte) 6);
        res.setHops((byte) 0);
        res.setXid (xid);
        res.setSecs((short) 0);
        res.setFlags((short) 0);
        res.setChaddr(macAddress);
      
        
		 System.out.println("inicio servidor");
		try {
			/*res.setYiaddr("198.60.70.8");
			res.setSiaddr("199.10.10.8");*/
			
		   //InetAddress direccion = InetAddress.getByName("comunicaiones-VirtualBox");
			System.out.println("inicio sd");
		   InetAddress prestar = InetAddress.getByAddress(ipprestar);
		   System.out.println("inicio ");
		   InetAddress miIp = InetAddress.getByAddress(ipmia);
		   
		   InetAddress broadcast = InetAddress.getByAddress(broad);
		   
		   InetAddress macenviar;
		   
		   System.out.println("ip prestar"+ prestar.toString());
		   System.out.println("entro"+ miIp.toString());
		   
		   
           DatagramSocket socket = new DatagramSocket(67);
            
           
           //DatagramSocket socketsali = new DatagramSocket(68);
            System.out.println("entro");

            while (true) {
            	System.out.println("entro 1");
                DatagramPacket pac = new DatagramPacket(new byte[1500], 1500);
                System.out.println("entro2");
                DHCPPacket     dhcp;
                System.out.println("entr3");
                socket.receive(pac);
                System.out.println("entro4");
                dhcp = DHCPPacket.getPacket(pac);
                System.out.println("entro5");
                System.out.println(dhcp.toString());
                System.out.println("entro6");
                
                //conseguir info cliente
                xid = dhcp.getXid();
                res.setXid (xid);
                macAddressrecivida = dhcp.getChaddr();
           //     res.setChaddr(macAddressrecivida);
                System.out.println("el xid es  " + dhcp.getXid() + "la mac es  " + dhcp.getChaddrAsHex() + "la net" + dhcp.getPort() + "la giad es :"+ dhcp.getGiaddr());
                
                
                System.out.println("creando offer");
                DHCPPacket respuest = DHCPResponseFactory.makeDHCPOffer(dhcp, prestar, 3600, miIp, null, null);
                System.out.println("se creo");
                //respuest.setOptionAsInetAddress((byte)28, broadcast);
                
                System.out.println("la clase de respt es :"+respuest.getClass());
                System.out.println("el paquete creado:");
                System.out.println(respuest.toString());
                System.out.println("mostrar respuesta");
                
                
                
                //macenviar = InetAddress.getByAddress(dhcp.getOptionRaw((byte) 5)) ;
                
                System.out.println("se serializa");
                buffer = respuest.serialize();
                System.out.println("serializo");
                //InetAddress miIpa = dhcp.getOptionAsInetAddrs((byte) 5);
                System.out.println("relay agent diecover");
                System.out.println( " relaya agent"+dhcp.getGiaddr());
                System.out.println("relayagent offer");
                System.out.println( " relaya agent"+ respuest.getGiaddr());
                System.out.println("creodatagram");
                DatagramPacket envoff = new DatagramPacket(buffer, buffer.length,dhcp.getGiaddr(),67);
                System.out.println("se packete");
                socket.send(envoff);
                System.out.println("se envio ");
                
                
                
                
                DatagramPacket packack = new DatagramPacket(new byte[1500], 1500);
                
                DHCPPacket  request;
                
                socket.receive(packack);
                
                request  = DHCPPacket.getPacket(packack);
                
                System.out.println(request.toString());
                
 
                DHCPPacket ack = DHCPResponseFactory.makeDHCPAck(request, prestar, 3600, miIp, null, null);
                
                buffer = ack.serialize();
                
                DatagramPacket enviarack = new DatagramPacket(buffer, buffer.length,dhcp.getGiaddr(),67);
                
                socket.send(enviarack);
                
                // enviar respuesta
                
              /* System.out.println("se serializa");
                buffer = res.serialize();
                System.out.println("serializo");
                DatagramPacket envoff = new DatagramPacket(buffer, buffer.length,direccion,68);
                System.out.println("se packete");
                socketsali.send(envoff);
                System.out.println("se envio ");
                DHCPCoreServer server = DHCPCoreServer.initServer(new DHCPServlet(), null);
				new Thread(server).start();
				server.init();
				server.run();
				server.dispatch();
				buffer = res.serialize();
				DatagramPacket d = new DatagramPacket(buffer , buffer.length , null, 68);
				server.sendResponse(d);*/
                
                
                //System.out.println(dhcp.getXid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
		
		
		
	}

}
