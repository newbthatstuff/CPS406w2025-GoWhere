public class Step {
    private String instruction;
    private double distance;

    public Step(String instruction, double distance) {
        this.instruction = instruction;
        this.distance = distance;
    }

    public String getInstruction() {
        return instruction + " (" + (int)distance + " pixels)";
    }
}