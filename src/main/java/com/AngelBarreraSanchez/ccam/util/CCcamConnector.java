package com.AngelBarreraSanchez.ccam.util;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Arrays;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;

public class CCcamConnector {

	private String host, user, password;
	private int port;
	private CryptoBlock recvblock;
	private CryptoBlock sendblock;
	private Socket socket;
	private DataInputStream is;
	private BufferedOutputStream os;

	public CCcamConnector(final CCCAMEntity cccamline) throws UnknownHostException, IOException {

		this.host = cccamline.getHost();
		this.port = Integer.valueOf(cccamline.getPort());
		this.user = cccamline.getUser();
		this.password = cccamline.getPass();

		socket = new Socket(this.host, this.port);
		is = new DataInputStream(socket.getInputStream());
		os = new BufferedOutputStream(socket.getOutputStream());
	}

	public boolean testCline() throws Exception {
		try {
			byte[] helloBytes = new byte[16];
			is.readFully(helloBytes);

			cc_crypt_xor(helloBytes); // XOR init bytes with 'CCcam'

			MessageDigest md;
			md = MessageDigest.getInstance("SHA-1");
			byte[] sha1hash = new byte[20];
			sha1hash = md.digest(helloBytes);

			recvblock = new CryptoBlock();
			recvblock.cc_crypt_init(sha1hash, 20);
			recvblock.cc_decrypt(helloBytes, 16);

			sendblock = new CryptoBlock();
			sendblock.cc_crypt_init(helloBytes, 16);
			sendblock.cc_decrypt(sha1hash, 20);

			SendMsg(20, sha1hash);// send crypted hash to server

			byte[] userBuf = new byte[20];
			System.arraycopy(user.getBytes(), 0, userBuf, 0, user.length());
			SendMsg(20, userBuf);// send username to server

			byte[] pwd = new byte[password.length()];
			System.arraycopy(password.getBytes(), 0, pwd, 0, password.length());
			sendblock.cc_encrypt(pwd, password.length()); // encript the
															// password

			byte[] CCcam = { 'C', 'C', 'c', 'a', 'm', 0 };
			SendMsg(6, CCcam); // But send CCcam\0

			byte[] rcvBuf = new byte[20];
			is.read(rcvBuf);
			recvblock.cc_decrypt(rcvBuf, 20);
			// check if received string after decription equals "CCcam"
			if (Arrays.equals(CCcam, Arrays.copyOf(rcvBuf, 6))) {
				// CCLine is correct!!!!
				socket.close();
				is.close();
				os.close();
				return true;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		socket.close();
		is.close();
		os.close();
		return false;
	}

	private synchronized int SendMsg(int len, byte[] buf) throws IOException {
		byte[] netbuf;
		netbuf = new byte[len];
		System.arraycopy(buf, 0, netbuf, 0, len);
		sendblock.cc_encrypt(netbuf, len);

		try {
			os.write(netbuf);
			os.flush();
			return len;
		} catch (IOException e) {
			socket.close();
		}
		return -1;
	}

	private void cc_crypt_xor(byte[] data) {
		String cccam = "CCcam";
		for (byte i = 0; i < 8; i++) {
			data[8 + i] = (byte) (i * data[i]);
			if (i < 5)
				data[i] ^= cccam.charAt(i);
		}
	}

	private class CryptoBlock {

		int[] keytable;
		int state;
		int counter;
		int sum;

		protected CryptoBlock() {
			keytable = new int[256];
			state = 0;
			counter = 0;
			sum = 0;
		}

		protected void cc_crypt_init(byte[] key, int len) {
			int i;
			for (i = 0; i < 256; i++)
				keytable[i] = i;
			int j = 0;
			for (i = 0; i < 256; i++) {
				j = 0xff & (j + key[i % len] + keytable[i]);
				// Swap
				int k = keytable[i];
				keytable[i] = keytable[j];
				keytable[j] = k;
			}

			state = key[0];
			counter = 0;
			sum = 0;
		}

		protected void cc_encrypt(byte[] data, int len) {
			for (int i = 0; i < len; i++) {
				counter = 0xff & (counter + 1);
				sum += keytable[counter];

				byte k = (byte) keytable[counter];
				keytable[counter] = keytable[sum & 0xFF];
				keytable[sum & 0xFF] = k;

				byte z = data[i];
				data[i] = (byte) (z ^ keytable[keytable[counter & 0xFF] + keytable[sum & 0xFF] & 0xFF] ^ state);
				state = 0xff & (state ^ z);
			}
		}

		protected void cc_decrypt(byte[] data, int len) {
			for (int i = 0; i < len; i++) {
				counter = 0xff & (counter + 1);
				sum += keytable[counter];

				byte k = (byte) keytable[counter];
				keytable[counter] = keytable[sum & 0xFF];
				keytable[sum & 0xFF] = k;

				byte z = data[i];
				data[i] = (byte) (z ^ keytable[keytable[counter] + keytable[sum & 0xFF] & 0xFF] ^ state);
				z = data[i];
				state = 0xff & (state ^ z);
			}
		}
	}
}
