import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by scott on 18/02/14.
 */
public class MeetingClient {
    private InetAddress inetAddress;
    private int portNumber;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public MeetingClient(String host, int port) throws IOException{
        inetAddress = InetAddress.getByName(host);
        portNumber = port;
        socket = new Socket(inetAddress, portNumber);
        OutputStream out = socket.getOutputStream();
        outputStream = new ObjectOutputStream(
                new BufferedOutputStream(out)
        );
        outputStream.flush();
        InputStream in = socket.getInputStream();
        inputStream = new ObjectInputStream(in);
    }
    private void getMeetTimes(Course course)throws IOException, ClassNotFoundException{
        outputStream.writeObject(course);
        outputStream.flush();
        Course c;
        for(;;){
            c = (Course)inputStream.readObject();
            if(c==null){
                break;
            }
            System.out.println(c.getName()+" meets on: ");
            ArrayList<String> buildings = c.getBuildingCodes();
            ArrayList<String> rooms = c.getRoomNumbers();
            ArrayList<String> starts = c.getStartTimes();
            ArrayList<String> ends = c.getEndTimes();
            ArrayList<String> days = c.getDays();
            for(int i=0; i<starts.size() ;i++){
                String day = getDayFromDayCode(days.get(i));
                System.out.println(day + " at " + starts.get(i) + " untill " + ends.get(i) + " in " + buildings.get(i)+"-"+rooms.get(i));
            }
        }
        inputStream.close();
        outputStream.close();
    }
    private static String getDayFromDayCode(String daycode){
        String day = "";
        char code = daycode.charAt(0);
        switch (code){
            case 'M':
                day="Monday";
                break;
            case 'T':
                day="Tuesday";
                break;
            case 'W':
                day="Wednesday";
                break;
            case 'R':
                day="Thursday";
                break;
            case 'F':
                day="Friday";
                break;
        }
        return day;
    }
    public static void main(String[] args)throws ClassNotFoundException{
        if(args.length!=4){
            System.out.println("Usage: java MeetingClient hostname port_number subject course_number");
            System.exit(1);
        }
        try{
            MeetingClient client = new MeetingClient(args[0], Integer.parseInt(args[1]));
            String name = args[2]+"-"+args[3];
            Course course = new Course(name);
            client.getMeetTimes(course);
            client.socket.close();
        }
        catch(IOException ex){
            System.err.println("Error: Could not connect to host "+args[0]+" at port "+args[1]);
        }

    }
}
