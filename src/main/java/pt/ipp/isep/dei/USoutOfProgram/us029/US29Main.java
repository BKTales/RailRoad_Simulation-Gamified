package pt.ipp.isep.dei.USoutOfProgram.us029;

public class US29Main {
    public static void main(String[] args) {
        US29UI ui = new US29UI();
        US29Controller controller = new US29Controller(ui);
        controller.runTests();
    }
}
