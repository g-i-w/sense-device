import java.util.*;
import java.io.*;

public class SenseDevice {

  List<String> devices;
  List<String> addedDevices;
  List<String> removedDevices;

  public SenseDevice () {
    devices = deviceList();
  }

  public static List<String> deviceList () {
    List<String> list = new ArrayList<>();
    try {
      Process p = Runtime.getRuntime().exec( "ls -1", null, new File("/dev/") );
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      // read the output from the command
      String s = null;
      while ((s = stdInput.readLine()) != null) {
          list.add( s );
      }
      // read any errors from the attempted command
      while ((s = stdError.readLine()) != null) {
          System.err.println( s );
      }
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    return list;
  }

  public void diffDevices () {
    diffDevices( deviceList() );
  }

  public void diffDevices ( List<String> newDevices ) {
    addedDevices = new ArrayList<>();
    removedDevices = new ArrayList<>();
    for (int a=0, b=0; a<newDevices.size(); a++, b++) {
      int cmp = newDevices.get(a).compareTo(devices.get(b));
      // System.out.println( newDevices.get(a)+" compared to "+devices.get(b)+" is "+cmp );
      if (cmp<0) {
        addedDevices.add(newDevices.get(a));
        // System.out.println(addedDevices);
        a++;
      } else if (cmp>0) {
        removedDevices.add(devices.get(b));
        // System.out.println(removedDevices);
        b++;
      }
    }
    devices = deviceList();
  }

  public String toString () {
    String changes = "";
    changes += (addedDevices.size()>0 ? "---\nAdded: \n" : "");
    for (String device : addedDevices) changes += "  "+device+"\n";
    changes += (removedDevices.size()>0 ? "---\nRemoved: \n" : "");
    for (String device : removedDevices) changes += "  "+device+"\n";
    return changes;
  }

  public static void test () {
    SenseDevice sd = new SenseDevice();
    List<String> newList = sd.deviceList();
    newList.remove(2);
    sd.diffDevices( newList );
    System.out.println( sd );
  }

  public static void main(String[] args) throws Exception {
    SenseDevice sd = new SenseDevice();
    while(true) {
      sd.diffDevices();
      System.out.print( sd );
      Thread.sleep(1000);
    }
  }
}
