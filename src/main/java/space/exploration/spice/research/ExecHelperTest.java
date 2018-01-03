package space.exploration.spice.research;

public class ExecHelperTest {

    public static void main(String[] args) {
        ExecFileHelper execFileHelper = new ExecFileHelper("/home/sanket/Documents/workspace/SpiceUtilities/src/main/resources/POSITIONS/finalPositionCalc",
                                                           "/home/sanket/Documents/workspace/SpiceUtilities/src/main/resources/POSITIONS/finalPositionCalc");
        for(String s: execFileHelper.makeFiles()){
            System.out.println(s);
        }
    }
}
