import java.io.File;
import java.util.*;
import java.io.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

public class Client extends Thread {
	// thread for file synchronization
	@Override
	public void run() {
		//String s = System.getProperty("user.dir") + File.separator + "directory_a";

		// System.out.println(s);
		File firstserverlist = Server.GetDirectoryContents();// Fetching file list for Server 1
		File secondserverlist = GetDirectoryContents();// File list for Server 2
		String arrs1[] = firstserverlist.list();// Storing server 1 file list to array
		String arrayserver2[] = secondserverlist.list();
		List<String> initlist = new ArrayList<String>(Arrays.asList(arrayserver2));
		int count = initlist.size();

		// initlist = Arrays.asList(arrayserver2);// Storing server 2 file list to an
		// array
		while (true) {
			try {
				File file = GetDirectoryContents();
				String arr2[] = file.list();
				List<String> currlist = new ArrayList<>();
				currlist = Arrays.asList(arr2);
				String diffname = "";
				if (currlist.size() > count) {

					for (int i = 0; i < currlist.size(); i++) {
						if (!initlist.contains(currlist.get(i))) {
							diffname = currlist.get(i);
						}
						String s1 = ("cp ." + File.separator + "directory_b" + File.separator + diffname + " ."
								+ File.separator + "directory_a");

						System.out.println(s1);
						Runtime.getRuntime().exec(s1);
						initlist.add(currlist.get(i));
						count = initlist.size();
					}
				} else if (currlist.size() < count) {
					for (int i = 0; i < initlist.size(); i++) {
						if (!currlist.contains(initlist.get(i))) {
							diffname = initlist.get(i);
							System.out.println(initlist.get(i));
						}
						String s2 = ("echo y | rm -i ./directory_a/" + diffname);
						System.out.println(s2);
						Runtime.getRuntime().exec(new String[] { "sh", "-c", s2 });
						System.out.println("else if Continue");
						initlist.remove(i);
						count = initlist.size();
					}
				} else {
					System.out.println("else Continue");
					Runtime.getRuntime().exec("cp -u ./directory_b/* ./directory_a");
					Runtime.getRuntime().exec("cp -u ./directory_a/* ./directory_b");
				}
			} catch (Exception e) {
				System.out.println(e);
			}

		}

	}

	public static void main(String args[]) throws UnknownHostException, IOException {
		System.out.println("Client running");
		Client t2 = new Client();
		t2.start();
		Socket servercall = new Socket("127.0.0.1", 1342);// Client Socket
		Scanner userinput = new Scanner(System.in);
		Scanner serverreply = new Scanner(servercall.getInputStream());// Scanner for getting input from server
		PrintStream client = new PrintStream(servercall.getOutputStream());
		client.println("Async call");
		if (serverreply.hasNextLine()) {
			String clientreq = serverreply.nextLine();
			System.out.println(clientreq);
		}

	}

	public static File GetDirectoryContents() {

		return (new File(System.getProperty("user.dir") + File.separator + "directory_b"));// Getting file list in
																							// directory

	}
}
