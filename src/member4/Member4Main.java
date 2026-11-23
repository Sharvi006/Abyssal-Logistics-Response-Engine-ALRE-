package member4;

public class Member4Main {
    public static void main(String[] args) {
        ReliefState initialState = new ReliefState("Global Crisis Hub", 10);
        StateSerializer serializer = new StateSerializer();
        ActionLogger logger = new ActionLogger("member4_actions.log");

        ConsoleMenu menu = new ConsoleMenu(initialState, serializer, logger);
        menu.run();
    }
}