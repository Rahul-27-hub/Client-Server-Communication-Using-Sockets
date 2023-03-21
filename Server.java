import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Server extends Thread{
	//Thread for file sync
	@Override
	public void run() {
		File firstserverlist = Client.GetDirectoryContents();// Fetching file list for Server 1
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
						String s1 = ("cp ." + File.separator + "directory_a" + File.separator + diffname + " ."
								+ File.separator + "directory_b");

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
						String s2 = ("echo y | rm -i ./directory_b/" + diffname);
						System.out.println(s2);
						Runtime.getRuntime().exec(new String[] { "sh", "-c", s2 });
						System.out.println("else if Continue");
						initlist.remove(i);
						count = initlist.size();
					}
				} else {
					System.out.println("else Continue");
					Runtime.getRuntime().exec("cp -u ./directory_a/* ./directory_b");
					Runtime.getRuntime().exec("cp -u ./directory_b/* ./directory_a");
				}
			} catch (Exception e) {
				System.out.println(e);
			}

		}


	}
	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException{
		System.out.println("Server  running");
		Server t1 = new Server();
		t1.start();

		ServerSocket server1 = new ServerSocket(1342);
		Socket firstserver = server1.accept();
        Scanner serverscanner = new Scanner(firstserver.getInputStream());

		PrintStream client = new PrintStream(firstserver.getOutputStream());
		if(serverscanner.hasNextLine()) {
			String clientreq = serverscanner.nextLine();
			System.out.println(clientreq);
		}
		client.println("Reply");


	}
public static File GetDirectoryContents() {

		return(new File(System.getProperty("user.dir")+File.separator+"directory_a"));

	}
}
