import lombok.Getter;
import org.junit.jupiter.api.Test;

@Getter
public class TestLombok {
    private String testField;

    public void setTestField(String testField) {
        this.testField = testField;
    }

    @Test
    public void testLombok() {
        TestLombok test = new TestLombok();
        test.setTestField("Hello, Lombok!");
        System.out.println(test.getTestField());
    }
}
