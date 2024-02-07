import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {
    @Test
    public void testIsSameNumber(){
        assertFalse(Flik.isSameNumber(9, 10));
        assertTrue(Flik.isSameNumber(9, 9));
    }
}
